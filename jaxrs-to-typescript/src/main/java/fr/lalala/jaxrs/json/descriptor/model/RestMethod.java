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
package fr.lalala.jaxrs.json.descriptor.model;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RestMethod {

	private String name;
	private String path;
	private LinkedHashMap<String, Param> params;
	private HttpMethod httpMethod;

	public String getPath() {
		return path;
	}

	@JsonIgnore
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/** Ordered mpa of params */
	public LinkedHashMap<String, Param> getParams() {
		return params;
	}

	public void setParams(LinkedHashMap<String, Param> generateParams) {
		params = generateParams;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

}
