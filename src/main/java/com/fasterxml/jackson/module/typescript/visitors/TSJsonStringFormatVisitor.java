package com.fasterxml.jackson.module.typescript.visitors;

import java.util.Set;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.module.typescript.grammar.StringType;


public class TSJsonStringFormatVisitor extends BaseTSJsonFormatVisitor<StringType>
		implements JsonStringFormatVisitor {

	
	public TSJsonStringFormatVisitor(BaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new StringType();
	}

	@Override
	public void format(JsonValueFormat format) {
	}

	@Override
	public void enumTypes(Set<String> enums) {
	}

}
