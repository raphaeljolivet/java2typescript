package java2typescript.jackson.module.util;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import org.junit.Assert;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class ExpectedOutputChecker {

	public static void checkOutputFromFile(Writer out) {
		Assert.assertEquals(getExpectedOutput(), out.toString());
	}

	private static String getExpectedOutput() {
		StackTraceElement testMethodStackTraceElem = new Throwable().getStackTrace()[2];
		String testMethodName = testMethodStackTraceElem.getMethodName();
		String className = testMethodStackTraceElem.getClassName();
		return getFileContent(className.replace('.', '/') + "." + testMethodName + ".d.ts");
	}

	private static String getFileContent(String resourceName) {
		URL url = Resources.getResource(resourceName);
		try {
			return Resources.toString(url, Charsets.UTF_8);
		}
		catch (IOException e) {
			throw new RuntimeException("failed to read content of " + url, e);
		}
	}
}
