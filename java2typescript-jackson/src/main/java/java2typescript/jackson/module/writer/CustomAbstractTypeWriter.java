package java2typescript.jackson.module.writer;

import java.io.IOException;
import java.io.Writer;

import java2typescript.jackson.module.grammar.base.AbstractNamedType;

/**
 * Class implementing this interface can be used to customize how Java type is written to TypeScript
 * @author Ats Uiboupin
 */
public interface CustomAbstractTypeWriter {
	/**
	 * @return true if this class should handle writing the type to {@link Writer}
	 */
	boolean accepts(AbstractNamedType type, WriterPreferences preferences);

	void writeDef(AbstractNamedType type, Writer writer, WriterPreferences preferences) throws IOException;
}
