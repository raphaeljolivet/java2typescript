package com.fasterxml.jackson.module.typescript.grammar;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractPrimitiveType;

public class NumberType extends AbstractPrimitiveType {

	static private NumberType instance = new NumberType();

	static public NumberType getIntance() {
		return instance;
	}

	private NumberType() {
		super("number");
	}
}
