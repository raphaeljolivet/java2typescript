package com.fasterxml.jackson.module.typescript.visitors;

import java.text.NumberFormat;
import java.util.Set;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.module.typescript.grammar.NumberType;
import com.fasterxml.jackson.module.typescript.grammar.StringType;


public class TSJsonNumberFormatVisitor extends BaseTSJsonFormatVisitor<NumberType>
		implements JsonNumberFormatVisitor, JsonIntegerFormatVisitor {

	
	public TSJsonNumberFormatVisitor(BaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new NumberType();
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
