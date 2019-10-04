package katalon.plugin.reportportal;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;

import katalon.plugin.reportportal.internal.ReportPortalComponent;
import katalon.plugin.reportportal.internal.ReportPortalPluginConstants;

public class PreferencePageImpl extends PreferencePage implements ReportPortalComponent {

	private Composite container;
	private Text txtRPEndPoint;
	private Text txtRPToken;
	private Text txtRPProjectName;
	private Text txtRPLaunchName;

	@Override
	protected Control createContents(Composite parent) {
		InputStream imageStream = new BufferedInputStream(getClass().getResourceAsStream("/icons/secret-icon.png"));
		Image image = new Image(parent.getDisplay(), imageStream);
		container = new Composite(parent, SWT.INHERIT_FORCE);
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		container.setLayout(new GridLayout(1, false));
		// Create group layout
		createLabel(container, "ReportPortal.io Setting");
		Group grpReportPortal = new Group(container, SWT.NONE);

		grpReportPortal.setBackgroundImage(image);
		grpReportPortal.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		GridLayout glRPAuthentication = new GridLayout(2, false);
		glRPAuthentication.horizontalSpacing = 15;
		glRPAuthentication.verticalSpacing = 10;
		grpReportPortal.setLayoutData(new GridData(600, 400));
		grpReportPortal.setLayout(glRPAuthentication);
		// Create configuration element
		createLabel(grpReportPortal, "API EndPoint");
		txtRPEndPoint = createTextbox(grpReportPortal);
		txtRPEndPoint.setToolTipText("Sample API Endpoint URL http://reportportal.testing.coe.com/api/v1");

		createLabel(grpReportPortal, "API Token");
		txtRPToken = createTextbox(grpReportPortal);
		txtRPToken.setToolTipText("Sample API Token 0704f3ca-0a2c-4be5-80b7-af625601fd44");

		createLabel(grpReportPortal, "Project Name");
		txtRPProjectName = createTextbox(grpReportPortal);
		txtRPProjectName.setToolTipText("Sample Project Name superadmin_personal");

		createLabel(grpReportPortal, "Launch Name");
		txtRPLaunchName = createTextbox(grpReportPortal);
		txtRPLaunchName.setToolTipText("Sample Launch Name superadmin_TEST_EXAMPLE");
		initializeInput();
		return container;
	}

	@Override
	public boolean performOk() {
		try {
			PluginPreference pluginStore = getPluginStore();

			pluginStore.setString(ReportPortalPluginConstants.REPORT_PORTAL_ENDPOINT.value(), txtRPEndPoint.getText());
			pluginStore.setString(ReportPortalPluginConstants.REPORT_PORTAL_LAUNCH_NAME.value(),
					txtRPLaunchName.getText());
			pluginStore.setString(ReportPortalPluginConstants.REPORT_PORTAL_PROJECT_NAME.value(),
					txtRPProjectName.getText());
			pluginStore.setString(ReportPortalPluginConstants.REPORT_PORTAL_TOKEN.value(), txtRPToken.getText());
			pluginStore.save();
			return true;
		} catch (ResourceException e) {
			MessageDialog.openWarning(getShell(), "Warning", "Unable to update ReportPortal.io Integration Settings.");
			return false;
		}
	}

	private Text createTextbox(Composite parent) {
		Text text = new Text(parent, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.widthHint = 200;
		text.setLayoutData(gridData);
		return text;
	}

	private void createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		label.setLayoutData(gridData);
	}

	private void initializeInput() {
		try {
			PluginPreference pluginStore = getPluginStore();
			txtRPEndPoint
					.setText(pluginStore.getString(ReportPortalPluginConstants.REPORT_PORTAL_ENDPOINT.value(), ""));
			txtRPLaunchName
					.setText(pluginStore.getString(ReportPortalPluginConstants.REPORT_PORTAL_LAUNCH_NAME.value(), ""));
			txtRPProjectName
					.setText(pluginStore.getString(ReportPortalPluginConstants.REPORT_PORTAL_PROJECT_NAME.value(), ""));
			txtRPToken.setText(pluginStore.getString(ReportPortalPluginConstants.REPORT_PORTAL_TOKEN.value(), ""));
			container.layout(true, true);
		} catch (ResourceException e) {
			MessageDialog.openWarning(getShell(), "Warning", "Unable to update ReportPortal.io Settings.");
		}
	}
}
