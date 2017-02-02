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

	static enum EnumOneValue {
		VAL1
	}

	static class Dummy {
		public String _String;
	}
	
	static class Constants {
		// constants in non-alfabetic order
		public static final String MY_CONSTANT_STRING = "stringValue";
		public static final boolean MY_CONSTANT_BOOLEAN = true;
	}
	
	enum E{B,C,A}


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
	public void enumToStringLiteralType() throws IOException {
		// Arrange
		ExternalModuleFormatWriter mWriter = new ExternalModuleFormatWriter();
		mWriter.preferences.useStringLiteralTypeForEnums();

		Module module = TestUtil.createTestModule(null, Enum.class, EnumOneValue.class);
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

		Configuration conf = null; // default conf
		Module module = TestUtil.createTestModule(conf, TestClass.class);
		List<Class<?>> toConvert = new ArrayList<Class<?>>();
		toConvert.add(Constants.class);
		new StaticFieldExporter(module, conf).export(toConvert);
		Writer out = new StringWriter();

		// Act
		writer.write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}

	@Test
	public void sortOutputTypesAndVars() throws IOException {
		// Arrange
		ExternalModuleFormatWriter writer = new ExternalModuleFormatWriter();
		Configuration conf = null; // default conf
		writer.preferences.sort();

		@SuppressWarnings("unused")
		class F{
			public String B;
			public String C;
			public String A;
			public void b() {};
			public void c() {};
			public void a() {};
		}
		class Z{}
		class A{}
		class D{}

		Class<?>[] classes = new Class[]{TestClass.class, D.class, Z.class, A.class, E.class, F.class};

		Module module = TestUtil.createTestModule(conf, classes);

		List<Class<?>> toConvert = new ArrayList<Class<?>>();
		toConvert.add(Constants.class);

		module.getVars().put("z", module.getNamedTypes().get(Z.class.getSimpleName()));
		module.getVars().put("a", module.getNamedTypes().get(A.class.getSimpleName()));

		new StaticFieldExporter(module, conf).export(toConvert);
		Writer out = new StringWriter();

		// Act
		writer.write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFileEquals(out);
	}
}
