package com.fasterxml.jackson.module.typescript.visitors;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.module.typescript.grammar.EnumType;
import com.fasterxml.jackson.module.typescript.grammar.Module;
import com.fasterxml.jackson.module.typescript.grammar.base.AbstractNamedType;
import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class TSJsonFormatVisitorWrapper extends ABaseTSJsonFormatVisitor implements JsonFormatVisitorWrapper {

	public TSJsonFormatVisitorWrapper(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
	}

	public TSJsonFormatVisitorWrapper(Module module) {
		super(module);
	}

	private <T extends ABaseTSJsonFormatVisitor<?>> T setTypeAndReturn(T actualVisitor) {
		type = actualVisitor.getType();
		return actualVisitor;
	}

	/** Visit recursively the type, or return a cached response */
	public static AbstractType getTSTypeForHandler(ABaseTSJsonFormatVisitor<?> baseVisitor,
			JsonFormatVisitable handler, JavaType typeHint) throws JsonMappingException {

		AbstractType computedType = baseVisitor.getComputedTypes().get(typeHint);

		if (computedType != null) {
			return computedType;
		}

		TSJsonFormatVisitorWrapper visitor = new TSJsonFormatVisitorWrapper(baseVisitor);
		handler.acceptJsonFormatVisitor(visitor, typeHint);
		baseVisitor.getComputedTypes().put(typeHint, visitor.getType());
		return visitor.getType();
	}

	/** Either Java simple name or @JsonTypeName annotation */
	private String getName(JavaType type) {
		JsonTypeName typeName = type.getRawClass().getAnnotation(JsonTypeName.class);
		if (typeName != null) {
			return typeName.value();
		} else {
			return type.getRawClass().getSimpleName();
		}
	}

	private TSJsonObjectFormatVisitor useNamedClassOrParse(JavaType javaType) {

		String name = getName(javaType);

		AbstractNamedType namedType = getModule().getNamedTypes().get(name);

		if (namedType == null) {
			TSJsonObjectFormatVisitor visitor = new TSJsonObjectFormatVisitor(this, name, javaType.getRawClass());
			type = visitor.getType();
			getModule().getNamedTypes().put(visitor.getType().getName(), visitor.getType());
			return visitor;
		} else {
			type = namedType;
			return null;
		}
	}

	private EnumType parseEnumOrGetFromCache(JavaType javaType) {
		String name = getName(javaType);
		AbstractType namedType = getModule().getNamedTypes().get(name);
		if (namedType == null) {
			EnumType enumType = new EnumType(name);
			for (Object val : javaType.getRawClass().getEnumConstants()) {
				enumType.getValues().add(val.toString());
			}
			getModule().getNamedTypes().put(name, enumType);
			return enumType;
		} else {
			return (EnumType) namedType;
		}
	}

	@Override
	public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
		return useNamedClassOrParse(type);
	}

	@Override
	public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
		return setTypeAndReturn(new TSJsonArrayFormatVisitor(this));
	}

	@Override
	public JsonStringFormatVisitor expectStringFormat(JavaType jType) throws JsonMappingException {
		if (jType.getRawClass().isEnum()) {
			type = parseEnumOrGetFromCache(jType);
			return null;
		} else {
			return setTypeAndReturn(new TSJsonStringFormatVisitor(this));
		}
	}

	@Override
	public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
		return setTypeAndReturn(new TSJsonNumberFormatVisitor(this));
	}

	@Override
	public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type) throws JsonMappingException {
		return setTypeAndReturn(new TSJsonNumberFormatVisitor(this));
	}

	@Override
	public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
		return setTypeAndReturn(new TSJsonBooleanFormatVisitor(this));
	}

	@Override
	public JsonNullFormatVisitor expectNullFormat(JavaType type) throws JsonMappingException {
		return setTypeAndReturn(new TSJsonNullFormatVisitor(this));
	}

	@Override
	public JsonAnyFormatVisitor expectAnyFormat(JavaType type) throws JsonMappingException {
		return setTypeAndReturn(new TSJsonAnyFormatVisitor(this));
	}

	@Override
	public JsonMapFormatVisitor expectMapFormat(JavaType type) throws JsonMappingException {
		return setTypeAndReturn(new TSJsonMapFormatVisitor(this));
	}

}
