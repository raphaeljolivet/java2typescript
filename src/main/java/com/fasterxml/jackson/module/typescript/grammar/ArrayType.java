package com.fasterxml.jackson.module.typescript.grammar;

import java.io.IOException;
import java.io.Writer;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class ArrayType extends AbstractType {
	private AbstractType itemType;

	public ArrayType() {
	}

	public ArrayType(AbstractType aType) {
		itemType = aType;
	}

	@Override
	public void write(Writer writer) throws IOException {
		itemType.write(writer);
		writer.write("[]");
	}

	public void setItemType(AbstractType itemType) {
		this.itemType = itemType;
	}

}
