package com.yonyou.nc.codevalidator.sdk.log;

/**
 * @author mazhqa
 * 
 */
public interface IRuleLogger {
	
	/**
	 * ����־ִ��ǰ�������µĳ�ʼ������
	 */
	void init();

	/**
	 * ��־������Ϣ
	 * 
	 * @param msg
	 */
	void debug(String msg);

	/**
	 * ��־��ͨ��Ϣ
	 * 
	 * @param msg
	 */
	void info(String msg);

	/**
	 * ��־������Ϣ
	 * 
	 * @param msg
	 * @param throwable
	 */
	void warn(String msg, Throwable throwable);

	/**
	 * ��־������Ϣ
	 * 
	 * @param msg
	 */
	void warn(String msg);

	/**
	 * ��־������Ϣ
	 * 
	 * @param msg
	 * @param throwable
	 */
	void error(String msg, Throwable throwable);

	/**
	 * ��־������Ϣ
	 * 
	 * @param msg
	 */
	void error(String msg);

}
