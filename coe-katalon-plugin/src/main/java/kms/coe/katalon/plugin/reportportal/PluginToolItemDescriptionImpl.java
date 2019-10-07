package kms.coe.katalon.plugin.reportportal;

import com.katalon.platform.api.extension.ToolItemDescription;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.DialogActionService;

import kms.coe.katalon.plugin.reportportal.internal.ReportPortalPluginConstants;

public class PluginToolItemDescriptionImpl implements ToolItemDescription {

	@Override
	public String toolItemId() {
		return ReportPortalPluginConstants.PLUGIN_ID.name() + ".reportPortalToolItem";
	}

	@Override
	public String name() {
		return "CoE Katalon Plugins";
	}

	@Override
	public String iconUrl() {
		return "platform:/plugin/" + ReportPortalPluginConstants.PLUGIN_ID.value() + "/icons/coe-icon.png";
	}

	@Override
	public void handleEvent() {
		ApplicationManager.getInstance().getUIServiceManager().getService(DialogActionService.class)
				.openPluginPreferencePage(ReportPortalPluginConstants.PREF_PAGE_ID.value());
	}

}
