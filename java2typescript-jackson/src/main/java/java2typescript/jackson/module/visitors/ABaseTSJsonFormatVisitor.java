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
package java2typescript.jackson.module.visitors;

import java.util.HashMap;
import java.util.Map;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.grammar.base.AbstractType;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWithSerializerProvider;

import java2typescript.jackson.module.Configuration;

/**
 * Chained providers with a root parent holder keeping a map of already
 * compiuted types, and
 */
public abstract class ABaseTSJsonFormatVisitor<T extends AbstractType> implements
		JsonFormatVisitorWithSerializerProvider {

	private final ABaseTSJsonFormatVisitor<?> parentHolder;

	protected T type;

	private SerializerProvider serializerProvider;

	private Module module;

	private Map<JavaType, AbstractType> computedTypes;
	
	protected final Configuration conf;

	public ABaseTSJsonFormatVisitor(ABaseTSJsonFormatVisitor parentHolder, Configuration conf) {
		this.parentHolder = parentHolder;
		this.conf = conf;
	}

	public ABaseTSJsonFormatVisitor(Module module, Configuration conf) {
		this.parentHolder = null;
		this.module = module;
		this.conf = conf;
	}

	public SerializerProvider getProvider() {
		return (parentHolder == null) ? serializerProvider : parentHolder.getProvider();
	}

	public void setProvider(SerializerProvider provider) {
		if (parentHolder != null) {
			parentHolder.setProvider(provider);
		} else {
			serializerProvider = provider;
		}
	}

	public Module getModule() {
		if (parentHolder == null) {
			return module;
		} else {
			return parentHolder.getModule();
		}
	}

	public Map<JavaType, AbstractType> getComputedTypes() {
		if (parentHolder == null) {
			if (computedTypes == null) {
				computedTypes = new HashMap<JavaType, AbstractType>();
			}
			return computedTypes;
		} else {
			return parentHolder.getComputedTypes();
		}
	}

	public T getType() {
		return type;
	}
}
