package com.fasterxml.jackson.module.typescript.visitors;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor;
import com.fasterxml.jackson.module.typescript.grammar.NullType;

public class TSJsonNullFormatVisitor extends ABaseTSJsonFormatVisitor<NullType> implements JsonNullFormatVisitor {
	public TSJsonNullFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = NullType.getIntance();
	}
}
