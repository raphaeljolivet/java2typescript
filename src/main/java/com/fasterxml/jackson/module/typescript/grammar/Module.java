package com.fasterxml.jackson.module.typescript.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractNamedType;

public class Module {

	private String name;

	private Map<String, AbstractNamedType> namedTypes = new HashMap<String, AbstractNamedType>();

	public Module() {
	}

	public Module(String name) {
		this.name = name;
	}

	public Map<String, AbstractNamedType> getNamedTypes() {
		return namedTypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void write(Writer writer) throws IOException {
		if (name != null) {
			writer.write(format("module %s {\n\n", name));
		}

		for (Object typeObj : namedTypes.values()) {
			if (name != null) {
				writer.write("export ");
			}
			((AbstractNamedType) typeObj).writeDef(writer);
			writer.write("\n\n");
		}

		if (name != null) {
			writer.write("}\n");
		}
		writer.flush();
	}

}
