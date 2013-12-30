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
import java2typescript.jaxrs.model.RestService;

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
	 * @parameter expression="${j2ts.service.class}"
	 */
	private String restServiceClassName;

	/**
	 * Path to output typescript file
	 * @required
	 * @parameter 
	 * 		expression="${j2ts.out.typescript}" 
	 * 		default-value = "${project.build.directory}/j2ts.d.ts"
	 */
	private File tsOutFile;

	/**
	 * Path to output JSON file
	 * 
	 * @required
	 * @parameter 
	 * 		expression="${j2ts.out.json}" 
	 * 		default-value = "${project.build.directory}/j2ts.json"
	 */
	private File jsonOutFile;

	@Override
	public void execute() throws MojoExecutionException {

		try {

			// Descriptor for service
			Class<?> serviceClass = Class.forName(restServiceClassName);
			ServiceDescriptorGenerator descGen = new ServiceDescriptorGenerator(serviceClass);

			// To Typescript
			{
				Writer writer = createFileAndGetWriter(tsOutFile);
				Module tsModule = descGen.generateTypeScript(null);
				tsModule.write(writer);
				writer.close();
			}

			// To JSON
			{
				Writer writer = createFileAndGetWriter(jsonOutFile);
				RestService restService = descGen.generateRestService();
				restService.toJSON(writer);
				writer.close();
			}

		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private Writer createFileAndGetWriter(File file) throws IOException {
		getLog().info("Create file : " + file.getCanonicalPath());
		file.createNewFile();
		FileOutputStream stream = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(stream);
		return writer;
	};
}
