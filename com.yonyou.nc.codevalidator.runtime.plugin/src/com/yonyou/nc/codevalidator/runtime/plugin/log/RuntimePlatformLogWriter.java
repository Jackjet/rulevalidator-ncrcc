package com.yonyou.nc.codevalidator.runtime.plugin.log;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.framework.log.FrameworkLogEntry;

/**
 * A log writer that writes log entries.
 * <p>
 * Note that this class just provides a bridge from the old ILog interface to
 * the OSGi FrameworkLog interface.
 */
public class RuntimePlatformLogWriter {
	private final RuntimePluginEclipseLog frameworkLog;

	public RuntimePlatformLogWriter(RuntimePluginEclipseLog frameworkLog) {
		this.frameworkLog = frameworkLog;
	}

	/**
	 * @see ILogListener#logging(IStatus, String)
	 */
	public synchronized void logging(IStatus status, String plugin) {
		frameworkLog.log(getLog(status));
	}

	protected FrameworkLogEntry getLog(IStatus status) {
		Throwable t = status.getException();
		ArrayList<FrameworkLogEntry> childlist = new ArrayList<FrameworkLogEntry>();
		int stackCode = t instanceof CoreException ? 1 : 0;
		// ensure a substatus inside a CoreException is properly logged
		if (stackCode == 1) {
			IStatus coreStatus = ((CoreException) t).getStatus();
			if (coreStatus != null) {
				childlist.add(getLog(coreStatus));
			}
		}
		if (status.isMultiStatus()) {
			IStatus[] children = status.getChildren();
			for (int i = 0; i < children.length; i++) {
				childlist.add(getLog(children[i]));
			}
		}
		FrameworkLogEntry[] children = (FrameworkLogEntry[]) (childlist.size() == 0 ? null : childlist
				.toArray(new FrameworkLogEntry[childlist.size()]));
		return new FrameworkLogEntry(status.getPlugin(), status.getSeverity(), status.getCode(), status.getMessage(),
				stackCode, t, children);
	}
}
