package com.fasterxml.jackson.module.typescript.grammar;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class FunctionType extends AbstractType {

	private LinkedHashMap<String, AbstractType> parameters = new LinkedHashMap<String, AbstractType>();

	private AbstractType resultType;

	@Override
	public void write(Writer writer) throws IOException {
		writer.write("(");
		int i = 1;
		for (Entry<String, AbstractType> entry : parameters.entrySet()) {
			writer.write(entry.getKey());
			writer.write(": ");
			entry.getValue().write(writer);
			if (i < parameters.size()) {
				writer.write(",");
			}
			i++;
		}
		writer.write("):");
		resultType.write(writer);
	}

	public LinkedHashMap<String, AbstractType> getParameters() {
		return parameters;
	}

	public AbstractType getResultType() {
		return resultType;
	}

	public void setResultType(AbstractType resultType) {
		this.resultType = resultType;
	}

}
