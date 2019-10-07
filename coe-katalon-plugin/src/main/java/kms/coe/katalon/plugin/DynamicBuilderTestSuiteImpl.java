package kms.coe.katalon.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.katalon.platform.api.Plugin;
import com.katalon.platform.api.console.PluginConsoleOption;
import com.katalon.platform.api.controller.FolderController;
import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.extension.DynamicQueryingTestSuiteDescription;
import com.katalon.platform.api.extension.LauncherOptionParserDescription;
import com.katalon.platform.api.extension.PluginActivationListener;
import com.katalon.platform.api.model.FolderEntity;
import com.katalon.platform.api.model.ProjectEntity;
import com.katalon.platform.api.model.TestCaseEntity;
import com.katalon.platform.api.model.TestSuiteEntity;
import com.katalon.platform.api.service.ApplicationManager;

public class DynamicBuilderTestSuiteImpl
		implements DynamicQueryingTestSuiteDescription, PluginActivationListener, LauncherOptionParserDescription {

	@Override
	public void beforeDeactivation(Plugin plugin) {
		System.out.println("Hello, coe before remove plugin");
	}

	@Override
	public void afterActivation(Plugin plugin) {
		System.out.println("Hello, coe try to implement plugin v2: " + plugin.getPluginId());
	}
	
	private static final List<String> DEFAULT_KEYWORDS = Arrays.asList("ids", "id", "name", "tag", "comment",
			"description");

	private static final List<String> DEFAULT_KEYWORDS_FOR_INPUTS = Arrays.asList("id", "name", "tag", "comment",
			"description");

	private static final String CONTENT_DELIMITER = ",";

	private FolderController folderController = ApplicationManager.getInstance().getControllerManager()
			.getController(FolderController.class);

	@Override
	public List<TestCaseEntity> query(ProjectEntity project, TestSuiteEntity testSuite, String fullSearchText)
			throws ResourceException {
		FolderEntity testCaseRoot = folderController.getFolder(project, "Test Cases");

		List<TestCaseEntity> allTestCases = getAllTestCases(project, testCaseRoot);
		return allTestCases.stream().filter(e -> isMatched(e, fullSearchText)).collect(Collectors.toList());
	}

	private List<TestCaseEntity> getAllTestCases(ProjectEntity project, FolderEntity parentFolder)
			throws ResourceException {
		List<TestCaseEntity> childTestCases = folderController.getChildTestCases(project, parentFolder);

		for (FolderEntity childFolder : folderController.getChildFolders(project, parentFolder)) {
			childTestCases.addAll(getAllTestCases(project, childFolder));
		}
		return childTestCases;
	}

	public List<String> getDefaultKeywords() {
		List<String> keywords = new ArrayList<>();
		keywords.addAll(DEFAULT_KEYWORDS);
		return keywords;
	}

	public List<String> getDefaultKeywordsForInputs() {
		List<String> keywordsForInputs = new ArrayList<>();
		keywordsForInputs.addAll(DEFAULT_KEYWORDS_FOR_INPUTS);
		return keywordsForInputs;
	}

	public boolean isMatched(TestCaseEntity testCase, String filteringText) {
		String trimmedText = filteringText.trim();
		if (trimmedText.equals(StringUtils.EMPTY)) {
			return false;
		}
		List<String> keywordList = getDefaultKeywords();
		Map<String, String> tagMap = parseSearchedString(keywordList.toArray(new String[0]), trimmedText);

		if (!tagMap.isEmpty()) {
			for (Entry<String, String> entry : tagMap.entrySet()) {
				String keyword = entry.getKey();
				if (keywordList.contains(keyword) && !compare(testCase, keyword, entry.getValue())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * parse searched string into a map of search tags of an entity element
	 * 
	 * @param element is ITreeEntity
	 * @return
	 */
	public Map<String, String> parseSearchedString(String[] searchTags, String contentString) {
		if (searchTags != null) {
			Map<String, String> tagMap = new HashMap<String, String>();
			for (int i = 0; i < searchTags.length; i++) {
				String tagRegex = searchTags[i] + "=\\([^\\)]+\\)";
				Matcher m = Pattern.compile(tagRegex).matcher(contentString);
				while (m.find()) {
					String tagContent = contentString.substring(m.start() + searchTags[i].length() + 2, m.end() - 1);
					tagMap.put(searchTags[i], tagContent);
				}
			}
			return tagMap;
		} else {
			return Collections.emptyMap();
		}

	}

	public String getPropertyValue(TestCaseEntity fileEntity, String keyword) {
		switch (keyword) {
		case "id":
			return fileEntity.getId();
		case "name":
			return fileEntity.getName();
		case "tag":
			return fileEntity.getTags();
		case "description":
			return fileEntity.getDescription();
		default:
			return "";
		}
	}

	public boolean compare(TestCaseEntity fileEntity, String keyword, String text) {
		if (fileEntity == null || keyword == null || text == null) {
			return false;
		}
		switch (keyword) {
		case "ids":
			return textContainsEntityId(text.toLowerCase(), fileEntity);
		case "id":
			return StringUtils.equalsIgnoreCase(fileEntity.getId(), text)
					|| StringUtils.startsWithIgnoreCase(fileEntity.getId(), text + "/");
		case "name":
			return StringUtils.containsIgnoreCase(fileEntity.getName(), text);
		case "tag":
			return StringUtils.containsIgnoreCase(fileEntity.getTags(), text);
		case "description":
			return StringUtils.containsIgnoreCase(fileEntity.getDescription(), text);
		default:
			return false;
		}
	}

	private boolean textContainsEntityId(String text, TestCaseEntity testCase) {
		// Allow spaces before and after delimiter
		return Arrays.asList(text.split(CONTENT_DELIMITER)).stream().map(a -> a.trim())
				.filter(a -> StringUtils.equalsIgnoreCase(testCase.getId(), a)
						|| StringUtils.startsWithIgnoreCase(testCase.getId(), a + "/"))
				.findAny().isPresent();
	}

	@Override
	public String getQueryingType() {
		return "CoE";
	}

	List<PluginConsoleOption<?>> consoleOptions;
	@Override
	public List<PluginConsoleOption<?>> getConsoleOptionList() {
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("consoleOptions");
		return this.consoleOptions;

	}

	@Override
	public void onConsoleOptionDetected(PluginConsoleOption<?> detectedConsoleOption) {
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("onConsoleOptionDetected: " + detectedConsoleOption);
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		consoleOptions.add(detectedConsoleOption);

	}
	
	@Override
	public List<TestCaseEntity> onPreExecution(List<TestCaseEntity> testCases) {
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("onConsoleOptionDetected: " + testCases);
		return LauncherOptionParserDescription.super.onPreExecution(testCases);
	}


}
