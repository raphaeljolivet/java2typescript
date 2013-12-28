package com.fasterxml.jackson.module.typescript.grammar;

import java.io.IOException;
import java.io.Writer;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class MapType extends AbstractType {
	private AbstractType valueType;
	private AbstractType keyType;

	public MapType() {
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write("{ [key: ");
		keyType.write(writer);
		writer.write(" ]: ");
		valueType.write(writer);
		writer.write(";}");
	}

	public AbstractType getValueType() {
		return valueType;
	}

	public void setValueType(AbstractType valueType) {
		this.valueType = valueType;
	}

	public AbstractType getKeyType() {
		return keyType;
	}

	public void setKeyType(AbstractType keyType) {
		this.keyType = keyType;
	}

}
