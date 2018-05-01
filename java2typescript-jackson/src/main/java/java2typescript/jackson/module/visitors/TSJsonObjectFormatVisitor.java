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

import static com.fasterxml.jackson.databind.PropertyName.NO_NAME;
import static java.lang.reflect.Modifier.isPublic;
import static java2typescript.jackson.module.visitors.TSJsonFormatVisitorWrapper.getTSTypeForHandler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import java2typescript.jackson.module.grammar.AnyType;
import java2typescript.jackson.module.grammar.FunctionType;
import java2typescript.jackson.module.grammar.ClassType;
import java2typescript.jackson.module.grammar.TypeDeclarationType;
import java2typescript.jackson.module.grammar.VoidType;
import java2typescript.jackson.module.grammar.base.AbstractType;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java2typescript.jackson.module.Configuration;

public class TSJsonObjectFormatVisitor extends ABaseTSJsonFormatVisitor<ClassType> implements JsonObjectFormatVisitor {

	private Class clazz;
	private List<String> blackListField = new ArrayList<String>();

	public TSJsonObjectFormatVisitor(ABaseTSJsonFormatVisitor<?> parentHolder, String className, Class clazz, Configuration conf) {
		super(parentHolder, conf);
		type = new ClassType(className);
		this.clazz = clazz;
	}

	private void addField(String name, AbstractType fieldType) {
        if (!blackListField.contains(name)) {
            type.getFields().put(name, fieldType);
        }
	}

	private boolean isAccessorMethod(Method method, BeanInfo beanInfo) {
		for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
			if (method.equals(property.getReadMethod())) {
				return true;
			}
			if (method.equals(property.getWriteMethod())) {
				return true;
			}
		}
		return false;
	}

	private void blackListUnnecessaryFieldMethods(Method method) {
        Pattern getSearcher = Pattern.compile("^get.*");
        Pattern setSearcher = Pattern.compile("^set.*");

        String methodName = method.getName();
        String ignoredField;

        if (getSearcher.matcher(method.getName()).matches()) {
            ignoredField = methodName.replaceFirst("^get","");
            ignoredField = Introspector.decapitalize(ignoredField);
            blackListField.add(ignoredField);
        } else if (setSearcher.matcher(method.getName()).matches()) {
            ignoredField = methodName.replaceFirst("^set","");
			ignoredField = Introspector.decapitalize(ignoredField);
            blackListField.add(ignoredField);
        }
    }

	void addPublicMethods() {

		for (Method method : this.clazz.getDeclaredMethods()) {

			// Only public
			if (!isPublic(method.getModifiers())) {
				continue;
			}

            //Don't exclude accessors with HTTP Annotations
			if (conf.getJaxrsRun() && conf.methodHasHTTPAnnotation(method)) {
				addMethod(method);
				blackListUnnecessaryFieldMethods(method);
				continue;
			}

			// Exclude accessors
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
				if (isAccessorMethod(method, beanInfo)) {
					continue;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			if(conf.isIgnoredMethod(method)) {
				continue;
			}

			addMethod(method);
		}
	}

	private AbstractType getTSTypeForClass(AnnotatedMember member) {

		TypeBindings bindings = new TypeBindings(TypeFactory.defaultInstance(), member.getDeclaringClass());
		BeanProperty prop = new BeanProperty.Std(member.getName(), member.getType(bindings), NO_NAME,
				new AnnotationMap(), member, false);

		try {
			return getTSTypeForProperty(prop);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		}
	}

	private void addMethod(Method method) {
		FunctionType function = new FunctionType();

		AnnotatedMethod annotMethod = new AnnotatedMethod(null, method, new AnnotationMap(), null);

		function.setResultType(getTSTypeForClass(annotMethod));
		for (int i = 0; i < annotMethod.getParameterCount(); i++) {
			AnnotatedParameter param = annotMethod.getParameter(i);
			String name = "param" + i;
			function.getParameters().put(name, getTSTypeForClass(param));
		}
		this.type.getMethods().put(method.getName(), function);
	}

	@Override
	public void property(BeanProperty writer) throws JsonMappingException {
		addField(writer.getName(), getTSTypeForProperty(writer));
	}

	@Override
	public void property(String name, JsonFormatVisitable handler, JavaType propertyTypeHint)
			throws JsonMappingException {
		addField(name, getTSTypeForHandler(this, handler, propertyTypeHint, conf));
	}


	@Override
	public void optionalProperty(BeanProperty writer) throws JsonMappingException {
		addField(writer.getName(), getTSTypeForProperty(writer));
	}

	@Override
	public void optionalProperty(String name, JsonFormatVisitable handler, JavaType propertyTypeHint)
			throws JsonMappingException {
		addField(name, getTSTypeForHandler(this, handler, propertyTypeHint, conf));
	}

	protected AbstractType getTSTypeForProperty(BeanProperty writer) throws JsonMappingException {
		if (writer == null) {
			throw new IllegalArgumentException("Null writer");
		}
		JavaType type = writer.getType();
		if (type.getRawClass().equals(Void.TYPE)) {
			return VoidType.getInstance();
		}

		AbstractType customType = conf.getCustomTypes().get(type.getRawClass().getName());
		if(customType != null) {
			if(customType instanceof TypeDeclarationType) {
				TypeDeclarationType tdt = (TypeDeclarationType) customType;
				getModule().getNamedTypes().put(tdt.getName(), tdt);
			}
			return customType;
		}

		try {
			JsonSerializer<Object> ser = getSer(writer);

			if (ser != null) {
				if (type == null) {
					throw new IllegalStateException("Missing type for property '" + writer.getName() + "'");
				}
				return getTSTypeForHandler(this, ser, type, conf);
			} else {
				return AnyType.getInstance();
			}

		} catch (Exception e) {
			throw new RuntimeException(String.format(//
					"Error when serializing %s, you should add a custom mapping for it", type.getRawClass()), e);
		}

	}

	protected JsonSerializer<java.lang.Object> getSer(BeanProperty writer) throws JsonMappingException {
		JsonSerializer<Object> ser = null;
		if (writer instanceof BeanPropertyWriter) {
			ser = ((BeanPropertyWriter) writer).getSerializer();
		}
		if (ser == null) {
			ser = getProvider().findValueSerializer(writer.getType(), writer);
		}
		return ser;
	}

}
