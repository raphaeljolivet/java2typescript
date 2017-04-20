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

import com.google.common.collect.Lists;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jaxrs.ServiceDescriptorGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Generate typescript file out of RESt service definition
 *
 * @goal generate
 * @phase process-classes
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
public class MainMojo extends AbstractMojo {

    /**
     * Full class name of the REST service
     *
     * @deprecated Use the parameter "serviceClasses" instead
     * @parameter alias="serviceClass"
     * expression="${j2ts.serviceClass}"
     */
    @Deprecated
    private String restServiceClassName;

    /**
     * Full class names of the REST services to parse
     *
     * @parameter alias="serviceClasses"
     * expression="${j2ts.serviceClasses}"
     */
    private List<String> restServiceClassNames;

    /**
     * Service class patterns to process
     * Is used together with the parameter serviceClassPackages
     * Allows to restrict the parsed service classes
     *
     * @parameter alias="serviceClassPatterns"
     * expression="${j2ts.serviceClassPatterns}"
     */
    private List<String> serviceClassPatterns;

    /**
     * Service class patterns to exclude
     * Is used together with the parameter serviceClassPackages
     * Allows to exclude classes matching the specified patterns
     *
     * @parameter alias="excludedServiceClassPatterns"
     * expression="${j2ts.excludedServiceClassPatterns}"
     */
    private List<String> excludedServiceClassPatterns;

    /**
     * Packages to parse services from
     * Is used together with the parameter serviceClassPatterns
     *
     * @parameter alias="serviceClassPackages"
     * expression="${j2ts.serviceClassPackages}"
     */
    private List<String> serviceClassPackages;

    /**
     * Name of output module (ts,js)
     *
     * @required
     * @parameter alias="moduleName"
     * expression="${j2ts.moduleName}"
     */
    private String moduleName;

    /**
     * Path to output typescript folder
     * The name will be <moduleName>.d.ts
     *
     * @required
     * @parameter alias="tsOutFolder"
     * expression="${j2ts.tsOutFolder}"
     * default-value = "${project.build.directory}"
     */
    private File tsOutFolder;

    /**
     * Path to output Js file
     * The name will be <moduleName>.js
     *
     * @required
     * @parameter alias="jsOutFolder"
     * expression="${j2ts.jsOutFolder}"
     * default-value = "${project.build.directory}"
     */
    private File jsOutFolder;

    /**
     * @required
     * @parameter
     * default-value="${project}"
     */
    private MavenProject project;


    @Override
    public void execute() throws MojoExecutionException {
        if (serviceClassPatterns.size() == 0) {
            serviceClassPatterns.add(".*");
        }
        List<Class<?>> classesToParse = getClassesToParse();

        try {
            List<Pattern> parsedInclusionPatterns = parsePatterns(serviceClassPatterns);
            List<Pattern> parsedExclusionPatterns = parsePatterns(excludedServiceClassPatterns);

            for (Class<?> serviceClass : classesToParse) {
                boolean isIncluded = matchFound(parsedInclusionPatterns, serviceClass.getName());
                boolean isExcluded = matchFound(parsedExclusionPatterns, serviceClass.getName());

                if (isIncluded && !isExcluded) {
                    parseClass(serviceClass);
                }
            }
        } catch (PatternSyntaxException e) {
            throw new MojoExecutionException("Parsing of inclusion patterns returned an error: \"" + e.getMessage() + "\"", e);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private List<Class<?>> getClassesToParse() throws MojoExecutionException {
        List<Class<?>> classesToParse = new LinkedList<Class<?>>();
        if (restServiceClassName != null) {
            Class<?> serviceClass = classForName(restServiceClassName);
            classesToParse.add(serviceClass);
        }

        if (serviceClassPatterns != null) {
            for (String serviceClassPackage : serviceClassPackages) {
                classesToParse.addAll(getClassesInPackage(serviceClassPackage));
            }
        }

        return classesToParse;
    }

    private Collection<Class<?>> getClassesInPackage(String packageName) throws MojoExecutionException {
        Reflections reflections = new Reflections(packageName, new MethodParameterScanner(), new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class);
    }

    private List<Pattern> parsePatterns(List<String> patternStrings) {
        List<Pattern> parsedPatterns = new LinkedList<Pattern>();
        for (String serviceClassPattern : patternStrings) {
            parsedPatterns.add(Pattern.compile(serviceClassPattern));
        }
        return parsedPatterns;
    }

    private boolean matchFound(List<Pattern> patterns, String testString) {
        for (Pattern pattern : patterns) {
            if (pattern.matcher(testString).matches()) {
                return true;
            }
        }
        return false;
    }


    private Class<?> classForName(String className) throws MojoExecutionException {
        try {
            return Class.forName(restServiceClassName);
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException("Could not find class \"" + className + "\"!", e);
        }
    }

    private void parseClass(Class<?> serviceClass) throws IOException {
        getLog().info("Parsing class " + serviceClass.getCanonicalName());
        ServiceDescriptorGenerator descGen = new ServiceDescriptorGenerator(Lists.newArrayList(serviceClass));

        // To Typescript
        Writer writer = createFileAndGetWriter(tsOutFolder, moduleName + ".d.ts");
        Module tsModule = descGen.generateTypeScript(moduleName);
        tsModule.write(writer);
        writer.close();


        // To JS
        Writer outFileWriter = createFileAndGetWriter(jsOutFolder, moduleName + ".js");
        descGen.generateJavascript(moduleName, outFileWriter);
        outFileWriter.close();

    }

    private Writer createFileAndGetWriter(File folder, String fileName) throws IOException {
        File file = new File(folder, fileName);
        getLog().info("Create file : " + file.getCanonicalPath());
        file.createNewFile();
        FileOutputStream stream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        return writer;
    }

    ;
}
