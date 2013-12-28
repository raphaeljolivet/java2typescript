package com.fasterxml.jackson.module.typescript.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractNamedType;

public class EnumType extends AbstractNamedType {

	private List<String> values = new ArrayList<String>();

	public EnumType(String className) {
		super(className);
	}

	@Override
	public void writeDef(Writer writer) throws IOException {
		writer.write(format("enum %s {\n", name));
		for (String value : values) {
			writer.write(format("    %s,\n", value));
		}
		writer.write("}");
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

}
