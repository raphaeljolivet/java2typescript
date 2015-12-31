package java2typescript.jackson.module.writer;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;

import java2typescript.jackson.module.grammar.Module;

/**
 * Generates TypeScript type definitions for given module in internal module format
 */
public class InternalModuleFormatWriter extends ExternalModuleFormatWriter {

	@Override
	public void write(Module module, Writer writer) throws IOException {
		writer.write(format("export module %s {\n\n", module.getName()));
		preferences.increaseIndentation();
		writeModuleContent(module, writer);
		preferences.decreaseIndention();
		writer.write("}\n");
		writer.flush();
	}

}
