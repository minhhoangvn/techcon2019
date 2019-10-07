package kms.coe.katalon.plugin;

import com.katalon.platform.api.Plugin;
import com.katalon.platform.api.extension.PluginActivationListener;

public class PluginActivationListenerImpl implements PluginActivationListener {

	@Override
	public void afterActivation(Plugin plugin) {
		System.out.println("Install Plugin Successfully");
//		System.out.println(plugin.getPluginId());
//		System.out.println(plugin.getExtensionPoints());
//		plugin.getExtensionPoints().forEach(extensionPoints -> {
//			System.out.println(extensionPoints.getExtensionPointId());
//			System.out.println(extensionPoints.getInterfaceClassName());
//			System.out.println(extensionPoints.getPluginId());
//			System.out.println(extensionPoints.getServiceClass());
//		});
//		plugin.getExtensions().forEach(extension -> {
//			System.out.println(extension.getExtensionId());
//			System.out.println(extension.getExtensionPointId());
//			System.out.println(extension.getImplementationClass());
//			System.out.println(extension.getPluginId());
//		});
	}

	@Override
	public void beforeDeactivation(Plugin plugin) {
		System.out.println("Remove Plugin Successfully");
//		System.out.println(plugin.getPluginId());
//		System.out.println(plugin.getExtensionPoints());
//		plugin.getExtensionPoints().forEach(extensionPoints -> {
//			System.out.println(extensionPoints.getExtensionPointId());
//			System.out.println(extensionPoints.getInterfaceClassName());
//			System.out.println(extensionPoints.getPluginId());
//			System.out.println(extensionPoints.getServiceClass());
//		});
//		plugin.getExtensions().forEach(extension -> {
//			System.out.println(extension.getExtensionId());
//			System.out.println(extension.getExtensionPointId());
//			System.out.println(extension.getImplementationClass());
//			System.out.println(extension.getPluginId());
//		});
	}
}
