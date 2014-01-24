/*
 * Created on 2005-8-11
 * @author 何冠宇
 */
package com.yonyou.nc.codevalidator.runtime.plugin.log;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;

import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.sdk.log.IRuleLogger;

/**
 * @author liuyuan
 */
public class RuntimePluginLogger implements IRuleLogger {

	public static void log(IStatus status) {
		PrintStream printStream = chooseStream(status);
		if (printStream != null) {
			RuntimePluginEclipseLog log = new RuntimePluginEclipseLog(new PrintWriter(printStream));
			RuntimePlatformLogWriter logWriter = new RuntimePlatformLogWriter(log);
			logWriter.logging(status, Activator.PLUGIN_ID);
		}
		ResourcesPlugin.getPlugin().getLog().log(status);
	}

	/**
	 * 根据IStatus的不同，选择不同的流进行输出(保证INFO|ERROR的信息颜色不同)
	 */
	private static PrintStream chooseStream(IStatus status) {
		// 得到默认控制台管理器
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		// 得到所有的控制台实例
		IConsole[] existing = manager.getConsoles();
		RuntimeConsole eipConsole = null;
		if (existing != null && existing.length > 0) {
			for (IConsole console : existing) {
				if (console instanceof RuntimeConsole) {
					eipConsole = (RuntimeConsole) console;
				}
			}
		}
		if (eipConsole != null) {
			switch (status.getSeverity()) {
			case IStatus.ERROR: 
				return eipConsole.getErrorPrintStream();
			default: 
				return eipConsole.getInfoPrintStream();
			}
		}
		return null;
	}

	public static void logError(String message) {
		log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, message, null));
	}

	public static void logInfo(String message) {
		log(new Status(IStatus.INFO, Activator.PLUGIN_ID, IStatus.INFO, message, null));
	}

	public static void logException(Throwable e, String message) {
		logException(e, null, message);
	}

	public static void logException(Throwable e, final String title, String message) {
		if (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		IStatus status = null;
		if (e instanceof CoreException)
			status = ((CoreException) e).getStatus();
		else {
			if (message == null) {
				message = e.getMessage();
			}
			if (message == null) {
				message = e.toString();
			}
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, message, e);
		}
		// ResourcesPlugin.getPlugin().getLog().log(status);
		log(status);
		Display display = Display.getDefault();
		final IStatus fstatus = status;
		display.asyncExec(new Runnable() {
			public void run() {
				ErrorDialog.openError(null, title, null, fstatus);
			}
		});
	}

	public static void logException(Throwable e) {
		logException(e, null, null);
	}

	public static void log(Throwable e) {
		if (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		IStatus status = null;
		if (e instanceof CoreException) {
			status = ((CoreException) e).getStatus();
		} else {
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
		}
		log(status);
	}

	@Override
	public void debug(String msg) {
		// log(new Status(IStatus.INFO, Activator.PLUGIN_ID, IStatus.INFO, msg,
		// null));
		// plugin not support debug in console.
	}

	@Override
	public void info(String msg) {
		log(new Status(IStatus.INFO, Activator.PLUGIN_ID, IStatus.INFO, msg, null));
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, IStatus.WARNING, msg, throwable));
	}

	@Override
	public void warn(String msg) {
		log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, IStatus.WARNING, msg, null));
	}

	@Override
	public void error(String msg, Throwable throwable) {
		log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, msg, throwable));
	}

	@Override
	public void error(String msg) {
		log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, msg, null));
	}

	@Override
	public void init() {
		
	}
}
