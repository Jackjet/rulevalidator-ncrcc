package ncmdp.exporttocode.wizard;

import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import ncmdp.model.Constant;
import ncmdp.tool.ClassTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.MDPLogger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class ExportToCodeWizard extends Wizard {
	private DestDirectoryPage destDirectoryPage = null;

	private String cellId = null;

	private String version = null;

	private String codeStyle = null;

	private IProject project = null;

	private String cellType = null;

	public ExportToCodeWizard(String cellId, String version,
			String defaultDestDirPath, IProject project, String codeStyle,
			String cellType) {
		super();
		this.cellId = cellId;
		this.version = version;
		destDirectoryPage = new DestDirectoryPage(
				DestDirectoryPage.class.getCanonicalName(), defaultDestDirPath,
				"TarGet Dir Selected", null);
		this.project = project;
		this.codeStyle = codeStyle;
		this.cellType = cellType;
	}

	boolean finished = false;

	private Exception excepte = null;

	@Override
	public boolean performFinish() {
		try {
			finished = false;
			Job job = new Job("generate java resource") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					int max = 30;
					monitor.beginTask("begin to generate java resource", max);
					int count = 0;
					while (!finished && !monitor.isCanceled()) {
						count++;
						if (count > max) {
							monitor.beginTask(
									"begin to generate java resource", max);
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

					// 刷新当前路径 2012-08-09
					try {
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
						// e.printStackTrace();
					}

					IStatus result = Status.OK_STATUS;
					if (!monitor.isCanceled()) {
						if (excepte != null) {
							String msg = NCMDPTool
									.getExceptionRecursionError(excepte);
							result = new Status(Status.ERROR, "NCMDP plugin",
									0, msg, excepte);
						}
					}
					return result;
				}

			};

			String clsName = "nc.md.gen.CodeGenerator";
			String methodName = null;
			if (Constant.CELL_EMTITY.endsWith(cellType)) {
				if (Constant.GENCODE_STYLE_STD.equals(codeStyle)) {
					methodName = "generateCodeStdByBeanID";
				} else if (Constant.GENCODE_STYLE_DEF.endsWith(codeStyle)) {
					clsName = "nc.pubitf.pubapp.codegenerator.MetaDataVOCodeGenerator";
					methodName = "generateVOJavaCode";
				} else {
					methodName = "generateCodeByBeanID";
				}
			} else if (Constant.CELL_OPINTERFACE.endsWith(cellType)) {
				methodName = "generateOpItfCodeByItfID";
			}
			final boolean isUserDef = "nc.pubitf.pubapp.codegenerator.MetaDataVOCodeGenerator"
					.equalsIgnoreCase(clsName);
			Class tcl = null;
//			if (isUserDef) {
//				tcl = ClientClassLoaderService.getClassLoader().loadClass(
//						clsName);
//			} else {
				tcl = ClassTool.loadClass(clsName, project);
//			}
			final Method m = tcl.getMethod(methodName, new Class[] {
					String.class, String.class, String.class });
			final Class cls = tcl;

			final String destDir = destDirectoryPage.getDestDirPath();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						excepte = null;

						if (isUserDef) {
//							IUAPAuthService service = UAPAuthServiceFactory
//									.getService();
//							Class tokenCls = ClientClassLoaderService
//									.getClassLoader()
//									.loadClass(
//											"nc.bs.framework.comn.NetStreamContext");
//							Method tokenMe = tokenCls.getMethod("setToken",
//									new Class[] { byte[].class });
//							Method resetToken = tokenCls.getMethod("resetAll",
//									null);
//							byte[] token = service.getToken();
//							tokenMe.invoke(tokenCls, token);
//							m.invoke(cls, new Object[] { cellId, version,
//									destDir });
//							service.releaseToken();
//							resetToken.invoke(tokenCls, null);
						} else {
							m.invoke(cls, new Object[] { cellId, version,
									destDir });
						}

					} catch (Exception e) {
						finished = true;
						excepte = e;
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
