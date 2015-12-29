package java2typescript.jackson.module;

import com.google.common.collect.ImmutableMap;

import java2typescript.jackson.module.grammar.ArrayType;
import java2typescript.jackson.module.grammar.base.AbstractType;

public class Configuration {
	private ImmutableMap<String, AbstractType> customTypes = ImmutableMap.<String, AbstractType> builder().build();

	public ImmutableMap<String, AbstractType> getCustomTypes() {
		return customTypes;
	}

	public Configuration addType(Class<?> klass, AbstractType tsType) {
		addType(klass.getName(), tsType);
		addArrayType(klass, tsType);
		return this;
	}

	public void addArrayType(Class<?> klass, AbstractType tsType) {
		addType("[L" + klass.getName() + ";", new ArrayType(tsType));
	}

	public void addType(String className, AbstractType tsType) {
		customTypes = ImmutableMap.<String, AbstractType> builder() //
			.putAll(customTypes) //
			.put(className, tsType) //
			.build();
	}
}
