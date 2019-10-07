package kms.coe.katalon.plugin.internal;

public class ReportPortalConsoleOption {
	public static abstract class ReportPortalConfigFilePath extends AbstractConsoleOption<String> {
		
		@Override
		public Class<String> getArgumentType() {
			return String.class;
		}

		@Override
		public void setValue(String rawValue) {
			value = rawValue;
		}
	}
	
	public static abstract class ReportPortalToken extends AbstractConsoleOption<String> {

		@Override
		public String getDefaultArgumentValue() {
			return "728938ee-d333-4e79-a583-9063d647222f";
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

	public static abstract class ReportPortalApiUrl extends AbstractConsoleOption<String> {

		@Override
		public String getDefaultArgumentValue() {
			return "http://reportportal.testing.coe.com/api/v1";
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

	public static abstract class ReportPortaProjectName extends AbstractConsoleOption<String> {

		
		@Override
		public String getDefaultArgumentValue() {
			return "Automation Suite";
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

	public static abstract class ReportPortaLaunchName extends AbstractConsoleOption<String> {

		@Override
		public String getDefaultArgumentValue() {
			return "Automation Launch";
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
