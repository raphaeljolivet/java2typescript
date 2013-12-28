package com.fasterxml.jackson.module.typescript.visitors;

import static com.fasterxml.jackson.module.typescript.visitors.TSJsonFormatVisitorWrapper.getTSTypeForHandler;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import com.fasterxml.jackson.module.typescript.grammar.MapType;


public class TSJsonMapFormatVisitor extends ABaseTSJsonFormatVisitor<MapType> implements JsonMapFormatVisitor {

	public TSJsonMapFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new MapType();
	}

	@Override
	public void keyFormat(JsonFormatVisitable handler, JavaType keyType) throws JsonMappingException {
		type.setKeyType(getTSTypeForHandler(this, handler, keyType));
	}

	@Override
	public void valueFormat(JsonFormatVisitable handler, JavaType valueType) throws JsonMappingException {
		type.setValueType(getTSTypeForHandler(this, handler, valueType));
	}

}
