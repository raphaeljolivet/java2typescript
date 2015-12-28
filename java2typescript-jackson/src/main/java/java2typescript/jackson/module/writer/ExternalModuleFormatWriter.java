package java2typescript.jackson.module.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map.Entry;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.grammar.base.AbstractNamedType;
import java2typescript.jackson.module.grammar.base.AbstractType;

/**
 * Generates TypeScript type definitions for given module in external module format
 */
public class ExternalModuleFormatWriter implements ModuleWriter {

	@Override
	public void write(Module module, Writer writer) throws IOException {
		writeModuleContent(module, writer);
		writer.flush();
	}

	protected void writeModuleContent(Module module, Writer writer) throws IOException {
		Collection<AbstractNamedType> namedTypes = module.getNamedTypes().values();

		for (AbstractNamedType type : namedTypes) {
			writer.write("export ");
			type.writeDef(writer);
			writer.write("\n\n");
		}

		for (Entry<String, AbstractType> entry : module.getVars().entrySet()) {
			writer.write("export var " + entry.getKey() + ": ");
			entry.getValue().write(writer);
			writer.write(";\n");
		}
	}

}
