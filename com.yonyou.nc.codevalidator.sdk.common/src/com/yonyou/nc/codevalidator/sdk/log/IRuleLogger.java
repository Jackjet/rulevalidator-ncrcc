package com.yonyou.nc.codevalidator.sdk.log;

/**
 * @author mazhqa
 * 
 */
public interface IRuleLogger {
	
	/**
	 * 在日志执行前进行重新的初始化操作
	 */
	void init();

	/**
	 * 日志调试信息
	 * 
	 * @param msg
	 */
	void debug(String msg);

	/**
	 * 日志普通信息
	 * 
	 * @param msg
	 */
	void info(String msg);

	/**
	 * 日志警告信息
	 * 
	 * @param msg
	 * @param throwable
	 */
	void warn(String msg, Throwable throwable);

	/**
	 * 日志警告信息
	 * 
	 * @param msg
	 */
	void warn(String msg);

	/**
	 * 日志错误信息
	 * 
	 * @param msg
	 * @param throwable
	 */
	void error(String msg, Throwable throwable);

	/**
	 * 日志错误信息
	 * 
	 * @param msg
	 */
	void error(String msg);

}
