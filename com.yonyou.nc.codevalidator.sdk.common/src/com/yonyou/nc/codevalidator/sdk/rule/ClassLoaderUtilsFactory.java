package com.yonyou.nc.codevalidator.sdk.rule;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.sdk.common.Activator;

/**
 * ClassLoader������
 * @author mazhqa
 * @since V1.0
 */
public class ClassLoaderUtilsFactory {

	public static IClassLoaderUtils getClassLoaderUtils() {
		BundleContext bundleContext = Activator.getBundleContext();
		ServiceReference<IClassLoaderUtils> serviceReference = bundleContext
				.getServiceReference(IClassLoaderUtils.class);
		IClassLoaderUtils classLoaderUtils = bundleContext.getService(serviceReference);
		if (classLoaderUtils == null) {
			throw new RuleBaseRuntimeException("δ�ܻ��Classloader��ʵ��");
		}
		return classLoaderUtils;
	}

}
