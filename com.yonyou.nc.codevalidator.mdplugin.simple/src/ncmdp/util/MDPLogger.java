package ncmdp.util;

import com.yonyou.nc.codevalidator.sdk.log.Logger;


/**
 * 插件的eclipse日志
 * @author wangxmn
 *
 */
public class MDPLogger {

	/**
	 * 记录error信息
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
