// java2ts module
j2ts = {
  
  // -- Adapters for REST calls
  adapters : {
  
     // >> To console
     console: function(httpMethod, path, getParams, postParams, body) {
       console.log([httpMethod, path, getParams, postParams, body]);
     },
  
     // >> Jquery
     jQuery : function jQueryAdapter(httpMethod, url, getParams, postParams, body) {
       var processData = true;
       var data;
     if (getParams != null) {
       // in URL
       data = getParams;
     } else if (postParams != null) {
       // as form Params
       data = postParams;
     } else if (body != null) {
       // as JSON in body
       data = JSON.stringify(body);
       processData = false;
     }
 
     jQuery.ajax({
       type : httpMethod,
       url : url,
       processData : processData,
       data : data
     }).done(function(data) {
       console.log(data);
     })
    } // end of jQuery adapter
  } // end of adapters

} // end of j2ts module definition 

j2ts.buildModule = function(desc) {

  var outModule = {
    
    rootUrl : null,
    
    // JQuery by default
    adapter : j2ts.adapters.jQuery
    
  };
  
  function methodGenerator(methodDesc) {
    
    return function() {
      
      if (outModule.rootUrl == null) {
         throw new Error("rootUrl is null, set it before calling rest methods");
      }     
      
      var url = outModule.rootUrl + desc.path + methodDesc.path
      
      // Init params to be passed to adapter function
      var getParams = null;
      var postParams = null;
      var body = null;
      
      // Loop on params
      for (var i=0; i< methodDesc.params.length; i++) {
        var paramValue = arguments[i];
        var paramDesc = methodDesc.params[i];

        switch(paramDesc.type) {
          case "FORM" :
             if (postParams == null) postParams = {};
             postParams[paramDesc.name] = paramValue;
             break;
          case "QUERY" :
             if (getParams == null) getParams = {};
             getParams[paramDesc.name] = paramValue;
             break;
          case "PATH" :
             url = url.replace("{" + paramDesc.name + "}", paramValue)
             break;
          case "BODY" :
             body = paramValue;
        }    
      }
      
      // Actual call to the adapter
      outModule.adapter(methodDesc.httpMethod, url, getParams, postParams, body);
    }
  }
  
  // Dynamically add some methods to the module
  for (var methodName in desc.methods) {  
    var methodDesc = desc.methods[methodName]; 
    outModule[methodName] = methodGenerator(methodDesc);  
  }
  
  return outModule;
}

%MODULE_NAME% = j2ts.buildModule(%JSON%);


