package com.fasterxml.jackson.module.typescript.grammar;

import java.io.IOException;
import java.io.Writer;

public abstract class AType {
	public abstract void write(Writer writer) throws IOException;
}
