package com.fasterxml.jackson.module.typescript.grammar;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractPrimitiveType;

public class StringType extends AbstractPrimitiveType {

	static private StringType instance = new StringType();

	static public StringType getIntance() {
		return instance;
	}

	private StringType() {
		super("string");
	}
}
