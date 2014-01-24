package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;

/**
 * 系统运行时上下文，其中包括系统的运行时状态信息，这些信息在该运行时是不能改变的
 * 
 * @author mazhqa
 * @since V2.6
 */
public class SystemRuntimeContext {

	private final String globalConfigFilePath;
	private final String globalExportFilePath;
	private final String globalLogFilePath;
	private final String globalLogLevel;
	private final ExecutePeriod executePeriod;
	
	public SystemRuntimeContext(String globalConfigFilePath, String globalExportFilePath, String globalLogFilePath, String globalLogLevel, ExecutePeriod executePeriod) {
		super();
		this.globalConfigFilePath = globalConfigFilePath;
		this.globalExportFilePath = globalExportFilePath;
		this.globalLogFilePath = globalLogFilePath;
		this.globalLogLevel = globalLogLevel;
		this.executePeriod = executePeriod;
	}
	
	public String getGlobalLogLevel() {
		return globalLogLevel;
	}

	public String getGlobalConfigFilePath() {
		return globalConfigFilePath;
	}

	public String getGlobalExportFilePath() {
		return globalExportFilePath;
	}
	
	public String getGlobalLogFilePath() {
		return globalLogFilePath;
	}
	
	public ExecutePeriod getExecutePeriod() {
		return executePeriod;
	}

}
