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
import java.io.Writer;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jaxrs.ServiceDescriptorGenerator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

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
	 * @required 
	 * @parameter
	 *    alias="serviceClass" 
	 *    expression="${j2ts.serviceClass}"
	 */
	private String restServiceClassName;

	/**
	 * Name of output module (ts,js)
	 * @required 
	 * @parameter
	 *     alias="moduleName" 
	 *     expression="${j2ts.moduleName}"
	 */
	private String moduleName;

	/**
	 * Path to output typescript folder
	 * The name will be <moduleName>.d.ts
	 * @required
	 * @parameter 
	 *    alias="tsOutFolder" 
	 * 		expression="${j2ts.tsOutFolder}" 
	 * 		default-value = "${project.build.directory}"
	 */
	private File tsOutFolder;

	/**
	 * Path to output Js file
	 * The name will be <moduleName>.js
	 * 
	 * @required
	 * @parameter 
	 *    alias="jsOutFolder"
	 * 		expression="${j2ts.jsOutFolder}" 
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

				outFileWriter.write(out);
				outFileWriter.close();
			}

		} catch (Exception e) {
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
