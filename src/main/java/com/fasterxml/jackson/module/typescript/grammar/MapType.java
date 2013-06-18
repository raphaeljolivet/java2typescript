package com.fasterxml.jackson.module.typescript.grammar;

import java.io.IOException;
import java.io.Writer;

public class MapType extends AType {
	private AType valueType;
	private AType keyType;

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

	public AType getValueType() {
		return valueType;
	}

	public void setValueType(AType valueType) {
		this.valueType = valueType;
	}

	public AType getKeyType() {
		return keyType;
	}

	public void setKeyType(AType keyType) {
		this.keyType = keyType;
	}

}
