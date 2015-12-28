
/*******************************************************************************
 * Copyright 2013 Ats Uiboupin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package java2typescript.jackson.module;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.grammar.base.AbstractPrimitiveType;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;

public class CustomTypeDefinitionGeneratorTest {

	class TestClass {
		public CustomDate customDate;
		public CustomDate[] customDateArray;
	}

	public class CustomDate {
	}

	public static class DateType extends AbstractPrimitiveType {

		static private DateType instance = new DateType();

		static public DateType getInstance() {
			return instance;
		}

		private DateType() {
			super("Date");
		}
	}

	@Test
	public void classWithCustomTypeFields() throws IOException {
		// Arrange
		Configuration conf = new Configuration().addType(CustomDate.class, DateType.getInstance());
		Module module = TestUtil.createTestModule(conf, TestClass.class);
		Writer out = new StringWriter();

		// Act
		new ExternalModuleFormatWriter().write(module, out);
		out.close();
		System.out.println(out);

		// Assert
		ExpectedOutputChecker.checkOutputFromFile(out);
	}
}
