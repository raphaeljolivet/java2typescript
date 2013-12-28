/*******************************************************************************
 * Copyright 2013 Raphael Jolivet
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
package com.fasterxml.jackson.module.typescript.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractNamedType;
import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class ObjectType extends AbstractNamedType {

	private Map<String, AbstractType> fields = new LinkedHashMap<String, AbstractType>();

	private Map<String, FunctionType> methods = new LinkedHashMap<String, FunctionType>();

	public ObjectType(String className) {
		super(className);
	}

	@Override
	public void writeDef(Writer writer) throws IOException {
		writer.write(format("interface %s {\n", name));
		for (Entry<String, AbstractType> entry : fields.entrySet()) {
			writer.write(format("    %s: ", entry.getKey()));
			entry.getValue().write(writer);
			writer.write(";\n");
		}
		for (String methodName : methods.keySet()) {
			writer.write("    " + methodName);
			this.methods.get(methodName).write(writer);
			writer.write(";\n");
		}
		writer.write("}");
	}

	public Map<String, AbstractType> getFields() {
		return fields;
	}

	public void setFields(Map<String, AbstractType> fields) {
		this.fields = fields;
	}

	public Map<String, FunctionType> getMethods() {
		return methods;
	}
}
