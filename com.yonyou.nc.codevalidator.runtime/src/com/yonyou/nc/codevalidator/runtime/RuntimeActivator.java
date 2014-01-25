package com.yonyou.nc.codevalidator.runtime;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RuntimeActivator implements BundleActivator {

	private static BundleContext bundleContext;

	static BundleContext getContext() {
		return RuntimeActivator.bundleContext;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		RuntimeActivator.bundleContext = bundleContext;
//		String ncHome = "D:\\viewroot\\mazhqa_NC_UAP_MODULES_NEW6.3_2_int\\NC6_UAP_VOB\\NC_UAP_MODULES";
//		String projectPath = "D:\\Develop\\GitRepo-nc\\RuleValidator\\test\\rulecheck";
//		SessionRuleExecuteResult result = RuntimeExecutor.execute(ncHome, projectPath);
//		Logger.info("Ö´ÐÐ³É¹¦! " + result);
	}

	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		RuntimeActivator.bundleContext = null;
	}

}
