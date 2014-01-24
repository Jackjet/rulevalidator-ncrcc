package com.yonyou.nc.codevalidator.persistence.dao;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.yonyou.nc.codevalidator.persistence.dao.impl.RuleDefinitionInitializerImpl;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class Activator implements BundleActivator {

	private static BundleContext context;

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
		Activator.context = bundleContext;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
					Logger.info("线程中执行更新开始");
					IRuleDefinitionInitializer initiDefinitionInitializer = new RuleDefinitionInitializerImpl();
					initiDefinitionInitializer.initialize();
					Logger.info("线程中执行更新完毕");
				} catch (RuleDaoException e) {
					Logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
