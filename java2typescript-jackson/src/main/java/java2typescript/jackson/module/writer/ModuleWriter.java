package java2typescript.jackson.module.writer;

import java.io.IOException;
import java.io.Writer;

import java2typescript.jackson.module.grammar.Module;

public interface ModuleWriter {
	void write(Module module, Writer writer) throws IOException;
}
