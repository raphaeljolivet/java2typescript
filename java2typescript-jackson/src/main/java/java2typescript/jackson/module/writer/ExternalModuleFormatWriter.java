package java2typescript.jackson.module.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map.Entry;

import java2typescript.jackson.module.grammar.EnumType;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.grammar.base.AbstractNamedType;
import java2typescript.jackson.module.grammar.base.AbstractType;

/**
 * Generates TypeScript type definitions for given module in external module format
 */
public class ExternalModuleFormatWriter implements ModuleWriter {

	public WriterPreferences preferences = new WriterPreferences();

	@Override
	public void write(Module module, Writer writer) throws IOException {
		writeModuleContent(module, writer);
		writer.flush();
	}

	protected void writeModuleContent(Module module, Writer writer) throws IOException {
		Collection<AbstractNamedType> namedTypes = module.getNamedTypes().values();

		writeEnumPatternBaseClassIfNeeded(namedTypes, writer);

		if (preferences.isSort()) {
			namedTypes = SortUtil.sortByTypeName(namedTypes);
		}
		for (AbstractNamedType type : namedTypes) {
			writer.write(preferences.getIndentation() + "export ");
			type.writeDef(writer, preferences);
			writer.write("\n\n");
		}

		if (preferences.isSort()) {
			namedTypes = SortUtil.sortByTypeName(namedTypes);
		}
		Collection<Entry<String, AbstractType>> entrySet = module.getVars().entrySet();
		if (preferences.isSort()) {
			entrySet = SortUtil.sortEntriesByKey(entrySet);
		}
		for (Entry<String, AbstractType> entry : entrySet) {
			writer.write(preferences.getIndentation() + "export var " + entry.getKey() + ": ");
			entry.getValue().write(writer);
			writer.write(";\n");
		}
	}

	private void writeEnumPatternBaseClassIfNeeded(Collection<AbstractNamedType> namedTypes, Writer writer) throws IOException {
		if (preferences.isUseEnumPattern() && hasEnum(namedTypes)) {
			writeBaseEnum(writer);
			writer.write("\n");
		}
	}

	private boolean hasEnum(Collection<AbstractNamedType> namedTypes) {
		for (AbstractNamedType type : namedTypes) {
			if (type instanceof EnumType) {
				return true;
			}
		}
		return false;
	}

	private void writeBaseEnum(Writer writer) throws IOException {
		writer.write(preferences.getIndentation() + "/** base class for implementing enums with Typesafe Enum Pattern " +
			"(to be able to use enum names, instead of ordinal values, in a type-safe manner) */\n");
		writer.write(preferences.getIndentation() + "export class EnumPatternBase {\n");
		preferences.increaseIndentation();
		writer.write(preferences.getIndentation() + "constructor(public name: string){}\n");
		writer.write(preferences.getIndentation() + "toString(){ return this.name; }\n");
		preferences.decreaseIndention();
		writer.write(preferences.getIndentation() + "}\n");
	}

}
