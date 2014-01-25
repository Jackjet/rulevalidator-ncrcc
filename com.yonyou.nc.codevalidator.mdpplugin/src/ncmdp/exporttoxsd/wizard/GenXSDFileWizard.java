package ncmdp.exporttoxsd.wizard;

import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import ncmdp.exporttocode.wizard.DestDirectoryPage;
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

public class GenXSDFileWizard extends Wizard {
	private DestDirectoryPage destDirectoryPage = null;
	private String cellId = null;
	private IProject project = null;

	public GenXSDFileWizard(String cellId, String defaultDestDirPath,
			IProject project) {
		super();
		this.cellId = cellId;
		this.project = project;
		destDirectoryPage = new DestDirectoryPage(
				DestDirectoryPage.class.getCanonicalName(), defaultDestDirPath,
				"select the column", null);// "目标目录选择页"

	}

	boolean finished = false;
	private Exception exce = null;

	@Override
	public boolean performFinish() {
		try {
			finished = false;
			Job job = new Job("generate XSD file") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					int max = 30;
					monitor.beginTask("begin to generate XSD file", max);
					int count = 0;
					while (!finished && !monitor.isCanceled()) {
						count++;
						if (count > max) {
							monitor.beginTask("begin to generate XSD file", max);
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
							result = new Status(Status.ERROR, "NCMDP plugin",
									IStatus.ERROR, msg, exce);
						}
					}
					return result;
				}

			};

			String clsName = "nc.uap.ws.generator.WsGenerator";
			String methodName = "generateXSDByBeanID";
			final Class cls = ClassTool.loadClass(clsName, project);
			final Method m = cls.getMethod(methodName, new Class[] {
					String.class, String.class });
			final String destDir = destDirectoryPage.getDestDirPath();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						exce = null;
						m.invoke(cls, new Object[] { cellId, destDir });
					} catch (Exception e) {
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
			MessageDialog.openInformation(getShell(), "note",
					NCMDPTool.getExceptionRecursionError(e));
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
