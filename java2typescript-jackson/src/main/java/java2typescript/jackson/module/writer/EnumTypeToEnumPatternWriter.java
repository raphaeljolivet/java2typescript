package java2typescript.jackson.module.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import java2typescript.jackson.module.grammar.EnumType;
import java2typescript.jackson.module.grammar.base.AbstractNamedType;

/**
 * Alternative to writing Java enum type to TypeScript enum type. Usefult, if You have following goals:<br>
 * 1) JavaScript object containing field of "enum" must contain enum name instead of ordinal value. This is good for several reasons: a) easy to understand JSON content b) doesn't mess things up, if enum order changes in backend.<br>
 * 2) You'd still like to write TypeScript as if You were using real enums - type safety, etc.<br>
 * 3) You need to use "reflection" (instanceof) to detect if field is "enum"
 * @author Ats Uiboupin
 */
public class EnumTypeToEnumPatternWriter implements CustomAbstractTypeWriter {

	@Override
	public boolean accepts(AbstractNamedType type, WriterPreferences preferences) {
		return type instanceof EnumType;
	}

	@Override
	public void writeDef(AbstractNamedType type, Writer writer, WriterPreferences preferences) throws IOException {
		EnumType enumType = (EnumType) type;
		String enumTypeName = enumType.getName();
		writer.write(String.format("class %s extends EnumPatternBase {\n", enumTypeName));
		preferences.increaseIndentation();
		List<String> enumConstants = enumType.getValues();
		if(preferences.isSort()) {
			enumConstants = SortUtil.sort(enumConstants);
		}
		for (String value : enumConstants) {
			writer.write(String.format(preferences.getIndentation() + "static %s = new %s('%s');\n", value, enumTypeName, value));
		}
		writer.write(preferences.getIndentation() + "constructor(name:string){super(name);}\n");
		preferences.decreaseIndention();
		writer.write(preferences.getIndentation() + "}");
	}

}
