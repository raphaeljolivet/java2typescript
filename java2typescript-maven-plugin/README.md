# Purpose

This **maven** plugin is part of the [java2typescript](..) project.
It generates a typescript definition of REST services and corresponding DTO model out of [JAX-RS](https://jax-rs-spec.java.net/) annotated Java services.

# Goals

There is a single goal **generate** that generates both `.ts` and `.js` files

```
mvn java2typescript:generate
```

# Parameters

Here are the parameters handled by the plugin

| Name           | Expression        | Default value              | Meaning                  | 
|----------------|-------------------|----------------------------|--------------------------|
| serviceClass   | j2ts.serviceClass |  -                         | Class of REST service    |
| moduleName     | j2ts.tsOutFolder  |  -                         | Name of output module    |
| tsOutFolder    | j2ts.tsOutFolder  | ${project.build.directory} | Output folder for ts file|
| jsOutFolder    | j2ts.jsOutFolder  | ${project.build.directory} | Output folder for js file|

By default the two files will be generated as :
* `<tsOutFolder>/<moduleName>.d.ts`
* `<jsOutFolder>/<moduleName>.js`

# Setup

To use this plugin, you need to declare a custom repository, and bind the **generate** goal to some phase.




