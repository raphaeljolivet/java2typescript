package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;

/**
 * @author Ats Uiboupin
 */
public class WriterPreferencesTest {

	static enum Enum {
		VAL1, VAL2
	}

	static class Dummy {
		public String _String;
	}

	@Test
	public void enumToEnumPattern() throws IOException {
		// Arrange
		ExternalModuleFormatWriter mWriter = new ExternalModuleFormatWriter();
		mWriter.preferences.useEnumPattern();

		Module module = TestUtil.createTestModule(Enum.class);
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

		Module module = TestUtil.createTestModule(Dummy.class);
		Writer out = new StringWriter();

		// Act
		mWriter.write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}
}
