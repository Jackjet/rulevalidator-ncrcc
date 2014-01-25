package com.yonyou.nc.codevalidator.runtime;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * User: mazhqa Date: 14-1-2
 */
public class RuleValidatorAntTask extends Task {

	private String wsUrl;
	private String methodName;
	private String ncHome;
	private String srcFolder;
	private String dataSourceName;

	@Override
	public void execute() throws BuildException {
		System.out.println("new Thread execute start!!!");
		SimpleClient.asyncExecute(wsUrl, methodName, ncHome, srcFolder, dataSourceName);
		System.out.println("new Thread execute stop!!!");
	}

	public String getWsUrl() {
		return wsUrl;
	}

	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getNcHome() {
		return ncHome;
	}

	public void setNcHome(String ncHome) {
		this.ncHome = ncHome;
	}

	public String getSrcFolder() {
		return srcFolder;
	}

	public void setSrcFolder(String srcFolder) {
		this.srcFolder = srcFolder;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
}
