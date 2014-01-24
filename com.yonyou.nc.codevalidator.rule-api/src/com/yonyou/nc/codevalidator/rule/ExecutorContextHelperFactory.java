package com.yonyou.nc.codevalidator.rule;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;

/**
 * ִ�������İ���������
 * @author mazhqa
 * @since V2.3
 */
public class ExecutorContextHelperFactory {
	

	/**
	 * �õ�ָ����������ִ���࣬��������ִ�й���
	 * @return
	 */
	public static IExecutorContextHelper getExecutorContextHelper() {
		BundleContext bundleContext = Activator.getBundleContext();
		ServiceReference<IExecutorContextHelper> serviceReference = bundleContext.getServiceReference(IExecutorContextHelper.class);
		IExecutorContextHelper executorContextHelper = bundleContext.getService(serviceReference);
		if(executorContextHelper == null){
			throw new RuleBaseRuntimeException("ִ�������İ����಻����!");
		}
		return executorContextHelper;
	}
}
