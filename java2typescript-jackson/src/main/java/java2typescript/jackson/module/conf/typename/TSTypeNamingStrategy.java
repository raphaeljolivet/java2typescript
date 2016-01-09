package java2typescript.jackson.module.conf.typename;

import com.fasterxml.jackson.databind.JavaType;

/**
 * Used to detect TypeScript type name based on given Java type
 */
public interface TSTypeNamingStrategy {

	/**
	 * @return name of the TypeScript class corresponding to Java class
	 */
	String getName(JavaType type);

}
