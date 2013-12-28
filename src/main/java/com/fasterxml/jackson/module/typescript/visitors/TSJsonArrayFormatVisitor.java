package com.fasterxml.jackson.module.typescript.visitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.module.typescript.grammar.AnyType;
import com.fasterxml.jackson.module.typescript.grammar.ArrayType;
import com.fasterxml.jackson.module.typescript.grammar.BooleanType;
import com.fasterxml.jackson.module.typescript.grammar.NumberType;
import com.fasterxml.jackson.module.typescript.grammar.StringType;
import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class TSJsonArrayFormatVisitor extends ABaseTSJsonFormatVisitor<ArrayType> implements JsonArrayFormatVisitor {

	public TSJsonArrayFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new ArrayType();
	}

	@Override
	public void itemsFormat(JsonFormatVisitable handler, JavaType elementType) throws JsonMappingException {
		TSJsonFormatVisitorWrapper visitorWrapper = new TSJsonFormatVisitorWrapper(this);
		handler.acceptJsonFormatVisitor(visitorWrapper, elementType);
		type.setItemType(visitorWrapper.getType());
	}

	@Override
	public void itemsFormat(JsonFormatTypes format) throws JsonMappingException {
		type.setItemType(typeScriptTypeFromJsonType(format));
	}

	private static AbstractType typeScriptTypeFromJsonType(JsonFormatTypes type) {
		switch (type) {
		case ANY:
			return AnyType.getIntance();
		case BOOLEAN:
			return BooleanType.getIntance();
		case ARRAY:
			return new ArrayType(AnyType.getIntance());
		case INTEGER: //$FALL-THROUGH$
		case NUMBER:
			return NumberType.getIntance();
		case STRING:
			return StringType.getIntance();
		default:
			throw new UnsupportedOperationException();
		}
	}
}
