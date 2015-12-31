package java2typescript.jackson.module.util;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

public class ExpectedOutputChecker {

	public static void checkOutputFromFile(Writer out) {
		compareFileContent(out);
	}

	private static void compareFileContent(Writer out) {
		// Can't rely on specific order of classes/fields/methods, so file content equality can't be used.
		// Using naive approach to check that actual output contains exactly the same lines as expected output
		Assert.assertEquals(getLinesAlphabetically(getExpectedOutput()), getLinesAlphabetically(out.toString()));
	}

	private static List<String> getLinesAlphabetically(String s) {
		List<String> lines = Lists.newArrayList(s.split("\\n"));
		Collections.sort(lines);
		return lines;
	}

	private static String getExpectedOutput() {
		StackTraceElement testMethodStackTraceElem = new Throwable().getStackTrace()[3];
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
