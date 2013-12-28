package com.fasterxml.jackson.module.typescript.grammar.base;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractType {
	public abstract void write(Writer writer) throws IOException;
}
