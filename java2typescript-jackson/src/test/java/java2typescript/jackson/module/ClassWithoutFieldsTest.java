package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;

public class ClassWithoutFieldsTest {

	static class ClassWithoutFields {
	}

	@Test
	public void classWithoutFields() throws IOException {
		// Arrange
		Module module = TestUtil.createTestModule(null, ClassWithoutFields.class);
		Writer out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

	@Test
	public void referencesClassWithoutFields() throws IOException {
		// Arrange
		@SuppressWarnings("unused")
		class RererencesClassWithoutFields {
			public ClassWithoutFields classWithoutFields;
			public Object javaLangObject;
		}
		Module module = TestUtil.createTestModule(null, RererencesClassWithoutFields.class);
		Writer out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

	@Test
	public void classWithOnlyMethod() throws IOException {
		// Arrange
		class ClassWithOnlyMethod {
			@SuppressWarnings("unused")
			public void onlyMethod() {
			}
		}
		Module module = TestUtil.createTestModule(null, ClassWithOnlyMethod.class);
		Writer out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}
}
