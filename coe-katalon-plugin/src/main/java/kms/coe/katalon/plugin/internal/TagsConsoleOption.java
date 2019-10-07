package kms.coe.katalon.plugin.internal;

public class TagsConsoleOption {
	public static abstract class TagsData extends AbstractConsoleOption<String> {
		@Override
		public Class<String> getArgumentType() {
			return String.class;
		}

		@Override
		public String getDefaultArgumentValue() {
			return "";
		}

		@Override
		public void setValue(String rawValue) {
			value = rawValue;
		}
	}

	public static abstract class TagsDelimiterChar extends AbstractConsoleOption<String> {
		@Override
		public Class<String> getArgumentType() {
			return String.class;
		}

		@Override
		public String getDefaultArgumentValue() {
			return ",";
		}

		@Override
		public void setValue(String rawValue) {
			value = rawValue;
		}
	}
}
