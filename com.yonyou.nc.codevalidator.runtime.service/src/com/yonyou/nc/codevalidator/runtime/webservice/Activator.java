package com.yonyou.nc.codevalidator.runtime.webservice;

import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.runtime.IRuleRuntimeExecutor;
import com.yonyou.nc.codevalidator.runtime.jms.JmsRuleExecutor;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private JmsRuleExecutor jmsRuleExecutor;

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
					Properties properties = new Properties();
					properties.load(Activator.class.getResourceAsStream("/config/jmsconfig.properties"));
					String brokerUrl = (String) properties.get("brokerUrl");
					String queueName = (String) properties.get("queueName");
					int numberOfConsumers = Integer.parseInt((String) properties.get("numberOfConsumers"));
					jmsRuleExecutor = new JmsRuleExecutor(brokerUrl, queueName);
					jmsRuleExecutor.setNumberOfConsumers(numberOfConsumers);
					jmsRuleExecutor.startListen();
				} catch (IOException e) {
					throw new RuleBaseRuntimeException(e);
				} catch (JMSException e) {
					throw new RuleBaseRuntimeException(e);
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
		jmsRuleExecutor.stopListener();
		Activator.context = null;
	}

	public static IRuleRuntimeExecutor getRuleRuntimeExecutor() {
		ServiceReference<IRuleRuntimeExecutor> serviceReference = context
				.getServiceReference(IRuleRuntimeExecutor.class);
		return context.getService(serviceReference);
	}

}
