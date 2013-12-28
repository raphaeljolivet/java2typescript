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
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.module.typescript.grammar.base.AbstractNamedType;

public class EnumType extends AbstractNamedType {

	private List<String> values = new ArrayList<String>();

	public EnumType(String className) {
		super(className);
	}

	@Override
	public void writeDef(Writer writer) throws IOException {
		writer.write(format("enum %s {\n", name));
		for (String value : values) {
			writer.write(format("    %s,\n", value));
		}
		writer.write("}");
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

}
