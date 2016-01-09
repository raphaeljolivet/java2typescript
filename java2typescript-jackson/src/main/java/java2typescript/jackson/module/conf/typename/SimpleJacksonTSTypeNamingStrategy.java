package java2typescript.jackson.module.conf.typename;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;

/**
 * If Java class/enum is annotated with {@link JsonTypeName}, then annotation value is used, otherwise {@link Class#getSimpleName()}
 */
public class SimpleJacksonTSTypeNamingStrategy implements TSTypeNamingStrategy {

	public String getName(JavaType type) {
		Class<?> rawClass = type.getRawClass();
		JsonTypeName typeName = rawClass.getAnnotation(JsonTypeName.class);
		if (typeName != null) {
			return typeName.value();
		}
		else {
			return getName(rawClass);
		}
	}

	protected String getName(Class<?> rawClass) {
		return rawClass.getSimpleName();
	}

}
