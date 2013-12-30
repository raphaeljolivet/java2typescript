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

import static java2typescript.jaxrs.model.ParamType.BODY;
import static java2typescript.jaxrs.model.ParamType.FORM;
import static java2typescript.jaxrs.model.ParamType.PATH;
import static java2typescript.jaxrs.model.ParamType.QUERY;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java2typescript.jackson.module.DefinitionGenerator;
import java2typescript.jackson.module.grammar.FunctionType;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.grammar.ObjectType;
import java2typescript.jackson.module.grammar.base.AbstractType;
import java2typescript.jaxrs.model.HttpMethod;
import java2typescript.jaxrs.model.Param;
import java2typescript.jaxrs.model.RestMethod;
import java2typescript.jaxrs.model.RestService;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Generates a {@link RestService} description out of a service class /
 * interface
 */
public class ServiceDescriptorGenerator {

	private final Class<?> clazz;

	private ObjectMapper mapper;

	public ServiceDescriptorGenerator(Class<?> clazz) {
		this(clazz, new ObjectMapper());
	}

	public ServiceDescriptorGenerator(Class<?> clazz, ObjectMapper mapper) {
		this.clazz = clazz;
		this.mapper = mapper;
		addDummyMappingForJAXRSClasses();
	}

	private class DummySerializer extends JsonSerializer<Object> {
		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
				JsonProcessingException {
			// No implementation here
		}
	}

	/** Those classes will be transformed as "any" */
	private void addDummyMappingForJAXRSClasses() {
		SimpleModule module = new SimpleModule("dummy jax-rs mappings");
		module.addSerializer(Response.class, new DummySerializer());
		module.addSerializer(UriInfo.class, new DummySerializer());
		module.addSerializer(Request.class, new DummySerializer());
		mapper.registerModule(module);
	}

	/**
	 * Main method to generate a REST Service desciptor out of JAX-RS service
	 * class
	 */
	public RestService generateRestService() {

		RestService module = new RestService();
		module.setName(clazz.getSimpleName());

		Path pathAnnotation = clazz.getAnnotation(Path.class);

		if (pathAnnotation == null) {
			throw new RuntimeException("No @Path on class " + clazz.getName());
		}

		module.setPath(pathAnnotation.value());

		for (Method method : clazz.getDeclaredMethods()) {
			if (Modifier.isPublic(method.getModifiers())) {
				RestMethod restMethod = generateMethod(method);
				module.getMethods().put(restMethod.getName(), restMethod);
			}
		}

		return module;
	}

	/**
	 * Generates a typescript definition of the REST service together with all
	 * required named types (classes and enums)
	 */
	public Module generateTypeScript(String moduleName) throws JsonMappingException {

		// Generates Typescript module out of service class definition
		DefinitionGenerator defGen = new DefinitionGenerator(mapper);
		Module module = defGen.generateTypeScript(moduleName, Collections.singleton(clazz));

		// Generate REST service descriptor
		RestService restModule = generateRestService();

		// Update methods with parameter names
		ObjectType classDef = (ObjectType) module.getNamedTypes().get(clazz.getSimpleName());
		decorateParamNames(restModule, classDef);

		return module;
	}

	private RestMethod generateMethod(Method method) {

		RestMethod restMethod = new RestMethod();
		Path pathAnnotation = method.getAnnotation(Path.class);

		restMethod.setPath(pathAnnotation == null ? "" : pathAnnotation.value());

		restMethod.setName(method.getName());

		if (method.getAnnotation(GET.class) != null) {
			restMethod.setHttpMethod(HttpMethod.GET);
		}
		if (method.getAnnotation(POST.class) != null) {
			restMethod.setHttpMethod(HttpMethod.POST);
		}
		if (method.getAnnotation(PUT.class) != null) {
			restMethod.setHttpMethod(HttpMethod.PUT);
		}
		if (method.getAnnotation(DELETE.class) != null) {
			restMethod.setHttpMethod(HttpMethod.DELETE);
		}

		if (restMethod.getHttpMethod() == null) {
			throw new RuntimeException("No Http method defined for method : " + method.getName());
		}

		restMethod.setParams(generateParams(method));

		return restMethod;
	}

	private List<Param> generateParams(Method method) {
		List<Param> params = new ArrayList<Param>();
		for (Annotation[] annotations : method.getParameterAnnotations()) {
			Param param = new Param();
			param.setType(BODY); // By default, in case of no annotation
			param.setName("body");
			for (Annotation annotation : annotations) {
				fillParam(annotation, param);
			}
			params.add(param);
		}
		return params;
	}

	private void fillParam(Annotation annot, Param param) {
		if (annot instanceof PathParam) {
			param.setType(PATH);
			param.setName(((PathParam) annot).value());
		} else if (annot instanceof QueryParam) {
			param.setType(QUERY);
			param.setName(((QueryParam) annot).value());
		} else if (annot instanceof FormParam) {
			param.setType(FORM);
			param.setName(((FormParam) annot).value());
		} else if (annot instanceof Context) {
			param.setContext(true);
		}
	}

	/** Use collected annotation in order to ad param names to service methods */
	private void decorateParamNames(RestService module, ObjectType classDef) {

		// Loop on methods of the service
		for (RestMethod restMethod : module.getMethods().values()) {
			FunctionType function = classDef.getMethods().get(restMethod.getName());

			// Copy ordered list of param types
			List<AbstractType> types = new ArrayList<AbstractType>();
			types.addAll(function.getParameters().values());

			function.getParameters().clear();

			int i = 0;
			for (Param param : restMethod.getParams()) {

				// Skip @Context parameters
				if (!param.isContext()) {
					function.getParameters().put(param.getName(), types.get(i));
				}
				i++;
			}
		}
	}

}
