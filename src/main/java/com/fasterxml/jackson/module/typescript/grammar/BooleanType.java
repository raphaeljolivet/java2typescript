package com.fasterxml.jackson.module.typescript.grammar;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractPrimitiveType;

public class BooleanType extends AbstractPrimitiveType {

	static private BooleanType instance = new BooleanType();

	static public BooleanType getIntance() {
		return instance;
	}

	private BooleanType() {
		super("bool");
	}
}
