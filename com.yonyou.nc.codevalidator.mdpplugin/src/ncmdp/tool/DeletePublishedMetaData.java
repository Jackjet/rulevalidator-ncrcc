package ncmdp.tool;

import java.io.File;
import java.lang.reflect.Method;

import ncmdp.util.MDPLogger;

import org.eclipse.core.internal.runtime.RuntimeLog;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

/**
 * 删除发布的元数据
 * 
 * @author dingxm
 * 
 */
public class DeletePublishedMetaData {
	private boolean finished = false;
	private Exception exce = null;

	public void deletePublishedMetaData(final String compId,
			final int versiontype, final String industry, final boolean isIncreased,
			File file,IProject project) {
		if (project != null) {
			try {
				finished = false;
				final String fileName = file.getName();
				String clsName = "nc.md.persist.designer.service.PublishServiceHelper";
				final Class<?> cls = ClassTool.loadClass(clsName, project);
				final Method m = cls.getMethod("deleteComponentAndChildren", 
						new Class[] { String.class, int.class, String.class,
								boolean.class });
				Job job = new Job(Messages.DeletePublishedMetaData_2) {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						int max = 30;
						monitor.beginTask(Messages.DeletePublishedMetaData_3
								+ fileName, max);
						int count = 0;

						while (!finished && !monitor.isCanceled()) {
							count++;
							if (count > max) {
								monitor.beginTask(
										Messages.DeletePublishedMetaData_4 + fileName, max);
								count = 0;
							}
							monitor.worked(1);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								MDPLogger.error(e.getMessage(), e);
							}
						}
						monitor.done();
						IStatus result = Status.OK_STATUS;
						if (!monitor.isCanceled()) {
							if (exce != null) {
								String msg = NCMDPTool
										.getExceptionRecursionError(exce);
								result = new Status(Status.ERROR,
										"NCMDP plugin", IStatus.ERROR, msg, exce);
							} else {
								String msg = Messages.DeletePublishedMetaData_6;
								result = new Status(Status.INFO,
										"NCMDP plugin", IStatus.OK, msg, null); 
								RuntimeLog.log(result);
							}
						}
						return result;
					}
				};
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						try {
							exce = null;
//							Thread.currentThread().setContextClassLoader(
//									cls.getClassLoader());
							m.invoke(cls, new Object[] { compId, versiontype,
									industry, isIncreased });
						} catch (Exception e) {
							finished = true;
							exce = e;
							e.printStackTrace();
						} finally {
							finished = true;
						}
					}
				});
				job.setUser(true);
				job.schedule();
			} catch (Exception e) {
				e.printStackTrace();
				NCMDPTool.showHintMsg(Display.getCurrent().getActiveShell(),
						Messages.DeletePublishedMetaData_8,
						NCMDPTool.getExceptionRecursionError(e));
			}
		}
	}

}
