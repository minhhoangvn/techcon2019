package katalon.plugin.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.katalon.platform.api.model.TestCaseEntity;

public class TagsUtils {
	private TagsUtils() {

	}

	private static final String DEFAULT_DELIMITER_CHAR = ",";

	public static Set<String> parsingRawTags(String rawTags, String delimiterChar) {
		if (rawTags == null || rawTags.isEmpty())
			return new HashSet<String>();
		if (delimiterChar == null)
			delimiterChar = DEFAULT_DELIMITER_CHAR;
		return new HashSet<String>(Arrays.asList(rawTags.split(delimiterChar)));
	}

	public static Boolean isMatchedTestCaseTags(Set<String> executionTags, TestCaseEntity testCase,
			String delimiterChar) {
		String rawTags = testCase.getTags();
		Set<String> testCaseTags = parsingRawTags(rawTags, delimiterChar);
		return isMatchedTag(executionTags, testCaseTags);
	}

	public static Boolean isMatchedTag(Set<String> executionTags, Set<String> testCaseTags) {
		for (String tag : executionTags) {
			for (String testCaseTag : testCaseTags)
				if (testCaseTag.trim().equalsIgnoreCase(tag.trim())) {
					return true;
				}
		}
		return false;
	}
}
