package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;

/**
 * ϵͳ����ʱ�����ģ����а���ϵͳ������ʱ״̬��Ϣ����Щ��Ϣ�ڸ�����ʱ�ǲ��ܸı��
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
	 * ����ù���ϵͳִ����5x�汾�£���Ҫ֧��ģ�����ܹ�ִ��ҵ��������򣬷�����Ҫ֧��
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
