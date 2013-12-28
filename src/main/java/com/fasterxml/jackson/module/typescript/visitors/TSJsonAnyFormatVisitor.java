package com.fasterxml.jackson.module.typescript.visitors;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.fasterxml.jackson.module.typescript.grammar.AnyType;

public class TSJsonAnyFormatVisitor extends ABaseTSJsonFormatVisitor<AnyType> implements JsonAnyFormatVisitor {
	public TSJsonAnyFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = AnyType.getIntance();
	}
}
