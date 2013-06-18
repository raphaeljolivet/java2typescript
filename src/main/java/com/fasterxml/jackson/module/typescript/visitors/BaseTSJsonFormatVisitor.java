package com.fasterxml.jackson.module.typescript.visitors;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWithSerializerProvider;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.module.typescript.grammar.AType;

/** Chained providers */
public class BaseTSJsonFormatVisitor<T extends AType> implements JsonFormatVisitorWithSerializerProvider {

	private final BaseTSJsonFormatVisitor<?> parentHolder;

	protected T type;

	private SerializerProvider serializerProvider;

	private Map<String, NamedType> namedTypes;

	private Map<JavaType, AType> computedTypes;

	public BaseTSJsonFormatVisitor(BaseTSJsonFormatVisitor parentHolder) {
		this.parentHolder = parentHolder;
	}

	@Override
	public SerializerProvider getProvider() {
		return (parentHolder == null) ? serializerProvider : parentHolder.getProvider();
	}

	@Override
	public void setProvider(SerializerProvider provider) {
		if (parentHolder != null) {
			parentHolder.setProvider(provider);
		} else {
			serializerProvider = provider;
		}
	}

	public Map<String, NamedType> getNamedTypes() {
		if (parentHolder == null) {
			if (namedTypes == null) {
				namedTypes = new TreeMap<String, NamedType>();
			}
			return namedTypes;
		} else {
			return parentHolder.getNamedTypes();
		}
	}

	public Map<JavaType, AType> getComputedTypes() {
		if (parentHolder == null) {
			if (computedTypes == null) {
				computedTypes = new HashMap<JavaType, AType>();
			}
			return computedTypes;
		} else {
			return parentHolder.getComputedTypes();
		}
	}

	public T getType() {
		return type;
	}
}
