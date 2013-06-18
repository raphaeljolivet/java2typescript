Jackson Module TypeScript
=========================

This is a [Jackson](http://jackson.codehaus.org/) module.
It generates *TypeScript* definition files (.d.ts) for Java classes.

The purpose is to eventually integrate it into some **maven** plugin to generate *.d.ts* files
of REST API at build time, thus enabling to have **end-to-end** syntax validation while developping.

Status
======

The Generator currently support the following *TypeScript* syntax :

* Primitives : bool, string, number
* Interfaces (from classes)
* Typed arrays
* Typed Maps
* Enums


Usage
=====

Use the class **com.fasterxml.jackson.module.typescript.DefinitionGenerator** like so :
```java
	
	ObjectMapper mapper = new ObjectMapper();

	DefinitionGenerator generator = new DefinitionGenerator(mapper);
	
	Writer out = new OutputStreamWriter(System.out);
	generator.generateDefinition("SomeModuleName", out, TestClass.class);
	out.close();
	
```

Interface / Class name
----------------------

By default, the name of the generated interfaces is the Java simple class name.
You can use the annotation **@JsonTypeName("CustomClassName")** to override it.

Module name
-----------
The module name is optionnal. If null, the definition will be generated out of any module.


Credits
=======

Inspired from the Jackson module [jsonSchema](https://github.com/FasterXML/jackson-module-jsonSchema)



