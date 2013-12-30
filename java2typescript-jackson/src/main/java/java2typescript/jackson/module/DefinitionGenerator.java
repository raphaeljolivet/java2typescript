/*******************************************************************************
 * Copyright 2013 Raphael Jolivet
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package java2typescript.jackson.module;

import java.util.Collection;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.visitors.TSJsonFormatVisitorWrapper;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Main class that generates a TypeScript grammar tree (a Module), out of a
 * class, together with a {@link ObjectMapper}
 */
public class DefinitionGenerator {

	private final ObjectMapper mapper;

	public DefinitionGenerator(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * @param module
	 *            Module to be filled with named types (classes, enums, ...)
	 * @param classes
	 *            Class for which generating definition
	 * @throws JsonMappingException
	 */
	public Module generateTypeScript(String moduleName, Collection<? extends Class<?>> classes)
			throws JsonMappingException {

		Module module = new Module(moduleName);
		TSJsonFormatVisitorWrapper visitor = new TSJsonFormatVisitorWrapper(module);

		for (Class<?> clazz : classes) {
			mapper.acceptJsonFormatVisitor(clazz, visitor);
		}
		return module;
	}

}
