package com.yonyou.nc.codevalidator.rule;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	private static BundleContext bundleContext;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.bundleContext = null;
	}
	
	public static BundleContext getBundleContext() {
		return bundleContext;
	}

}
