package com.fasterxml.jackson.module.typescript.grammar;

import java.io.IOException;
import java.io.Writer;

abstract public class APrimitiveType extends AType {
	
	private String token;
		
	public APrimitiveType(String token) {
		this.token = token;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(token);
	}
}
