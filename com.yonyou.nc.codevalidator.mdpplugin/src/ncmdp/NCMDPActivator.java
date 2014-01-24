package ncmdp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

//import com.yonyou.uap.studio.lic.UAPStudioProduct;

/**
 * The activator class controls the plug-in life cycle
 * 启动类
 */
public class NCMDPActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "NCMDP";

	// The shared instance
	private static NCMDPActivator plugin;

	/**
	 * The constructor
	 */
	public NCMDPActivator() {
		plugin = this;
	}

	public static String getPluginID(){
		return PLUGIN_ID;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
//		 StudioProduct.BIZ_MODELING.validateLicenseThrowException();
//		 UAPStudioProduct.BIZ_MODELING.validateLicenseThrowException();
		super.start(context);
		//在当前打开的工作台页面中添加页面监听器
//		getWorkbench().getActiveWorkbenchWindow().addPageListener(
//				new IPageListener() {
//
//					public void pageActivated(IWorkbenchPage page) {
//						LocatorAction.doLocator();
//					}
//
//					public void pageClosed(IWorkbenchPage page) {
//					}
//
//					public void pageOpened(IWorkbenchPage page) {
//						page.addPartListener(new PartListener());
//
//					}
//
//				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static NCMDPActivator getDefault() {
		return plugin;
	}

}
