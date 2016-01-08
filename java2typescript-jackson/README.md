This project generates TypeScript code from given Java Classes

## Output format
Java classes are converted to TypeScript interfaces.


### Module (generated output) format
TypeScript has concept of [internal](http://www.typescriptlang.org/Handbook#modules) and [external](http://www.typescriptlang.org/Handbook#modules-going-external) modules, that have slightly different format (see the test output for [internal](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/resources/java2typescript/jackson/module/DefinitionGeneratorTest.internalModuleFormat.d.ts) vs [external](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/resources/java2typescript/jackson/module/DefinitionGeneratorTest.externalModuleFormat.d.ts) module format). By default internal module format is used (that adds one line before and after content of external module format).

External module format can be used by using different ModuleWriter (ExternalModuleFormatWriter - see [the test for an example](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/java/java2typescript/jackson/module/DefinitionGeneratorTest.java#L85-L97))


### Ignored methods
When generating TypeScript from Java classes <sup>(actually when Java classes are analysed)</sup>, some methods are excluded:
* Non-public methods
* Methods annotated with @java.beans.Transient
* Java Bean property getters/setters are excluded even if field doesn't exist with exact name (field is generated based on corresponding Java Bean property name instead)
* Methods ignored by the configuration:

```Java
		Configuration conf = new Configuration();
		conf.addIngoredMethod("blacklistedMethod");
```

See the [tests for excluded methods](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/java/java2typescript/jackson/module/ExcludedMethodsTest.java#L33-L65) for details.

> Note: You can also extend the configuration and overwrite [`isIgnoredMethod`](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/main/java/java2typescript/jackson/module/Configuration.java#L44-L49) method to programmatically make the decision (most likely based on method signature: parameters, return type, declaring class, annotations, ...)


### Enums
Java enums are converted to TypeScript enums by default,
but TypeSafe enum pattern can be used to force generating classes instead of enums (see [the description of the issue](https://github.com/raphaeljolivet/java2typescript/issues/13).
Example [output](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/resources/java2typescript/jackson/module/WriterPreferencesTest.enumToEnumPattern.d.ts) from test that [turns on this preference](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/java/java2typescript/jackson/module/WriterPreferencesTest.java#L44) using

```Java
mWriter.preferences.useEnumPattern();
```

### Mapping specific java classes to custom TypeScript types
There are scenarios when You might want to use different TypeScript type instead of specific Java Type. There are several options for doing this depending how You want it to be done:

##### Renaming Type and emitting it to the output
You can use `@com.fasterxml.jackson.annotation.JsonTypeName("ChangedEnumName")` annotation on the Java type to use different name in TypeScript output (interface/enum with different name is also generated to the output).
https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/java/java2typescript/jackson/module/DefinitionGeneratorTest.java#L37

##### Using different type, not emitting it to the output
One common use-case, could be for instance Joda or Java8 LocalDate or Date or Calendar to TypeScript/JavaScript Date.
See the test [source](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/java/java2typescript/jackson/module/CustomTypeDefinitionGeneratorTest.java#L57) and [output](https://github.com/raphaeljolivet/java2typescript/blob/master/java2typescript-jackson/src/test/resources/java2typescript/jackson/module/CustomTypeDefinitionGeneratorTest.classWithCustomTypeFields.d.ts) as an example.

Mapping Java type to TypeScript type is done using:

```Java
new Configuration().addType(CustomDate.class, DateType.getInstance())
```

where `DateType` class specifies the expected output type name.

