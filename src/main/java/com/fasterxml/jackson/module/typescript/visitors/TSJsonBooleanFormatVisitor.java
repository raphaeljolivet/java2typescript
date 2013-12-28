package com.fasterxml.jackson.module.typescript.visitors;

import java.util.Set;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.module.typescript.grammar.BooleanType;

public class TSJsonBooleanFormatVisitor extends ABaseTSJsonFormatVisitor<BooleanType> implements
		JsonBooleanFormatVisitor {

	public TSJsonBooleanFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = BooleanType.getIntance();
	}

	@Override
	public void format(JsonValueFormat format) {
	}

	@Override
	public void enumTypes(Set<String> enums) {
	}

}
