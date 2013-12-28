package com.fasterxml.jackson.module.typescript.grammar;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractPrimitiveType;

public class AnyType extends AbstractPrimitiveType {

	static private AnyType instance = new AnyType();

	static public AnyType getIntance() {
		return instance;
	}

	private AnyType() {
		super("any");
	}
}
