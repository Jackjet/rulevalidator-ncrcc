package com.yonyou.nc.codevalidator.rule;

/**
 * 规则配置的一些常量，这些常量都可以在运行时以System.getProperty形式获取
 * @author mazhqa
 * @since V2.6
 */
public final class RuleConstants {

	private RuleConstants() {

	}

	public static final String GLOBAL_CONFIG_FILEPATH = "globalConfigFilePath";
	public static final String GLOBAL_EXPORT_FILEPATH = "globalExportFilePath";
	public static final String GLOBAL_LOG_FILEPATH = "logFilePath";
	public static final String GLOBAL_LOG_LEVEL = "logLevel";
	public static final String EXECUTE_LEVEL = "executeLevel";
	public static final String RUN_IN_NC_5X = "runInNc5x";
	public static final String CURRENT_SERVER_FOLDER = "currentEquinoxFolder";
	
	
	public static final String LOG_FILENAME = "ruleexecute.log";
	

}
