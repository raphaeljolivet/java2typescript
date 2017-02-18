This project generates TypeScript code from given Java Classes

## Output format
Java classes are converted to TypeScript interfaces.


### Module (generated output) format
TypeScript has a concept of [internal](http://www.typescriptlang.org/Handbook#modules) and [external](http://www.typescriptlang.org/Handbook#modules-going-external) modules, that have slightly different formats (see the test output for [internal](src/test/resources/java2typescript/jackson/module/DefinitionGeneratorTest.internalModuleFormat.d.ts) vs [external](src/test/resources/java2typescript/jackson/module/DefinitionGeneratorTest.externalModuleFormat.d.ts) module format). By default, internal module format is used (that adds one line before and after content of external module format). To write in the external module format, use the `ExternalModuleFormatWriter` class (see [the test for an example](src/test/java/java2typescript/jackson/module/DefinitionGeneratorTest.java#L85-L97)).

A third option is to use the `AmbientdModuleFormatWriter`, which behaves much like the `InternalModuleFormatWriter`, except it uses `declare module ...` on the outer wrapping module instead of `export module ...`. This is called an "ambient" module in TypeScript lingo. For more information, see the [typescript docs on modules](https://www.typescriptlang.org/docs/handbook/modules.html).

>NOTE: A note about terminology: It's important to note that in TypeScript 1.5, the nomenclature has changed. "Internal modules" are now "namespaces". "External modules" are now simply "modules", as to align with ECMAScript 2015's terminology, (namely that module X { is equivalent to the now-preferred namespace X {).

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

See the [tests for excluded methods](src/test/java/java2typescript/jackson/module/ExcludedMethodsTest.java#L33-L65) for details.

> Note: You can also extend the configuration and overwrite the [`isIgnoredMethod`](src/main/java/java2typescript/jackson/module/Configuration.java#L44-L49) method to programmatically make the decision (most likely based on method signature: parameters, return type, declaring class, annotations, ...). You may also want to override this method to always return true if you don't want to include any methods in your Typescript definitions.


### Enums
Java enums are converted to TypeScript enums by default,
but TypeSafe enum pattern can be used to force the generation of classes instead of enums (see [the description of the issue](https://github.com/raphaeljolivet/java2typescript/issues/13).
See [this output](src/test/resources/java2typescript/jackson/module/WriterPreferencesTest.enumToEnumPattern.d.ts) from the [test that turns on this preference](src/test/java/java2typescript/jackson/module/WriterPreferencesTest.java#L49) using

```Java
mWriter.preferences.useEnumPattern();
```

Another option available is to write enums as [String Literal Types](https://www.typescriptlang.org/docs/handbook/advanced-types.html#string-literal-types).
The advantage here is that the generated typescript definitions can be used with JSON that has the string value of the corresponding java enum value, while still maintaining
strong-typing. For example, take this java DTO class:

```java
class MyDto {
    static enum MyEnum {
        VAL1, VAl2, VAL3
    }
    public MyEnum myKey;
}

```

A java web server which is converting an instance of `MyDto` to JSON will likely return something like this:

```json
{
  "myKey": "VAL1"
}
```

The default for this tool is to create Typescript enums, which are integer-based. This will not work without some
conversion layer in your client code. To mitigate, you can use the `useStringLiteralTypeForEnums` option on the module
writer. This will generate a Typescript definition like this:

```TypeScript
export interface MyDto {
    myKey: MyEnum;
}
export type MyEnum = "VAL1" | "VAl2" | "VAL3";
```

This will keep strong-typing for the values, while allowing for string literal values of enum properties. Take a look at
the [test that turns on this preference](src/test/java/java2typescript/jackson/module/WriterPreferencesTest.java#L67).

The `useStringLiteralTypeForEnums` method takes an optional boolean argument called `withConstants`. If this is set to `true`, two definitions will be generated from each java enum definition. So this:

```Java
class MyDto {
    static enum MyEnum {
        VAL1, VAl2, VAL3
    }
    public MyEnum myKey;
}
```

Will generate this:

```TypeScript
export interface MyDto {
    myKey: MyEnum;
}
export type MyEnum = "VAL1" | "VAl2" | "VAL3";
export class MyEnumValues {
    static VAL1: EnumOneValue = "VAL1";
    static VAL2: EnumOneValue = "VAL2";
    static VAL3: EnumOneValue = "VAL3";
}
```

The reason for this feature is that prior to typescript v2.0.2, compile-time checks for string literal types were not exhaustive, resulting in certain situations where type-checking was unavailable (see discussion at [#68](https://github.com/raphaeljolivet/java2typescript/issues/68)). If you are using v2.0.2 or later, it is likely less helpful/necessary to enable this option.


### Mapping specific java classes to custom TypeScript types
There are scenarios when you might want to use a different TypeScript type instead of the default Java Type. There are several options for doing this depending how you want it to be done:

##### Renaming Type and emitting it to the output
Logic that determines the TypeScript type based on Java class is implemented using the [TSTypeNamingStrategy](src/main/java/java2typescript/jackson/module/conf/typename/TSTypeNamingStrategy.java) interface. Currently there are two implementations provided out of the box by this library:
1. [SimpleJacksonTSTypeNamingStrategy](src/main/java/java2typescript/jackson/module/conf/typename/SimpleJacksonTSTypeNamingStrategy.java) - The default, uses the Java class name for the TypeScript type, unless the class has an annotation that specifies a custom name (see bellow).
1. [WithEnclosingClassTSTypeNamingStrategy](jackson/module/conf/typename/WithEnclosingClassTSTypeNamingStrategy.java) - extends SimpleJacksonTSTypeNamingStrategy to include the enclosing class name as a prefix (`javaClass.getName()` without package).

###### Renaming Type using annotation on the Java class (SimpleJacksonTSTypeNamingStrategy)
You can use the `@com.fasterxml.jackson.annotation.JsonTypeName("ChangedEnumName")` annotation on the Java type to use a different name in TypeScript output (interface/enum with different name is also generated in the output).
See the [example from the test](src/test/java/java2typescript/jackson/module/DefinitionGeneratorTest.java#L37).

###### Renaming Types using custom (re)naming strategy
To tweak naming TypeScript types for your specific needs, you can provide an implementation of [TSTypeNamingStrategy](src/main/java/java2typescript/jackson/module/conf/typename/TSTypeNamingStrategy.java), such as [SimpleJacksonTSTypeNamingStrategy](src/main/java/java2typescript/jackson/module/conf/typename/SimpleJacksonTSTypeNamingStrategy.java) (or write your own) to [Configuration.setNamingStrategy(namingStrategy)](src/main/java/java2typescript/jackson/module/Configuration.java#L61).

See the test [TypeRenamingWithEnclosingClassTest](src/test/java/java2typescript/jackson/module/TypeRenamingWithEnclosingClassTest.java#L34) and the [expected output of the test](src/test/resources/java2typescript/jackson/module/TypeRenamingWithEnclosingClassTest.twoClassesWithSameName.d.ts#L1).


##### Using different type, not emitting it to the output
One common use-case, could be for instance Joda or Java8 LocalDate or Date or Calendar to TypeScript/JavaScript Date.
See the test [source](src/test/java/java2typescript/jackson/module/CustomTypeDefinitionGeneratorTest.java#L57) and [output](src/test/resources/java2typescript/jackson/module/CustomTypeDefinitionGeneratorTest.classWithCustomTypeFields.d.ts) as an example.

Mapping Java type to TypeScript type is done using:

```Java
new Configuration().addType(CustomDate.class, DateType.getInstance())
```

where `DateType` class specifies the expected output type name.


##### Emitting `declare type SomeTypeName = SomeOtherTypeOrTypeExpression` instead of interface
There are cases where instead of asking to emit TypeScript interface for specific Java type,
You might want to specify the TypeScript type declaration
(with potentially different type name)
to be used instead of the interface of the Java type. Here is an example:

You might have `LocalDate` class in Java, and custom deserializer that can accept either string or number, so that in TypeScript You should be able to assign either of them to that type:
```TypeScript
type DateWithoutTime = string | number;
```
This could be accomplished using custom configuration:
```Java
Configuration conf = new Configuration()
	.addType(LocalDate.class, new TypeDeclarationType("DateWithoutTime", "string | number"));
```
There are more use-cases for this solution - see
[the description of the issue](https://github.com/raphaeljolivet/java2typescript/issues/76)
and the test [source](src/test/java/java2typescript/jackson/module/CustomTypeDefinitionGeneratorTest.java#L82)
and [output](src/test/resources/java2typescript/jackson/module/CustomTypeDefinitionGeneratorTest.classWithCustomTypeFields.d.ts) as an example.
