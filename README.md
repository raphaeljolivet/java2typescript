# Java to Typescript

The aim of this project is to provide a bridge between a **Java** REST service definition and 
a **Typescript** client. 

It enables to expose the full DTO model structure and REST service API as a clean typescript definition file, thus enabling strong type checking on the model of your application.

This project is composed of 3 modules :
* **java2typescript-jackson**: A [Jackson](http://jackson.codehaus.org/) module that generate **typescript** definition files for Java classes, using a Jackson ObjectMapper.
* **java2typescript-jaxrs**: A [JAX-RS](https://jax-rs-spec.java.net/) module that takes a JAX-RS-annotated java class and produces both :
 * A Typescript definition file of the service (`.d.ts`)
 * A `.json` file with additional info : HTTP method (POST, GET), parameter's locations (path, form params, body, ..)
* **java2typescript-maven-plugin**: A maven plugin to automate the generation of `.d.ts` and `.json` files for a REST service

# Jackson Module

This is a [Jackson](http://jackson.codehaus.org/) module.
It generates *TypeScript* definition files (.d.ts) for Java classes.

## Status

The Generator currently support the following *TypeScript* syntax :

* Primitives : bool, string, number
* Interfaces (from classes)
* Typed arrays
* Typed Maps
* Enums
* Methods

## Usage

Use the class **DefinitionGenerator** like so :
```java
	
ObjectMapper mapper = new ObjectMapper();
DefinitionGenerator generator = new DefinitionGenerator(mapper);

Module module = generator.generateTypeScript(//
    "moduleName", //
    newArrayList(//
           MyClass.class, //
           OtherClass.class));
           
module.write(outWriter);
```

## Sample output

```typescript

module MyModule {

export enum MyEnum {
    VAL1,
    VAL2,
    VAL3,
}

export interface TestClass {
    aString: string;
    aBoolean: bool;
    aInt: number;
    aFloat: number;
    stringArray: string[];
    map: { [key: string ]: bool;};
    recursive: TestClass;
    recursiveArray: TestClass[];
    aEnum: ChangedEnumName;
}

}
```


## Interface / Class name


By default, the name of the generated interfaces is the Java simple class name.
You can use the annotation **@JsonTypeName("CustomClassName")** to override it.

## Module name

The module name is optionnal. If null, the definition will be generated out of any module.

# Licence

This project is licenced under the [Apache v2.0 Licence](http://www.apache.org/licenses/LICENSE-2.0.html)


# Credits

Jackson module is inspired from the [jsonSchema module](https://github.com/FasterXML/jackson-module-jsonSchema)



