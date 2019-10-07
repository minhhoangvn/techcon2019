package kms.coe.katalon.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.katalon.platform.api.console.PluginConsoleOption;
import com.katalon.platform.api.extension.LauncherOptionParserDescription;
import com.katalon.platform.api.model.TestCaseEntity;

import kms.coe.katalon.plugin.internal.ReportPortalConsoleOption;
import kms.coe.katalon.plugin.internal.TagsConsoleOption;
import kms.coe.katalon.plugin.utils.Constant;
import kms.coe.katalon.plugin.utils.TagsUtils;

public class LauncherOptionParserDescriptionImpl implements LauncherOptionParserDescription {

	PluginConsoleOption<String> tagArgumentDataOption = new TagsConsoleOption.TagsData() {
		@Override
		public String getOption() {
			return Constant.TAGS_DATA_OPTION;
		}

		@Override
		public boolean isRequired() {
			return false;
		}
	};

	PluginConsoleOption<String> tagArgumentDelimiterCharOption = new TagsConsoleOption.TagsDelimiterChar() {

		@Override
		public String getOption() {
			return Constant.TAGS_DELIMITER_CHAR_OPTION;
		}

		@Override
		public boolean isRequired() {
			return false;
		}
	};

	PluginConsoleOption<String> rpTokenArgumentOption = new ReportPortalConsoleOption.ReportPortalToken() {

		@Override
		public String getOption() {
			return Constant.RP_TOKEN_OPTION;
		}

		@Override
		public boolean isRequired() {
			return false;
		}
	};

	PluginConsoleOption<String> rpApiUrlArgumentOption = new ReportPortalConsoleOption.ReportPortalToken() {

		@Override
		public String getOption() {
			return Constant.RP_API_URL_OPTION;
		}

		@Override
		public boolean isRequired() {
			return false;
		}
	};

	PluginConsoleOption<String> rpProjectNameArgumentOption = new ReportPortalConsoleOption.ReportPortalToken() {

		@Override
		public String getOption() {
			return Constant.RP_PROJECT_NAME_OPTION;
		}

		@Override
		public boolean isRequired() {
			return false;
		}
	};

	PluginConsoleOption<String> rpLaunchNameArgumentOption = new ReportPortalConsoleOption.ReportPortalToken() {

		@Override
		public String getOption() {
			return Constant.RP_LAUNCH_NAME_OPTION;
		}

		@Override
		public boolean isRequired() {
			return false;
		}
	};

	PluginConsoleOption<String> rpConfigFilePathArgumentOption = new ReportPortalConsoleOption.ReportPortalConfigFilePath() {

		@Override
		public boolean isRequired() {
			return false;
		}

		@Override
		public String getOption() {
			return Constant.RP_CONFIG_FILE_OPTION;
		}
	};

	@Override
	public List<PluginConsoleOption<?>> getConsoleOptionList() {
		return Arrays.asList(tagArgumentDataOption, tagArgumentDelimiterCharOption, rpTokenArgumentOption,
				rpApiUrlArgumentOption, rpLaunchNameArgumentOption, rpProjectNameArgumentOption);
	}

	@Override
	public List<TestCaseEntity> onPreExecution(List<TestCaseEntity> testCases) {
		List<TestCaseEntity> filterTest = new ArrayList<>();
		String delimiterChar = tagArgumentDelimiterCharOption.getValue();
		Set<String> executionTags = TagsUtils.parsingRawTags(tagArgumentDataOption.getValue(), delimiterChar);
		testCases.forEach(testCase -> {
			if (TagsUtils.isMatchedTestCaseTags(executionTags, testCase, delimiterChar)) {
				filterTest.add(testCase);
			}
		});
		return filterTest.isEmpty() ? testCases : filterTest;
	}

	@Override
	public void onConsoleOptionDetected(PluginConsoleOption<?> detectedConsoleOption) {
		String optionValue = detectedConsoleOption.getValue().toString();
		switch (detectedConsoleOption.getOption()) {
		case Constant.RP_API_URL_OPTION:
			rpApiUrlArgumentOption.setValue(optionValue);
			break;
		case Constant.RP_LAUNCH_NAME_OPTION:
			rpLaunchNameArgumentOption.setValue(optionValue);
			break;
		case Constant.RP_PROJECT_NAME_OPTION:
			rpProjectNameArgumentOption.setValue(optionValue);
			break;
		case Constant.RP_TOKEN_OPTION:
			rpTokenArgumentOption.setValue(optionValue);
			break;
		case Constant.RP_CONFIG_FILE_OPTION:
			rpConfigFilePathArgumentOption.setValue(optionValue);
			break;
		case Constant.TAGS_DATA_OPTION:
			tagArgumentDataOption.setValue(optionValue);
			break;
		case Constant.TAGS_DELIMITER_CHAR_OPTION:
			tagArgumentDelimiterCharOption.setValue(optionValue);
			break;
		default:
			break;
		}

	}

}
