package org.java2typescript.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jaxrs.ServiceDescriptorGenerator;
import java2typescript.jaxrs.model.RestService;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.google.common.io.Resources;

/**
 * Generate typescript file out of RESt service definition
 * 
 * @goal generate
 * @phase process-classes
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
public class MainMojo extends AbstractMojo {

  private static final String JS_TEMPLATE_RES = "module-template.js";

  private static final String MODULE_NAME_PLACEHOLDER = "%MODULE_NAME%";
  private static final String JSON_PLACEHOLDER = "%JSON%";

  /**
   * Full class name of the REST service
   * @required 
   * @parameter
   *    alias="j2ts.service.class" 
   *    expression="${j2ts.service.class}"
   */
  private String restServiceClassName;

  /**
   * Name of output module (ts,js)
   * @required 
   * @parameter
   *     alias="j2ts.module.name" 
   *     expression="${j2ts.module.name}"
   */
  private String moduleName;

  /**
   * Path to output typescript folder
   * The name will be <moduleName>.d.ts
   * @required
   * @parameter 
   *    alias="j2ts.out.ts"
   * 		expression="${j2ts.out.ts}" 
   * 		default-value = "${project.build.directory}"
   */
  private File tsOutFolder;

  /**
   * Path to output Js file
   * The name will be <moduleName>.js
   * 
   * @required
   * @parameter 
   *    alias="j2ts.out.js"
   * 		expression="${j2ts.out.js}" 
   * 		default-value = "${project.build.directory}"
   */
  private File jsOutFolder;

  @Override
  public void execute() throws MojoExecutionException {

    try {

      // Descriptor for service
      Class<?> serviceClass = Class.forName(restServiceClassName);
      ServiceDescriptorGenerator descGen = new ServiceDescriptorGenerator(serviceClass);

      // To Typescript
      {
        Writer writer = createFileAndGetWriter(tsOutFolder, moduleName + ".d.ts");
        Module tsModule = descGen.generateTypeScript(moduleName);
        tsModule.write(writer);
        writer.close();
      }

      // To JS
      {
        Writer outFileWriter = createFileAndGetWriter(jsOutFolder, moduleName + ".js");

        // Generate JSON as String
        StringWriter json = new StringWriter();
        RestService restService = descGen.generateRestService();
        restService.toJSON(json);

        // Read template content
        String jsTemplate = Resources.toString(//
            MainMojo.class.getResource(JS_TEMPLATE_RES), //
            Charset.defaultCharset());

        // Replace template values
        String out = jsTemplate.replace(MODULE_NAME_PLACEHOLDER, moduleName);
        out = out.replace(JSON_PLACEHOLDER, json.toString());

        outFileWriter.write(out);
        outFileWriter.close();
      }

    }
    catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  private Writer createFileAndGetWriter(File folder, String fileName) throws IOException {
    File file = new File(folder, fileName);
    getLog().info("Create file : " + file.getCanonicalPath());
    file.createNewFile();
    FileOutputStream stream = new FileOutputStream(file);
    OutputStreamWriter writer = new OutputStreamWriter(stream);
    return writer;
  };
}
