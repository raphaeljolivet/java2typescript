package com.fasterxml.jackson.module.typescript.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ObjectType extends ANamedType {

	private Map<String, AType> fields = new LinkedHashMap<String, AType>();

	public ObjectType(String className) {
		super(className);
	}

	@Override
	public void writeDef(Writer writer) throws IOException {
		writer.write(format("interface %s {\n", name));
		for (Entry<String, AType> entry : fields.entrySet()) {
			writer.write(format("    %s: ", entry.getKey()));
			entry.getValue().write(writer);
			writer.write(";\n");
		}
		writer.write("}");
	}

	public Map<String, AType> getFields() {
		return fields;
	}

	public void setFields(Map<String, AType> fields) {
		this.fields = fields;
	}
}
