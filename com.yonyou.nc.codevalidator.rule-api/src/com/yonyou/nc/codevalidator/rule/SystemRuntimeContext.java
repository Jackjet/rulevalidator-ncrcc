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

	/**
	 * 如果该规则系统执行在5x版本下，需要支持模块下能够执行业务组件规则，否则不需要支持
	 */
	private boolean executeLevelIn5x = false;

	public SystemRuntimeContext(String globalConfigFilePath, String globalExportFilePath, String globalLogFilePath,
			String globalLogLevel, ExecutePeriod executePeriod) {
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

	public boolean isExecuteLevelIn5x() {
		return executeLevelIn5x;
	}

	public void setExecuteLevelIn5x(boolean executeLevelIn5x) {
		this.executeLevelIn5x = executeLevelIn5x;
	}

}
