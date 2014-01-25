package ncmdp.exporttosql.wizard;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ncmdp.exporttocode.wizard.DestDirectoryPage;
import ncmdp.model.JGraph;
import ncmdp.tool.ClassTool;
import ncmdp.tool.NCMDPTool;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.Messages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
public class GenSqlsWizard extends Wizard {
	
	private DestDirectoryPage destDirectoryPage = null;
	private List<TreeItem> al = null;
	private IProject project = null;
	private boolean isExecSqls = false;
	private String moduleName = "";
	private JGraph currentModel = null;
	private String sqlsPath = null;
	public GenSqlsWizard(List<TreeItem> al, IProject project, boolean execSqls) {
		super();
		this.al = al;
		this.project = project;
		this.isExecSqls = execSqls;
		destDirectoryPage = new DestDirectoryPage(
				DestDirectoryPage.class.getCanonicalName(),
				NCMDPTool.getGenSqlsRootDir(), Messages.PDM_DIR_PAGE_0, null,
				true);// 目标目录选择页
	}
	
	public GenSqlsWizard(JGraph model, IProject project, String sqlPath,boolean execSqls) {
		super();
		this.al = new ArrayList<TreeItem>();
		this.currentModel = model;
		this.project = project;
		this.isExecSqls = execSqls;
		this.sqlsPath = sqlPath;
		destDirectoryPage = new DestDirectoryPage(
				DestDirectoryPage.class.getCanonicalName(),
				NCMDPTool.getGenSqlsRootDir(), Messages.PDM_DIR_PAGE_0, null,
				true);// 目标目录选择页
	}
	
	boolean finished = false;

	private Exception exce = null;

	@Override
	public void addPages() {
		addPage(destDirectoryPage);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean doGenSql(String sqlsRootDir) {
		if (al.size() == 0&&currentModel==null)
			return true;
		try {
			finished = false;
			Job job = new Job("generate sql") {
				protected IStatus run(IProgressMonitor monitor) {
					int max = 30;
					String hint = "generate sql";
					if (isExecSqls) {
						hint = "generate sql and execute";
					}
					monitor.beginTask(hint, max);
					int count = 0;
					while (!finished && !monitor.isCanceled()) {
						count++;
						if (count > max) {
							monitor.beginTask(hint, max);
							count = 0;
						}
						monitor.worked(1);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							MDPLogger.error(e.getMessage(), e);
						}
					}
					IStatus result = Status.OK_STATUS;
					monitor.done();
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
			String clsName = "nc.md.persist.designer.service.PublishServiceHelper";
			final boolean needFK = destDirectoryPage.isGenFk();
			final boolean forLocale = destDirectoryPage.forLocale();
			Class tCl = null;
//			if (isExecSqls) {
//				tCl = ClientClassLoaderService.getClassLoader().loadClass(
//						clsName);
//			} else {
				tCl = ClassTool.loadClass(clsName, project);
//			}
			final Class cls = tCl;
			final Method m = cls.getMethod("generateSQLByComponent",
					new Class[] { Map.class, Map.class, String.class,
							String.class, boolean.class, boolean.class,
							boolean.class, String.class });
			final HashMap<String, String> hm = new HashMap<String, String>();
			final HashMap<String, String> hmVersion = new HashMap<String, String>();
			if(al.size()>0&&currentModel==null){
				dealMDPExplorerView(hm,hmVersion,sqlsRootDir);
			}else if(al.size()==0&&currentModel!=null){
				sqlsRootDir = dealMDPEditor(hm,hmVersion,sqlsRootDir);
			}
			final String rootDir = sqlsRootDir;
			this.getShell().getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					try {//token权限
						exce = null;
						if (isExecSqls) {
//							IUAPAuthService service = UAPAuthServiceFactory
//									.getService();
//							Class tokenCls = ClientClassLoaderService
//									.getClassLoader().loadClass(
//											"nc.bs.framework.comn.NetStreamContext");
//							Method tokenMe = tokenCls.getMethod("setToken",
//									new Class[] { byte[].class });
//							Method resetToken = tokenCls.getMethod("resetAll",null);
//							//设置
//							byte[] token = service.getToken();
//							tokenMe.invoke(tokenCls, token);
//							m.invoke(cls, new Object[] { hm, hmVersion,
//									moduleName, rootDir, isExecSqls, needFK,
//									forLocale, "design" });
//							service.releaseToken();
//							resetToken.invoke(tokenCls, null);
						} else {
							m.invoke(cls, new Object[] { hm, hmVersion,
									moduleName, rootDir, isExecSqls, needFK,
									forLocale, "design" });
						}
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
			MessageDialog.openInformation(
					Display.getDefault().getActiveShell(), "提示",
					NCMDPTool.getExceptionRecursionError(e));
		}
		return true;

	}

	@Override
	public boolean performFinish() {
		String destDir = destDirectoryPage.getDestDirPath();
		if (destDir == null || destDir.trim().length() == 0)
			return false;
		destDir = destDir.trim().replace('\\', '/');
		return doGenSql(destDir);
	}

	@Override
	public boolean canFinish() {
		IWizardPage currPage = this.getContainer().getCurrentPage();
		if (currPage instanceof DestDirectoryPage) {
			return true;
		}
		return false;
	}
	
	private void dealMDPExplorerView(Map<String,String> hm,
			Map<String,String> hmVersion,String sqlsRootDir){
		for (int i = 0; i < al.size(); i++) {
			TreeItem item = (TreeItem) al.get(i);
			if (item instanceof MDPFileTreeItem) {
				MDPFileTreeItem ti = (MDPFileTreeItem) item;
				moduleName = ti.getModuleName();
				String path = ti.getMDFileRelativePath();
				int index = path.lastIndexOf(".");
				if (index != -1) {
					path = path.substring(0, index);
				}
				sqlsRootDir += "/" + moduleName + "/" + path;
				if (path.startsWith("/") || path.startsWith("\\")) {
					path = path.substring(1);
				}
				JGraph graph = ti.getGraph();
				hm.put(graph.getId(), path + ".sql");
				hmVersion.put(graph.getId(), graph.getVersion() + "");
			}
		}
	}
	
	private String dealMDPEditor(Map<String,String> hm,
			Map<String,String> hmVersion,String sqlsRootDir){
		moduleName = currentModel.getOwnModule();
		int index = sqlsPath.lastIndexOf(".");
		if (index != -1) {
			sqlsPath = sqlsPath.substring(0, index);
		}
		sqlsRootDir += "/" + moduleName + "/" + sqlsPath;
		if (sqlsPath.startsWith("/") || sqlsPath.startsWith("\\")) {
			sqlsPath = sqlsPath.substring(1);
		}
		hm.put(currentModel.getId(), sqlsPath + ".sql");
		hmVersion.put(currentModel.getId(), currentModel.getVersion() + "");
		return sqlsRootDir;
	}

}
