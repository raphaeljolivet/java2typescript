package java2typescript.jackson.module.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import java2typescript.jackson.module.grammar.base.AbstractNamedType;

public class WriterPreferences {

	private List<CustomAbstractTypeWriter> customWriters = new ArrayList<CustomAbstractTypeWriter>();

	private boolean useEnumPattern;

	public void useEnumPattern() {
		addWriter(new EnumTypeToEnumPatternWriter());
		useEnumPattern = true;
	}

	public boolean isUseEnumPattern() {
		return useEnumPattern;
	}

	public void addWriter(CustomAbstractTypeWriter writer) {
		this.customWriters.add(writer);
	}

	public List<CustomAbstractTypeWriter> getCustomWriters() {
		return customWriters;
	}

	public boolean hasCustomWriter(AbstractNamedType type) {
		return getCustomWriter(type) != null;
	}

	public void writeDef(AbstractNamedType type, Writer writer) throws IOException {
		getCustomWriter(type).writeDef(type, writer, this);
	}

	public CustomAbstractTypeWriter getCustomWriter(AbstractNamedType type) {
		for (CustomAbstractTypeWriter writer : customWriters) {
			if (writer.accepts(type, this)) {
				return writer;
			}
		}
		return null;
	}

}
