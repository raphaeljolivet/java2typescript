package com.fasterxml.jackson.module.typescript.grammar;

import java.io.IOException;
import java.io.Writer;

public class ArrayType extends AType {
	private AType itemType;

	public ArrayType() {
	}

	public ArrayType(AType aType) {
		itemType = aType;
	}

	@Override
	public void write(Writer writer) throws IOException {
		itemType.write(writer);
		writer.write("[]");
	}

	public void setItemType(AType itemType) {
		this.itemType = itemType;
	}

}
