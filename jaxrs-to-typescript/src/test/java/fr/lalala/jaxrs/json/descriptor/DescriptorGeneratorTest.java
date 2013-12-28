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
package fr.lalala.jaxrs.json.descriptor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.typescript.grammar.Module;

import fr.lalala.jaxrs.json.descriptor.model.RestService;

public class DescriptorGeneratorTest {

	private Writer out;

	static class MyObject {
		public String field;
	}

	@Path("/")
	static private interface ExampleService {

		@Path("/{id}")
		@POST
		public String aPostMethod(//
				@QueryParam("q1") String queryParam, //
				@PathParam("id") String id, //
				@FormParam("formParam") Integer formParam, //
				String postPayload);

		@Path("/{id}")
		@GET
		public void aGetMethod(//
				@QueryParam("q1") String queryParam, //
				@PathParam("id") String id, //
				@FormParam("formParam") Integer formParam, //
				MyObject postPayload);

	}

	@Before
	public void setUp() {
		out = new OutputStreamWriter(System.out);
	}

	@Test
	public void testJSONGenerate() throws JsonGenerationException, JsonMappingException, IOException {
		DescriptorGenerator descGen = new DescriptorGenerator(ExampleService.class);
		RestService restService = descGen.generateRestService();
		restService.toJSON(out);
	}

	@Test
	public void testTypescriptGenerate() throws JsonGenerationException, JsonMappingException, IOException {

		DescriptorGenerator descGen = new DescriptorGenerator(ExampleService.class);

		Module tsModule = descGen.generateTypeScript("modName", new ObjectMapper());
		tsModule.write(out);
	}
}
