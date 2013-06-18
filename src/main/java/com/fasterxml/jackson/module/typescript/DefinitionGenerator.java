package com.fasterxml.jackson.module.typescript;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.typescript.grammar.ANamedType;
import com.fasterxml.jackson.module.typescript.visitors.TSJsonFormatVisitorWrapper;

public class DefinitionGenerator {

	private final ObjectMapper mapper;

	public DefinitionGenerator(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * @param moduleName
	 *            May be null
	 * @param clazz
	 *            Class for which generating definition
	 * @throws IOException
	 */
	public void generateDefinition(String moduleName, Writer writer, Class<?> clazz) throws IOException {

		TSJsonFormatVisitorWrapper visitor = new TSJsonFormatVisitorWrapper(null);

		mapper.acceptJsonFormatVisitor(clazz, visitor);

		if (moduleName != null) {
			writer.write(format("module %s {\n\n", moduleName));
		}

		for (Object typeObj : visitor.getNamedTypes().values()) {
			if (moduleName != null) {
				writer.write("export");
			}
			((ANamedType) typeObj).writeDef(writer);
			writer.write("\n\n");
		}

		if (moduleName != null) {
			writer.write("}\n");
		}
	}

}
