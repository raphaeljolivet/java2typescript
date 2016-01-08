
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

import java.beans.Transient;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.util.ExpectedOutputChecker;
import java2typescript.jackson.module.util.TestUtil;
import java2typescript.jackson.module.writer.ExternalModuleFormatWriter;

public class ExcludedMethodsTest {

	static class TestClass {
		public String field;

		// only public methods are included in output
		String nonPublicMethodsAreIgnored() {
			return null;
		}

		// Java Bean property getters are excluded even if field doesn't exist with exact name (field is generated instead)
		public String getBeanProperty() {
			return null;
		}

		// Java Bean property setters are excluded even if field doesn't exist with exact name  (field is generated instead)
		public void setBeanProperty(String beanProperty) {
		}

		// methods annotated with @java.beans.Transient are excluded from output
		@Transient
		public String transientMethodsAreIgnoredByDefault() {
			return null;
		}

		// this method is added to the blacklist using conf.addIngoredMethod("blacklistedMethod")
		public String blacklistedMethod() {
			return null;
		}

		// this method is added to the blacklist using conf.addIngoredMethod("blacklistedStaticMethod")
		public static String blacklistedStaticMethod() {
			return null;
		}

		// --------------- END EXCLUDED METHODS

		public String instanceMetohd() {
			return null;
		}

		public static String staticMethod() {
			return null;
		}
	}

	@Test
	public void testExcludedMethods() throws IOException {
		// Arrange
		Configuration conf = new Configuration();
		conf.addIngoredMethod("blacklistedMethod");
		conf.addIngoredMethod("blacklistedStaticMethod");
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
