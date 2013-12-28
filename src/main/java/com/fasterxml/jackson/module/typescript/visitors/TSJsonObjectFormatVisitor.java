package com.fasterxml.jackson.module.typescript.visitors;

import static com.fasterxml.jackson.databind.PropertyName.NO_NAME;
import static com.fasterxml.jackson.module.typescript.visitors.TSJsonFormatVisitorWrapper.getTSTypeForHandler;
import static java.lang.reflect.Modifier.isPublic;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.typescript.grammar.AnyType;
import com.fasterxml.jackson.module.typescript.grammar.FunctionType;
import com.fasterxml.jackson.module.typescript.grammar.ObjectType;
import com.fasterxml.jackson.module.typescript.grammar.VoidType;
import com.fasterxml.jackson.module.typescript.grammar.base.AbstractType;

public class TSJsonObjectFormatVisitor extends ABaseTSJsonFormatVisitor<ObjectType> implements JsonObjectFormatVisitor {

	private Class clazz;

	public TSJsonObjectFormatVisitor(ABaseTSJsonFormatVisitor<?> parentHolder, String className, Class clazz) {
		super(parentHolder);
		type = new ObjectType(className);
		this.clazz = clazz;
		addPublicMethods();
	}

	private void addField(String name, AbstractType fieldType) {
		type.getFields().put(name, fieldType);
	}

	private void addPublicMethods() {

		List<Method> res = new ArrayList<Method>();
		for (Method method : this.clazz.getDeclaredMethods()) {
			if (!isPublic(method.getModifiers()))
				continue;
			String name = method.getName();

			// Ignore accessors
			if (name.startsWith("get") || name.startsWith("set") || name.startsWith("is"))
				continue;

			// Ignore @Transient methods
			if (method.getAnnotation(Transient.class) != null)
				continue;

			addMethod(method);
		}
	}

	private AbstractType getTSTypeForClass(AnnotatedMember member) {

		TypeBindings bindings = new TypeBindings(TypeFactory.defaultInstance(), member.getDeclaringClass());
		BeanProperty prop = new BeanProperty.Std(member.getName(), member.getType(bindings), NO_NAME, null, member,
				false);

		try {
			return getTSTypeForProperty(prop);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		}
	}

	private void addMethod(Method method) {
		FunctionType function = new FunctionType();

		AnnotatedMethod annotMethod = new AnnotatedMethod(method, null, null);

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
		addField(name, getTSTypeForHandler(this, handler, propertyTypeHint));
	}

	@Override
	public void property(String name) throws JsonMappingException {
		addField(name, AnyType.getIntance());
	}

	@Override
	public void optionalProperty(BeanProperty writer) throws JsonMappingException {
		addField(writer.getName(), getTSTypeForProperty(writer));
	}

	@Override
	public void optionalProperty(String name, JsonFormatVisitable handler, JavaType propertyTypeHint)
			throws JsonMappingException {
		addField(name, getTSTypeForHandler(this, handler, propertyTypeHint));
	}

	@Override
	public void optionalProperty(String name) throws JsonMappingException {
		addField(name, AnyType.getIntance());
	}

	protected AbstractType getTSTypeForProperty(BeanProperty writer) throws JsonMappingException {
		if (writer == null) {
			throw new IllegalArgumentException("Null writer");
		}
		JavaType type = writer.getType();
		if (type.getRawClass().equals(Void.TYPE)) {
			return VoidType.getInstance();
		}
		JsonSerializer<Object> ser = getSer(writer);
		if (ser != null) {
			if (type == null) {
				throw new IllegalStateException("Missing type for property '" + writer.getName() + "'");
			}
			return getTSTypeForHandler(this, ser, type);
		} else {
			return AnyType.getIntance();
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
