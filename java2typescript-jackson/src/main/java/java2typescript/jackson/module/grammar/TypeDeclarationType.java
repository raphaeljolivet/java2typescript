package java2typescript.jackson.module.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;

import java2typescript.jackson.module.grammar.base.AbstractNamedType;
import java2typescript.jackson.module.writer.WriterPreferences;

/**
 * {@link java2typescript.jackson.module.grammar.base.AbstractType} that has name, like classes/interfaces,
 * but instead of body the type has declared value.
 * <p>
 * For example
 * `type NumberOrString = number | string`
 * that can be expressed with
 * <p>
 * `new TypeDeclarationType("NumberOrString", "number | string")`
 *
 * @author Ats Uiboupin
 */
public class TypeDeclarationType extends AbstractNamedType {

	private String typeScriptType;

	public TypeDeclarationType(String className, String typeScriptType) {
		super(className);
		this.typeScriptType = typeScriptType;
	}

	@Override
	public void writeDefInternal(Writer writer, WriterPreferences preferences) throws IOException {
		// TODO separate writer for this type
		writer.write(format("type %s = %s;", name, typeScriptType));
	}

}
