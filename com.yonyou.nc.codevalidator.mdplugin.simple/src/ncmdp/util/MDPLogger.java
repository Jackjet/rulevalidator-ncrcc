package ncmdp.util;

import com.yonyou.nc.codevalidator.sdk.log.Logger;


/**
 * �����eclipse��־
 * @author wangxmn
 *
 */
public class MDPLogger {

	/**
	 * ��¼error��Ϣ
	 * @param message
	 * @param e
	 */
	public static void error(String message,Throwable e){
		Logger.error(message, e);
	}
	
	public static void error(String message){
		Logger.error(message);
	}
	
	public static void warning(String message){
		Logger.warn(message);
	}
	
	public static void info(String message){
		Logger.info(message);
	}
	
	public void OK(String message){
		Logger.info(message);
	}
}
