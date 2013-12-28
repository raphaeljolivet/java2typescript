package com.fasterxml.jackson.module.typescript.visitors;

import java.util.Set;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.module.typescript.grammar.NumberType;

public class TSJsonNumberFormatVisitor extends ABaseTSJsonFormatVisitor<NumberType> implements JsonNumberFormatVisitor,
		JsonIntegerFormatVisitor {

	public TSJsonNumberFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = NumberType.getIntance();
	}

	@Override
	public void format(JsonValueFormat format) {
	}

	@Override
	public void enumTypes(Set<String> enums) {
	}

	@Override
	public void numberType(com.fasterxml.jackson.core.JsonParser.NumberType type) {

	}

}
