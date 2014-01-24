package com.yonyou.nc.codevalidator.sdk.log;

public class Logger {

	private static IRuleLogger ruleLogger = DefaultRuleDelegatorLoggerImpl.getInstance();

	public static void init() {
		ruleLogger.init();
	}
	
	public static void debug(String msg) {
		ruleLogger.debug(msg);
	}

	public static void info(String msg) {
		ruleLogger.info(msg);
	}

	public static void warn(String msg, Throwable throwable) {
		ruleLogger.warn(msg, throwable);
	}

	public static void warn(String msg) {
		ruleLogger.warn(msg);
	}

	public static void error(String msg, Throwable throwable) {
		ruleLogger.error(msg, throwable);
	}

	public static void error(String msg) {
		ruleLogger.error(msg);
	}
	
	public static void setThreadState(String s) {

	}

}
