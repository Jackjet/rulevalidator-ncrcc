package com.yonyou.nc.codevalidator.runtime.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.config.IRuleConfig;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

public class Activator extends AbstractUIPlugin {

	private static BundleContext context;

	private static Activator plugin;

	public static final String PLUGIN_ID = "com.yonyou.nc.codevalidator.runtime.plugin";

	public static final String APPTITLE = "Rulecase Validate Tool ";

	// private ServiceRegistration<IRuleLogger> ruleLoggerRegistration;

	// private LogService logService;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		Activator.context = bundleContext;
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		plugin = null;
		JFaceResources.getImageRegistry().dispose();
	}

	public static Activator getDefault() {
		return plugin;
	}

	public IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		return window == null ? null : window.getShell();
	}

	/**
	 * 懒加载的方式添加Image资源的处理
	 * @param imageFilePath
	 * @return
	 */
	public static Image imageFromPlugin(String imageFilePath) {
		Image image = JFaceResources.getImageRegistry().get(imageFilePath);
		if(image != null) {
			return image;
		} else {
			ImageDescriptor imageDescriptorFromPlugin = imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
			image = imageDescriptorFromPlugin.createImage();
			JFaceResources.getImageRegistry().put(imageFilePath, image);
			return image;
		}
	}
	
	/**
	 * 懒加载的方式添加Image资源的处理
	 * @param imageFilePath
	 * @return
	 */
	public static ImageDescriptor imageDescriptorFromPlugin(String imageFilePath) {
		return imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
	}

	public static IRuleConfig getRuleConfig() {
		ServiceReference<IRuleConfig> serviceReference = getContext().getServiceReference(IRuleConfig.class);
		IRuleConfig ruleConfig = getContext().getService(serviceReference);
		if(ruleConfig == null) {
			throw new RuleBaseRuntimeException("规则配置: IRuleConfig 服务未发现!");
		}
		return ruleConfig;
	}
	
}
