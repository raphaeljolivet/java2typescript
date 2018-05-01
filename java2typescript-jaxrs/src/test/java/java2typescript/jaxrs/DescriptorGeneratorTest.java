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
package java2typescript.jaxrs;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java2typescript.jackson.module.grammar.Module;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.junit.Before;
import org.junit.Test;

import com.example.rs.PeopleRestService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class DescriptorGeneratorTest {

	private Writer out;

	static class MyObject {
		public String field;
	}

	@Path("/")
	class ExampleService {

		@Path("/{id}")
		@POST
		public String aPostMethod(//
				@QueryParam("q1") String queryParam, //
				@PathParam("id") String id, //
				@FormParam("formParam") Integer formParam, //
				String postPayload){
			return "test";
		}

		@Path("/{id}")
		@GET
		public void aGetMethod(//
				@QueryParam("q1") String queryParam, //
				@PathParam("id") String id, //
				@FormParam("formParam") Integer formParam, //
				MyObject postPayload){

		}

		@Path("/random")
		@GET
		public int getRandom() {
			return 4;
		}

		@Path("/multi")
		@GET
		public int getMultiWordGetter() {
			return 3;
		}

	}

	@Before
	public void setUp() {
		out = new OutputStreamWriter(System.out);
	}

	@Test
	public void testJSGenerate() throws JsonGenerationException, JsonMappingException, IOException {
		ServiceDescriptorGenerator descGen = new ServiceDescriptorGenerator(
				Collections.singletonList(PeopleRestService.class));
		descGen.generateJavascript("moduleName", out);
	}

	@Test
	public void testTypescriptGenerate() throws JsonGenerationException, JsonMappingException, IOException {

		ServiceDescriptorGenerator descGen = new ServiceDescriptorGenerator(
				Collections.singletonList(PeopleRestService.class));

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("custom-mapping");

		mapper.registerModule(module);

		Module tsModule = descGen.generateTypeScript("modName");
		tsModule.write(out);
	}

	@Test
	public void testTypescriptGenerateWithExample() throws JsonGenerationException, JsonMappingException, IOException {

		ServiceDescriptorGenerator descGen = new ServiceDescriptorGenerator(
				Collections.singletonList(ExampleService.class));

		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("custom-mapping");

		mapper.registerModule(module);

		Module tsModule = descGen.generateTypeScript("modName");
		tsModule.write(out);
	}
}
