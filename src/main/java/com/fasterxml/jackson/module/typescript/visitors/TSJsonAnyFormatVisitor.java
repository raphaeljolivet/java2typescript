package com.fasterxml.jackson.module.typescript.visitors;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.fasterxml.jackson.module.typescript.grammar.NullType;


public class TSJsonAnyFormatVisitor extends BaseTSJsonFormatVisitor<NullType>
		implements JsonAnyFormatVisitor {
	public TSJsonAnyFormatVisitor(BaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new NullType();
	}

}
