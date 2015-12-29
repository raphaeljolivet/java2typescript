/*******************************************************************************
 * Copyright 2014 Florian Benz
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
package java2typescript.jackson.module;

import java.lang.reflect.Field;
import java.util.List;

import java2typescript.jackson.module.grammar.AnyType;
import java2typescript.jackson.module.grammar.ArrayType;
import java2typescript.jackson.module.grammar.BooleanType;
import java2typescript.jackson.module.grammar.EnumType;
import java2typescript.jackson.module.grammar.Module;
import java2typescript.jackson.module.grammar.NumberType;
import java2typescript.jackson.module.grammar.StaticClassType;
import java2typescript.jackson.module.grammar.StringType;
import java2typescript.jackson.module.grammar.base.AbstractType;
import java2typescript.jackson.module.grammar.base.Value;
import java2typescript.jackson.module.visitors.TSJsonFormatVisitorWrapper;

import com.fasterxml.jackson.databind.type.SimpleType;

public class StaticFieldExporter {
	private static final String CLASS_NAME_EXTENSION = "Static";

	public static void export(Module module, List<Class<?>> classesToConvert)
			throws IllegalArgumentException {
		for (Class<?> clazz : classesToConvert) {
			if (clazz.isEnum()) {
				continue;
			}
			StaticClassType staticClass = new StaticClassType(clazz.getSimpleName()
					+ CLASS_NAME_EXTENSION);

			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				if (isPublicStaticFinal(field.getModifiers())) {
					Value value;
					try {
						value = constructValue(module, field.getType(), field.get(null));
					}
					catch (IllegalAccessException e) {
						throw new RuntimeException("Failed to get value of field " + field, e);
					}
					if (value != null) {
						staticClass.getStaticFields().put(field.getName(), value);
					}
				}
			}
			if (staticClass.getStaticFields().size() > 0) {
				module.getNamedTypes().put(staticClass.getName(), staticClass);
			}
		}
	}

	private static boolean isPublicStaticFinal(final int modifiers) {
		return java.lang.reflect.Modifier.isPublic(modifiers)
				&& java.lang.reflect.Modifier.isStatic(modifiers)
				&& java.lang.reflect.Modifier.isFinal(modifiers);
	}

	private static Value constructValue(Module module, Class<?> type, Object rawValue)
			throws IllegalArgumentException, IllegalAccessException {
		if (type == boolean.class) {
			return new Value(BooleanType.getInstance(), rawValue);
		} else if (type == int.class) {
			return new Value(NumberType.getInstance(), rawValue);
		} else if (type == double.class) {
			return new Value(NumberType.getInstance(), rawValue);
		} else if (type == String.class) {
			return new Value(StringType.getInstance(), "'" + (String) rawValue + "'");
		} else if (type.isEnum()) {
			final EnumType enumType = TSJsonFormatVisitorWrapper.parseEnumOrGetFromCache(module,
					SimpleType.construct(type));
			return new Value(enumType, enumType.getName() + "." + rawValue);
		} else if (type.isArray()) {
			final Class<?> componentType = type.getComponentType();
			final Object[] array;
			if (componentType == boolean.class) {
				boolean[] tmpArray = (boolean[]) rawValue;
				array = new Boolean[tmpArray.length];
				for (int i = 0; i < array.length; i++) {
					array[i] = Boolean.valueOf(tmpArray[i]);
				}
			} else if (componentType == int.class) {
				int[] tmpArray = (int[]) rawValue;
				array = new Integer[tmpArray.length];
				for (int i = 0; i < array.length; i++) {
					array[i] = Integer.valueOf(tmpArray[i]);
				}
			} else if (componentType == double.class) {
				double[] tmpArray = (double[]) rawValue;
				array = new Double[tmpArray.length];
				for (int i = 0; i < array.length; i++) {
					array[i] = Double.valueOf(tmpArray[i]);
				}
			} else {
				array = (Object[]) rawValue;
			}
			final StringBuilder arrayValues = new StringBuilder();
			arrayValues.append("[ ");
			for (int i = 0; i < array.length; i++) {
				arrayValues.append(constructValue(module, componentType, array[i]).getValue());
				if (i < array.length - 1) {
					arrayValues.append(", ");
				}
			}
			arrayValues.append(" ]");
			return new Value(new ArrayType(typeScriptTypeFromJavaType(module, componentType)),
					arrayValues.toString());
		}
		return null;
	}

	private static AbstractType typeScriptTypeFromJavaType(Module module, Class<?> type) {
		if (type == boolean.class) {
			return BooleanType.getInstance();
		} else if (type == int.class) {
			return NumberType.getInstance();
		} else if (type == double.class) {
			return NumberType.getInstance();
		} else if (type == String.class) {
			return StringType.getInstance();
		} else if (type.isEnum()) {
			return TSJsonFormatVisitorWrapper.parseEnumOrGetFromCache(module, SimpleType
					.construct(type));
		} else if (type.isArray()) {
			return new ArrayType(AnyType.getInstance());
		}
		throw new UnsupportedOperationException();
	}
}
