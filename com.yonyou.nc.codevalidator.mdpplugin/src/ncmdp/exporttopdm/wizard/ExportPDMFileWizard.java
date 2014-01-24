package ncmdp.exporttopdm.wizard;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.widgets.TreeItem;
/**
 * 生成pdm文件
 * @author wangxmn
 *
 */
public class ExportPDMFileWizard extends Wizard {
	private DestPdmDirectoryPage destDirectoryPage = null;
	private List<TreeItem> al = null;
	private IProject project = null;//当前工程
	private boolean isOpen = false;
	boolean finished = false;
	private Exception exce = null;
	private JGraph currentModel = null;

	public ExportPDMFileWizard(List<TreeItem> al, String defaultDestDirPath,
			IProject project) {
		super();
		this.al = al;
		this.project = project;
		destDirectoryPage = new DestPdmDirectoryPage(
				DestPdmDirectoryPage.class.getCanonicalName(),
				defaultDestDirPath, Messages.PDM_DIR_PAGE_0, null);// "目标目录选择页"
	}

	public ExportPDMFileWizard(JGraph graph, String defaultDestDirPath,
			IProject project) {
		super();
		this.al = new ArrayList<TreeItem>();
		this.project = project;
		this.currentModel = graph;
		destDirectoryPage = new DestPdmDirectoryPage(
				DestPdmDirectoryPage.class.getCanonicalName(),
				defaultDestDirPath, Messages.PDM_DIR_PAGE_0, null);// "目标目录选择页"
	}
	
	public boolean doPdmFile() {
		if (al.size() == 0 && this.currentModel==null)
			return true;
		try {
			finished = false;
			Job job = new Job("generate PDM file") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					int max = 30;
					monitor.beginTask("begin to generate PDM file", max);
					int count = 0;
					while (!finished && !monitor.isCanceled()) {
						count++;
						if (count > max) {
							monitor.beginTask("begin to generate PDM file", max);
							count = 0;
						}
						monitor.worked(1);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							MDPLogger.error(e.getMessage(),e);
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
			String clsName = "nc.md.persist.designer.service.PublishServiceHelper";//
			final Class<?> cls = ClassTool.loadClass(clsName, project);
			final Method m = cls.getMethod("generatePDMByComponent",
					new Class[] { Map.class, Map.class, String.class });
			final HashMap<String, String> hm = new HashMap<String, String>();
			final HashMap<String, String> hmVersion = new HashMap<String, String>();
			if(al.size()>0&&currentModel==null){
				dealMDPExplorerView(hm,hmVersion);
			}else if(currentModel!=null&&al.size()==0){
				dealEditor(hm,hmVersion);
			}

			final String destDir = destDirectoryPage.getDestDirPath();
			getShell().getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					try {
						exce = null;
						m.invoke(cls, new Object[] { hm, hmVersion, destDir });
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
			if (isOpen) {
				//修改后可以打开带空格的文件
				Runtime.getRuntime().exec("cmd /c start " + "\"\" " + "\""+destDir+ "\"");
			}
		} catch (Exception e) {
			MDPLogger.error(e.getMessage(),e);
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
	public boolean performFinish() {
		String destDir = destDirectoryPage.getDestDirPath();
		if (destDir == null || destDir.trim().length() == 0)
			return false;
		return doPdmFile();
	}

	@Override
	public boolean canFinish() {
		IWizardPage currPage = this.getContainer().getCurrentPage();
		if (currPage instanceof DestPdmDirectoryPage) {
			return true;
		}
		return false;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	private void dealMDPExplorerView(HashMap<String, String> hm,
			HashMap<String, String> hmVersion){
		for (int i = 0; i < al.size(); i++) {
			TreeItem item = (TreeItem) al.get(i);
			if (item instanceof MDPFileTreeItem) {
				MDPFileTreeItem ti = (MDPFileTreeItem) item;
				JGraph graph = ti.getGraph();
				hm.put(graph.getId(), "test.pdm");//纯粹为了和中间件中的方法集合
				hmVersion.put(graph.getId(), graph.getVersion() + "");
			}
		}
	}
	
	private void dealEditor(HashMap<String, String> hm,
			HashMap<String, String> hmVersion){
		hm.put(currentModel.getId(), "test.pdm");
		hmVersion.put(currentModel.getId(), currentModel.getVersion() + "");
	}
}
