package ncmdp.wizard;
import java.lang.reflect.Method;

import ncmdp.exporttocode.wizard.DestDirectoryPage;
import ncmdp.model.Service;
import ncmdp.tool.ClassTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

public class GenWSDLWizard extends Wizard {
	private DestDirectoryPage destDirectoryPage = null;
	private Service service = null;
	private IProject project = null;
	public GenWSDLWizard(Service service, String defaultDestDirPath,
			IProject project) {
		super();
		this.service = service;
		this.project = project;
		destDirectoryPage = new DestDirectoryPage(DestDirectoryPage.class.getCanonicalName(), defaultDestDirPath, "目标目录选择页", null);

	}
	boolean finished = false;
	private Throwable exce = null;

	@Override
	public boolean performFinish() {
		try {
			finished = false;
			Job job = new Job("生成WSDL文件") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					int max = 30;
					monitor.beginTask("开始生成WSDL文件", max);
					int count = 0;
					while (!finished && !monitor.isCanceled()) {
						count++;
						if (count > max) {
							monitor.beginTask("开始生成WSDL文件", max);
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
							String msg = NCMDPTool.getExceptionRecursionError(exce);
							result = new Status(Status.ERROR,"NCMDP plugin",IStatus.ERROR,msg,exce);
						}
					}
					return result;
				}

			};

			String clsName = "nc.uap.ws.generator.WsGenerator";
			String methodName = "generateWSDLBySEI";
			final Class<?> cls = ClassTool.loadClass(clsName, project);
			final Method m = cls.getMethod(methodName, new Class[] {
					String.class, String.class });
			final String destDir = destDirectoryPage.getDestDirPath();
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					try { 
						exce = null;
//						Thread.currentThread().setContextClassLoader(ClassTool.getURLClassLoader(project));
						m.invoke(cls, new Object[] { service.getFullClassName(), destDir });
					} catch (Throwable e) {
						finished = true;
						exce = e;
						MDPLogger.error(e.getMessage(), e);

					} finally {
						finished = true;
					}
				}
			});
			job.setUser(true);
			job.schedule();
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(), e);
			MessageDialog.openInformation(getShell(), "提示", NCMDPTool
					.getExceptionRecursionError(e));
		}
		return true;
	}
	@Override
	public void addPages() {
		addPage(destDirectoryPage);
	}

	@Override
	public boolean canFinish() {
		IWizardPage currPage = this.getContainer().getCurrentPage();
		if (currPage instanceof DestDirectoryPage) {
			return true;
		}

		return false;
	}
}
