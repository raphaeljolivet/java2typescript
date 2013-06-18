package com.fasterxml.jackson.module.typescript.visitors;

import static com.fasterxml.jackson.module.typescript.visitors.TSJsonFormatVisitorWrapper.getTSTypeForHandler;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.module.typescript.grammar.AType;
import com.fasterxml.jackson.module.typescript.grammar.AnyType;
import com.fasterxml.jackson.module.typescript.grammar.ObjectType;


public class TSJsonObjectFormatVisitor extends BaseTSJsonFormatVisitor<ObjectType> implements JsonObjectFormatVisitor {

	public TSJsonObjectFormatVisitor(BaseTSJsonFormatVisitor<?> parentHolder, String className) {
		super(parentHolder);
		type = new ObjectType(className);
	}

	private void addField(String name, AType fieldType) {
		type.getFields().put(name, fieldType);
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
		addField(name, new AnyType());
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
		addField(name, new AnyType());
	}

	protected AType getTSTypeForProperty(BeanProperty writer) throws JsonMappingException {
		if (writer == null) {
			throw new IllegalArgumentException("Null writer");
		}
		JsonSerializer<Object> ser = getSer(writer);
		if (ser != null) {
			JavaType type = writer.getType();
			if (type == null) {
				throw new IllegalStateException("Missing type for property '" + writer.getName() + "'");
			}
			return getTSTypeForHandler(this, ser, type);
		} else {
			return new AnyType();
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
