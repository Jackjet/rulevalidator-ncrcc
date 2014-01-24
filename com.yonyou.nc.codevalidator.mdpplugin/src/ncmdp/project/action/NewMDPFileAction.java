package ncmdp.project.action;

import java.io.File;

import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.ui.industry.IndustryNewFileDilog;
import ncmdp.ui.tree.mdptree.IMDPTreeNode;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新建业务实体组件文件
 * @author wangxmn
 *
 */
public class NewMDPFileAction extends Action {

	public NewMDPFileAction() {
		super(Messages.NewMDPFileAction_0);
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		if (view == null)
			return;
		if(MDPUtil.getDevCode()==null){
			return;
		}
		Tree tree = view.getExplorerTree();
		TreeItem[] selTIs = tree.getSelection();
		Shell shell = new Shell(Display.getCurrent());
		try {
			if (selTIs == null || selTIs.length == 0)
				throw new Exception(Messages.NewMDPFileAction_1);
			TreeItem selTI = selTIs[0];
			if (!(selTI instanceof IMDPTreeNode)){
				MDPLogger.error("当前节点不为元数据树节点！");
				return;
			}
			File parentFile = ((IMDPTreeNode) selTI).getFile();
			if (parentFile.isFile())
				throw new Exception(Messages.NewMDPFileAction_2);
			IndustryNewFileDilog dilog = new IndustryNewFileDilog(shell);
			if (dilog.open() == Window.OK) {
				String fileName = dilog.getFileName();
				if (fileName != null && fileName.trim().length() > 0) {
					fileName = fileName.trim();
					if (!fileName.toLowerCase().endsWith(
							Constant.MDFILE_SUFFIX)) {
						fileName += Constant.MDFILE_SUFFIX;
					}
					File f = new File(parentFile, fileName);
					if (!f.exists()) {
						JGraph graph = getNewGraph((IMDPTreeNode) selTI, f);
						String versionType = dilog.getVersiontype();
						String programCode = dilog.getProgramCode();
						graph.setIndustry(dilog.getIndustry());
						graph.setVersionType(versionType);
						graph.setProgramCode(programCode);
						MDPFileTreeItem fileTreeItem = view.addFileTreeNode(
								selTI, fileName, graph);//新增实体节点
						fileTreeItem.setGraph(graph);
						view.openEditorTemp(fileTreeItem);
					} else {
						throw new Exception(Messages.NewMDPFileAction_3 + f.getPath());
					}
				}
			}
		} catch (Exception e) {
			String title = Messages.NewMDPFileAction_4;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	/**
	 * 根据元数据树节点和新建的实体文件创建一个model
	 * @param item
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private JGraph getNewGraph(IMDPTreeNode item, File f) throws Exception {
		JGraph graph = JGraph.getBMFJGraph();
		graph.setNameSpace(item.getModuleName());
		graph.setOwnModule(item.getModuleName());
		String name = f.getName();
		name = name.substring(0,
				name.length() - Constant.MDFILE_SUFFIX.length());
		graph.setName(name);
		graph.setDisplayName(name);
		return graph;
	}
}
