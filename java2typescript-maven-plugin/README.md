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

| Name                          | Expression                           | Default value              | Meaning                                     |
|----------------------------------------------------------------------|----------------------------|---------------------------------------------|
| serviceClasses                | j2ts.serviceClasses                  |  -                         | List of REST service classes                |
| serviceClassPackages          | j2ts.serviceClassPackages            | []                         | List of REST service packages               |
| serviceClassPatterns          | j2ts.serviceClassPatterns            | [".*"]                     | Regex pattern to select services to parse   |
| excludedServiceClassPatterns  | j2ts.excludedServiceClassPatterns    | []                         | Regex paterns to select services to exclude |
| moduleName                    | j2ts.moduleName                      |  -                         | Name of output module                       |
| tsOutFolder                   | j2ts.tsOutFolder                     | ${project.build.directory} | Output folder for ts file                   |
| jsOutFolder                   | j2ts.jsOutFolder                     | ${project.build.directory} | Output folder for js file                   |

By default the two files will be generated as :
* `<tsOutFolder>/<moduleName>.d.ts`
* `<jsOutFolder>/<moduleName>.js`

# Setup

To use this plugin, you first need to declare a custom repository :

```xml
    <pluginRepositories>
        <pluginRepository>
            <id>jitpack.io2</id>
            <name>jitpack.io Plugin Repository</name>
            <url>https://jitpack.io</url>
            <layout>default</layout>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <updatePolicy>always</updatePolicy>
            </releases>
        </pluginRepository>
    </pluginRepositories>
```

and add the plugin:
```xml
	<build>
		<plugins>
            <plugin>
                <groupId>com.github.raphaeljolivet.java2typescript</groupId>
                <artifactId>java2typescript-maven-plugin</artifactId>
                <version>FIXME-change-the-version</version>
                <configuration>
                    <!-- FIXME see the configuration parameters above -->
                    <serviceClass>com.example.rs.PeopleRestService</serviceClass>
                    <moduleName>People</moduleName>
                </configuration>
			</plugin>
		</plugins>
	</build>
```

> Note, that You should change the configuration properties (see above) and plugin version (take a look at [installation instruction](../../../#installation)s from parent project)




