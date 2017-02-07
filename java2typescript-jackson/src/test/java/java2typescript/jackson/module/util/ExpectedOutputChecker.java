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
		compareFileContent(out, getCaller());
	}

	public static void checkOutputFromFileEquals(Writer out) {
		Assert.assertEquals(getExpectedOutput(getCaller()), out.toString());
	}
	private static StackTraceElement getCaller() {
		return new Throwable().getStackTrace()[2];
	}
	private static void compareFileContent(Writer out, StackTraceElement testMethodStackTraceElem) {
		// Can't rely on specific order of classes/fields/methods, so file content equality can't be used.
		// Using naive approach to check that actual output contains exactly the same lines as expected output
		Assert.assertEquals(getLinesAlphabetically(getExpectedOutput(testMethodStackTraceElem)), getLinesAlphabetically(out.toString()));
	}

	private static List<String> getLinesAlphabetically(String s) {
		List<String> lines = Lists.newArrayList(s.split("\\n"));
		Collections.sort(lines);
		return lines;
	}

	private static String getExpectedOutput(StackTraceElement testMethodStackTraceElem) {
		String testMethodName = testMethodStackTraceElem.getMethodName();
		String className = testMethodStackTraceElem.getClassName();
		String expectedOutputFromFile = getFileContent(className.replace('.', '/') + "." + testMethodName + ".d.ts");
		expectedOutputFromFile = expectedOutputFromFile.replace("\r", "");
		return expectedOutputFromFile;
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
