package katalon.plugin.reportportal.internal;

public enum ReportPortalPluginConstants {
	PLUGIN_ID("kms.coe.katalon.plugins"),
	PREF_PAGE_ID("kms.coe.katalon.plugin.reportportal.PluginPreferencePageImpl"),
	REPORT_PORTAL_TOKEN("reportPortal.token"), REPORT_PORTAL_ENDPOINT("reportPortal.endPoint"),
	REPORT_PORTAL_PROJECT_NAME("reportPortal.projectName"), REPORT_PORTAL_LAUNCH_NAME("reportPortal.launchName");
	
	private String value;

	private ReportPortalPluginConstants(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
