package java2typescript.jackson.module.util;

import static com.google.common.collect.Lists.newArrayList;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java2typescript.jackson.module.Configuration;
import java2typescript.jackson.module.DefinitionGenerator;
import java2typescript.jackson.module.grammar.Module;

public class TestUtil {

	public static Module createTestModule(Configuration conf, Class<?>... classes) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		DefinitionGenerator generator = new DefinitionGenerator(mapper);
		return generator.generateTypeScript("modName", newArrayList(classes), conf);
	}
}
