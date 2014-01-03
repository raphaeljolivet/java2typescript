## Purpose

**Java2Typescript** provides a bridge between a **Java** REST service definition and a **Typescript** client. 

It enables to expose the full DTO model and REST services API as a clean typescript definition file, thus enabling strong type checking on the model of your application.

This project is composed of 3 modules :
* **[java2typescript-jackson](java2typescript-jackson)**: A [Jackson](http://jackson.codehaus.org/) module that generate **typescript** definition files for Java classes, using a Jackson ObjectMapper.
* **[java2typescript-jaxrs](java2typescript-jaxrs)**: An extension to **java2typescript-jackson** that takes a [JAX-RS](https://jax-rs-spec.java.net/) annotated java class and produces both :
 * A Typescript definition file of the service (`.d.ts`), together with description of all needed DTO objects. 
 * An implementation `.js `of the above definition as REST client stub. 
 * 
* **[java2typescript-maven-plugin](java2typescript-maven-plugin)**: A maven plugin to automate the generation of `.d.ts` and `.js` implementation of REST services.

## Big picture

Here is a schema of the workflow for a typical project using **j2ts** :
![j2ts workflow](img/j2ts-workflow.png)

There are only two source files here :
* Server side: `AppRest.java` with annotated JAX-RS services
* Client side: `App.ts` 

The detailed workflow is:

1. `AppRest.java` contains the annotated **JAX-RS** service definition
2. **j2ts** compiles the REST service definition into a `.d.ts` description file, and a `.js` file (runtime implementation)
3. `App.ts` imports and uses the `.d.ts` file.
4. `App.ts` is compiled into a `App.js` file

# Example

**java2typescript** handles all the HTTP REST standard itself, and present services like vanilla Typescript methods, regardless of the HTTP method / mime to use.


# Usage


# Licence

This project is licenced under the [Apache v2.0 Licence](http://www.apache.org/licenses/LICENSE-2.0.html)


# Credits

Jackson module is inspired from the [jsonSchema module](https://github.com/FasterXML/jackson-module-jsonSchema)



