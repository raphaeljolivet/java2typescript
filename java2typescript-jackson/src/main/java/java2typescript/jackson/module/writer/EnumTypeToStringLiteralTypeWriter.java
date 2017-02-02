package java2typescript.jackson.module.writer;

import java2typescript.jackson.module.grammar.EnumType;
import java2typescript.jackson.module.grammar.base.AbstractNamedType;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;

/**
 * Another alternate way of converting Java enums. This writer will convert enums to what is known as a String Literal
 * Type in TypeScript (>=1.8). The advantage here is that the generated typescript definitions can be used with JSON
 * that has the string value of the corresponding java enum value, while still maintaining strong-typing.
 * More info: https://www.typescriptlang.org/docs/handbook/advanced-types.html#string-literal-types
 * @author Andy Perlitch
 */
public class EnumTypeToStringLiteralTypeWriter implements CustomAbstractTypeWriter {

	@Override
	public boolean accepts(AbstractNamedType type, WriterPreferences preferences) {
		return type instanceof EnumType;
	}

	@Override
	public void writeDef(AbstractNamedType type, Writer writer, WriterPreferences preferences) throws IOException {
		// metadata about the enum being converted
		EnumType enumType = (EnumType) type;
		String enumTypeName = enumType.getName();
		List<String> enumConstants = enumType.getValues();

		writer.write(format("type %s =\n", enumTypeName));
		preferences.increaseIndentation();
		if(preferences.isSort()) {
			enumConstants = SortUtil.sort(enumConstants);
		}
		Iterator<String> iter = enumConstants.iterator();
		boolean isFirst = true;
		while(iter.hasNext()) {
			String value = iter.next();
			writer.write(format("%s%s\"%s\"%s",
					preferences.getIndentation(),
					isFirst ? "" : "| ",
					value,
					iter.hasNext() ? "\n" : ";"
			));
			isFirst = false;
		}
		preferences.decreaseIndention();
	}
}
