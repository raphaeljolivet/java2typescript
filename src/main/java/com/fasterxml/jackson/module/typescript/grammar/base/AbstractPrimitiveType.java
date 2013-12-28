package com.fasterxml.jackson.module.typescript.grammar.base;

import java.io.IOException;
import java.io.Writer;

abstract public class AbstractPrimitiveType extends AbstractType {

	private String token;

	public AbstractPrimitiveType(String token) {
		this.token = token;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(token);
	}
}
