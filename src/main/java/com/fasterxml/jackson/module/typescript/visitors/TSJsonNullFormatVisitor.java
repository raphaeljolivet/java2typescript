package com.fasterxml.jackson.module.typescript.visitors;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor;
import com.fasterxml.jackson.module.typescript.grammar.AnyType;


public class TSJsonNullFormatVisitor extends BaseTSJsonFormatVisitor<AnyType>
		implements JsonNullFormatVisitor {
	public TSJsonNullFormatVisitor(BaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new AnyType();
	}
}
