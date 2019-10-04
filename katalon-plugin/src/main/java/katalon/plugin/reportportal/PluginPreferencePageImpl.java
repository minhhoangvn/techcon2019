package katalon.plugin.reportportal;

import org.eclipse.jface.preference.PreferencePage;

import com.katalon.platform.api.extension.PluginPreferencePage;

import katalon.plugin.reportportal.internal.ReportPortalPluginConstants;

public class PluginPreferencePageImpl implements PluginPreferencePage {

	@Override
	public String getName() {
		return "CoE Katalon Plugins";
	}

	@Override
	public String getPageId() {
		return ReportPortalPluginConstants.PREF_PAGE_ID.value();
	}

	@Override
	public Class<? extends PreferencePage> getPreferencePageClass() {
		System.out.println("PreferencePageImpl.class");
		return PreferencePageImpl.class;
	}

}
