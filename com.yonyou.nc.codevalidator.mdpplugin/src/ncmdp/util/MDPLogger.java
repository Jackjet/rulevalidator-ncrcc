package ncmdp.util;

import ncmdp.NCMDPActivator;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * 插件的eclipse日志
 * @author wangxmn
 *
 */
public class MDPLogger {

	private static ILog log = NCMDPActivator.getDefault().getLog();
	
	/**
	 * 记录error信息
	 * @param message
	 * @param e
	 */
	public static void error(String message,Throwable e){
		log.log(new Status(IStatus.ERROR,NCMDPActivator.getPluginID(),message,e));
	}
	
	public static void error(String message){
		log.log(new Status(IStatus.ERROR,NCMDPActivator.getPluginID(),message));
	}
	
	public static void warning(String message){
		log.log(new Status(IStatus.WARNING,NCMDPActivator.getPluginID(),message));
	}
	
	public static void info(String message){
		log.log(new Status(IStatus.INFO,NCMDPActivator.getPluginID(),message));
	}
	
	public void OK(String message){
		log.log(new Status(IStatus.OK,NCMDPActivator.getPluginID(),message));
	}
}
