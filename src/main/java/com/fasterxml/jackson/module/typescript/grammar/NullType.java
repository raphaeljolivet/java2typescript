package com.fasterxml.jackson.module.typescript.grammar;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractPrimitiveType;

public class NullType extends AbstractPrimitiveType {

	static private NullType instance = new NullType();

	static public NullType getIntance() {
		return instance;
	}

	private NullType() {
		super("null");
	}
}
