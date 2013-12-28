package com.fasterxml.jackson.module.typescript.visitors;

import java.util.Set;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.module.typescript.grammar.StringType;

public class TSJsonStringFormatVisitor extends ABaseTSJsonFormatVisitor<StringType> implements JsonStringFormatVisitor {

	public TSJsonStringFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = StringType.getIntance();
	}

	@Override
	public void format(JsonValueFormat format) {
	}

	@Override
	public void enumTypes(Set<String> enums) {
	}

}
