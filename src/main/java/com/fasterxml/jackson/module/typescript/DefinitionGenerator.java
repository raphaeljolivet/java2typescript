package com.fasterxml.jackson.module.typescript;

import java.util.Collection;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.typescript.grammar.Module;
import com.fasterxml.jackson.module.typescript.visitors.TSJsonFormatVisitorWrapper;

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
