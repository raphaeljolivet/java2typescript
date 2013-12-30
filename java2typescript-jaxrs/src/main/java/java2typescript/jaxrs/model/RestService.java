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
package java2typescript.jaxrs.model;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Root descrpition of a service */
public class RestService {

	private String name;
	private String path;
	private final Map<String, RestMethod> methods = new HashMap<String, RestMethod>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, RestMethod> getMethods() {
		return methods;
	}

	/**
	 * Dump a JSON representation of the REST services
	 */
	public void toJSON(Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(INDENT_OUTPUT, true);

		mapper.writeValue(writer, copyWithoutContextParams(this));
	}

	static private RestService copyWithoutContextParams(RestService restService) {
		Kryo kryo = new Kryo();
		RestService res = kryo.copy(restService);
		for (RestMethod method : res.getMethods().values()) {
			Iterator<Param> paramsIt = method.getParams().iterator();
			while (paramsIt.hasNext()) {
				Param param = paramsIt.next();
				if (param.isContext()) {
					paramsIt.remove();
				}
			}
		}
		return res;
	}
}
