package kms.coe.katalon.plugin.internal;

public class TestImpactConsoleOption {
	public static abstract class TestImpactFilePath extends AbstractConsoleOption<String>{
		
		@Override
		public String getDefaultArgumentValue() {
			return null;
		}

		@Override
		public Class<String> getArgumentType() {
			return String.class;
		}

		@Override
		public void setValue(String rawValue) {
			value = rawValue;
		}
	}
}
