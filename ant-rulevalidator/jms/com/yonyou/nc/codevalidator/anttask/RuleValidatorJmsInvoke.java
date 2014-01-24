package com.yonyou.nc.codevalidator.anttask;

import javax.jms.JMSException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * ����ϵͳ�е�jms invoke��, Ĭ�ϲ����й�����֤
 * 
 * @author mazhqa
 * @since V2.9
 */
public class RuleValidatorJmsInvoke extends Task {

	private boolean needValidator = false;

	private String brokerUrl;
	private String queueName;

	private String codePath;
	private String ncHome;
	private String dataSourceName;
	private String executePeriod;
	private String productCode;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public String getNcHome() {
		return ncHome;
	}

	public void setNcHome(String ncHome) {
		this.ncHome = ncHome;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean isNeedValidator() {
		return needValidator;
	}

	public void setNeedValidator(boolean needValidator) {
		this.needValidator = needValidator;
	}

	public String getExecutePeriod() {
		return executePeriod;
	}

	public void setExecutePeriod(String executePeriod) {
		this.executePeriod = executePeriod;
	}

	@Override
	public void execute() throws BuildException {
		if (isNeedValidator()) {
			if (isBlank(brokerUrl)) {
				throw new BuildException("brokerUrl ��������Ϊ��!");
			}
			if (isBlank(queueName)) {
				throw new BuildException("queueName ��������Ϊ��!");
			}
			if (isBlank(codePath)) {
				throw new BuildException("codePath ��������Ϊ��!");
			}
			if (isBlank(executePeriod)) {
				throw new BuildException("executePeriod ��������Ϊ��!");
			}
			if (isBlank(productCode)) {
				throw new BuildException("productCode ��������Ϊ��!");
			}
			System.out.println("�첽���й�����֤��ʼ...");
			RuleValidatorMessageObject ruleValidatorMessageObject = new RuleValidatorMessageObject(codePath, ncHome,
					dataSourceName, executePeriod, productCode);
			try {
				JmsSender.sendMapMessage(brokerUrl, queueName, ruleValidatorMessageObject);
			} catch (JMSException e) {
				e.printStackTrace();
			}
			System.out.println("�첽���й�����֤����...");
		} else {
			System.out.println("�����й�����֤...");
		}
	}

	public boolean isBlank(String string) {
		return string == null || string.trim().length() == 0;
	}

}
