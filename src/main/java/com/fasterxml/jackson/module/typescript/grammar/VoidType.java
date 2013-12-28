package com.fasterxml.jackson.module.typescript.grammar;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractPrimitiveType;

public class VoidType extends AbstractPrimitiveType {

	static private VoidType instance = new VoidType();

	static public VoidType getInstance() {
		return instance;
	}

	private VoidType() {
		super("void");
	}
}
