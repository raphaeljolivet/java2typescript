package com.fasterxml.jackson.module.typescript;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.typescript.grammar.Module;

public class DefinitionGeneratorTest {

	@JsonTypeName("ChangedEnumName")
	static enum Enum {
		VAL1, VAL2, VAL3
	}

	class GenericClass<T> {
		public T someField;
	}

	class TestClass {
		public String _String;
		public boolean _boolean;
		public Boolean _Boolean;
		public int _int;
		public float _float;
		public String[] stringArray;
		public Map<Integer, Boolean> map;
		public TestClass recursive;
		public TestClass[] recursiveArray;
		public ArrayList<String> stringArrayList;
		public Collection<Boolean> booleanCollection;
		public Enum _enum;

		public String aMethod(boolean recParam, String param2) {
			return "toto";
		}
	}

	public class StringClass extends GenericClass<String> {

	}

	@Test
	public void testTypeScriptDefinition() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		DefinitionGenerator generator = new DefinitionGenerator(mapper);
		Writer out = new OutputStreamWriter(System.out);

		Module module = generator.generateTypeScript(//
				"modName", //
				newArrayList(//
						TestClass.class, //
						StringClass.class));

		module.write(out);

		out.close();
	}
}
