/*******************************************************************************
 * Copyright 2013 Raphael Jolivet
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.fasterxml.jackson.module.typescript.visitors;

import static com.fasterxml.jackson.module.typescript.visitors.TSJsonFormatVisitorWrapper.getTSTypeForHandler;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import com.fasterxml.jackson.module.typescript.grammar.MapType;


public class TSJsonMapFormatVisitor extends ABaseTSJsonFormatVisitor<MapType> implements JsonMapFormatVisitor {

	public TSJsonMapFormatVisitor(ABaseTSJsonFormatVisitor parentHolder) {
		super(parentHolder);
		type = new MapType();
	}

	@Override
	public void keyFormat(JsonFormatVisitable handler, JavaType keyType) throws JsonMappingException {
		type.setKeyType(getTSTypeForHandler(this, handler, keyType));
	}

	@Override
	public void valueFormat(JsonFormatVisitable handler, JavaType valueType) throws JsonMappingException {
		type.setValueType(getTSTypeForHandler(this, handler, valueType));
	}

}
