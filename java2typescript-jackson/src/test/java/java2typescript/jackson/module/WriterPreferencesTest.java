package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;
import java2typescript.jackson.module.writer.InternalModuleFormatWriter;

/**
 * @author Ats Uiboupin
 */
public class WriterPreferencesTest {

	static class TestClass {
		public String someField;
		public Enum someEnum;
	}

	static enum Enum {
		VAL1, VAL2
	}

	static class Dummy {
		public String _String;
	}
	
	static class Constants {
		public static final boolean MY_CONSTANT_BOOLEAN = true;
	}
	

	@Test
	public void enumToEnumPattern() throws IOException {
		// Arrange
		ExternalModuleFormatWriter mWriter = new ExternalModuleFormatWriter();
		mWriter.preferences.useEnumPattern();

		Module module = TestUtil.createTestModule(null, Enum.class);
		Writer out = new StringWriter();

		// Act
		mWriter.write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

	@Test
	public void enumPatternBaseNotAddedWhenNotNeeded() throws IOException {
		// Arrange
		ExternalModuleFormatWriter mWriter = new ExternalModuleFormatWriter();
		mWriter.preferences.useEnumPattern(); // should be ignored when no enums found

		Module module = TestUtil.createTestModule(null, Dummy.class);
		Writer out = new StringWriter();

		// Act
		mWriter.write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

	@Test
	public void indentWithTabs() throws IOException {
		// Arrange
		InternalModuleFormatWriter writer = new InternalModuleFormatWriter();
		writer.preferences.setIndentationStep("\t"); // custom indentation
		writer.preferences.useEnumPattern();

		Module module = TestUtil.createTestModule(null, TestClass.class);
		List<Class<?>> toConvert = new ArrayList<Class<?>>();
		toConvert.add(Constants.class);
		StaticFieldExporter.export(module, toConvert);
		Writer out = new StringWriter();

		// Act
		writer.write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}
}
