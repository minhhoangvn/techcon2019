package katalon.plugin.reportportal.internal;

import com.katalon.platform.api.Application;
import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.service.PreferenceManager;
import com.katalon.platform.api.service.ProjectManager;

public interface ReportPortalComponent {
	default PluginPreference getPluginStore() throws ResourceException {
		Application application = ApplicationManager.getInstance();
		PreferenceManager preferenceManager = application.getPreferenceManager();
		ProjectManager projectManager = application.getProjectManager();
		return preferenceManager.getPluginPreference(
				projectManager.getCurrentProject().getId(), ReportPortalPluginConstants.PLUGIN_ID.value());
	}
}
