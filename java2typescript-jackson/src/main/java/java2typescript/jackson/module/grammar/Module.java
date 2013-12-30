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
package java2typescript.jackson.module.grammar;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java2typescript.jackson.module.grammar.base.AbstractNamedType;


public class Module {

	private String name;

	private Map<String, AbstractNamedType> namedTypes = new HashMap<String, AbstractNamedType>();

	public Module() {
	}

	public Module(String name) {
		this.name = name;
	}

	public Map<String, AbstractNamedType> getNamedTypes() {
		return namedTypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void write(Writer writer) throws IOException {
		if (name != null) {
			writer.write(format("module %s {\n\n", name));
		}

		for (Object typeObj : namedTypes.values()) {
			if (name != null) {
				writer.write("export ");
			}
			((AbstractNamedType) typeObj).writeDef(writer);
			writer.write("\n\n");
		}

		if (name != null) {
			writer.write("}\n");
		}
		writer.flush();
	}

}
