package com.yonyou.nc.codevalidator.anttask;

import javax.jms.JMSException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * 规则系统中的jms invoke端, 默认不进行规则验证
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
				throw new BuildException("brokerUrl 不能设置为空!");
			}
			if (isBlank(queueName)) {
				throw new BuildException("queueName 不能设置为空!");
			}
			if (isBlank(codePath)) {
				throw new BuildException("codePath 不能设置为空!");
			}
			if (isBlank(executePeriod)) {
				throw new BuildException("executePeriod 不能设置为空!");
			}
			if (isBlank(productCode)) {
				throw new BuildException("productCode 不能设置为空!");
			}
			System.out.println("异步进行规则验证开始...");
			RuleValidatorMessageObject ruleValidatorMessageObject = new RuleValidatorMessageObject(codePath, ncHome,
					dataSourceName, executePeriod, productCode);
			try {
				JmsSender.sendMapMessage(brokerUrl, queueName, ruleValidatorMessageObject);
			} catch (JMSException e) {
				e.printStackTrace();
			}
			System.out.println("异步进行规则验证结束...");
		} else {
			System.out.println("不进行规则验证...");
		}
	}

	public boolean isBlank(String string) {
		return string == null || string.trim().length() == 0;
	}

}
