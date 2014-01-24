package com.yonyou.nc.codevalidator.rule;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

/**
 * 执行上下文帮助工厂类
 * @author mazhqa
 * @since V2.3
 */
public class ExecutorContextHelperFactory {
	

	/**
	 * 得到指定的上下文执行类，用来监视执行过程
	 * @return
	 */
	public static IExecutorContextHelper getExecutorContextHelper() {
		BundleContext bundleContext = Activator.getBundleContext();
		ServiceReference<IExecutorContextHelper> serviceReference = bundleContext.getServiceReference(IExecutorContextHelper.class);
		IExecutorContextHelper executorContextHelper = bundleContext.getService(serviceReference);
		if(executorContextHelper == null){
			throw new RuleBaseRuntimeException("执行上下文帮助类不存在!");
		}
		return executorContextHelper;
	}
}
