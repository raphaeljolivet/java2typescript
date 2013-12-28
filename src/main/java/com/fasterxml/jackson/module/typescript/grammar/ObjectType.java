package com.fasterxml.jackson.module.typescript.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractNamedType;
import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class ObjectType extends AbstractNamedType {

	private Map<String, AbstractType> fields = new LinkedHashMap<String, AbstractType>();

	private Map<String, FunctionType> methods = new LinkedHashMap<String, FunctionType>();

	public ObjectType(String className) {
		super(className);
	}

	@Override
	public void writeDef(Writer writer) throws IOException {
		writer.write(format("interface %s {\n", name));
		for (Entry<String, AbstractType> entry : fields.entrySet()) {
			writer.write(format("    %s: ", entry.getKey()));
			entry.getValue().write(writer);
			writer.write(";\n");
		}
		for (String methodName : methods.keySet()) {
			writer.write("    " + methodName);
			this.methods.get(methodName).write(writer);
			writer.write(";\n");
		}
		writer.write("}");
	}

	public Map<String, AbstractType> getFields() {
		return fields;
	}

	public void setFields(Map<String, AbstractType> fields) {
		this.fields = fields;
	}

	public Map<String, FunctionType> getMethods() {
		return methods;
	}
}
