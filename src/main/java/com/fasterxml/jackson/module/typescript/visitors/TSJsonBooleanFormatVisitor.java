package com.fasterxml.jackson.module.typescript.visitors;

import java.util.Set;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.module.typescript.grammar.BooleanType;
import com.fasterxml.jackson.module.typescript.grammar.StringType;


public class TSJsonBooleanFormatVisitor extends BaseTSJsonFormatVisitor<BooleanType>
		implements JsonBooleanFormatVisitor {

	public TSJsonBooleanFormatVisitor(BaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new BooleanType();
	}

	@Override
	public void format(JsonValueFormat format) {
	}

	@Override
	public void enumTypes(Set<String> enums) {
	}

}
