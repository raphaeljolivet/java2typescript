package com.fasterxml.jackson.module.typescript.visitors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.module.typescript.grammar.AType;
import com.fasterxml.jackson.module.typescript.grammar.AnyType;
import com.fasterxml.jackson.module.typescript.grammar.ArrayType;
import com.fasterxml.jackson.module.typescript.grammar.BooleanType;
import com.fasterxml.jackson.module.typescript.grammar.NumberType;
import com.fasterxml.jackson.module.typescript.grammar.StringType;


public class TSJsonArrayFormatVisitor extends BaseTSJsonFormatVisitor<ArrayType> implements JsonArrayFormatVisitor {

	public TSJsonArrayFormatVisitor(BaseTSJsonFormatVisitor parentHolder) {
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

	private static AType typeScriptTypeFromJsonType(JsonFormatTypes type) {
		switch (type) {
		case ANY:
			return new AnyType();
		case BOOLEAN:
			return new BooleanType();
		case ARRAY:
			return new ArrayType(new AnyType());
		case INTEGER: //$FALL-THROUGH$
		case NUMBER:
			return new NumberType();
		case STRING:
			return new StringType();
		default:
			throw new UnsupportedOperationException();
		}
	}
}
