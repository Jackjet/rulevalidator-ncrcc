package ncmdp.project.action;

import java.io.File;

import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.ui.industry.IndustryNewFileDilog;
import ncmdp.ui.tree.mdptree.IMDPTreeNode;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPUtil;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新建业务活动文件
 * @author wangxmn
 *
 */
public class NewBpfFileAction extends Action {

	public NewBpfFileAction() {
		super(Messages.NewBpfFileAction_0);//新建业务操作组件
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		if (view == null)
			return;
		if(MDPUtil.getDevCode()==null){//判断开发者信息是否为空
			return;
		}
		Tree tree = view.getExplorerTree();// treeView.getTree();
		TreeItem[] selTIs = tree.getSelection();
		Shell shell = new Shell(Display.getCurrent());

		try {
			if (selTIs == null || selTIs.length == 0)
				throw new Exception(Messages.NewBpfFileAction_1);//请选中一个节点。
			TreeItem selTI = selTIs[0];
			if (!(selTI instanceof IMDPTreeNode))
				return;
			File parentFile = ((IMDPTreeNode) selTI).getFile();
			if (parentFile.isFile())
				throw new Exception(Messages.NewBpfFileAction_2);//不能在文件下新建文件。
			// parentFile
//			IProject project = MDPExplorerTreeView.getMDPExploerTreeView(
//					view.getSite().getPage()).getFileOwnProject(parentFile);
			IProject project = ProjectUtil.getFileOwnProject(parentFile);
			IndustryNewFileDilog dilog = new IndustryNewFileDilog(shell,
					project);

			if (dilog.open() == Window.OK) {
				String fileName = dilog.getFileName();
				if (fileName != null && fileName.trim().length() > 0) {
					fileName = fileName.trim();
					if (!fileName.toLowerCase().endsWith(
							Constant.MDFILE_BPF_SUFFIX)) {
						fileName += Constant.MDFILE_BPF_SUFFIX;
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
								selTI, fileName, graph);
						fileTreeItem.setGraph(graph);
						view.openEditorTemp(fileTreeItem);
					} else {
						throw new Exception(Messages.NewBpfFileAction_3 + f.getPath());//文件已经存在：
					}

				}
			}
		} catch (Exception e) {
			String title = Messages.NewBpfFileAction_4;//新建业务操作组件
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}

	private JGraph getNewGraph(IMDPTreeNode item, File f) throws Exception {
		JGraph graph =JGraph.getBPFJGraph();
		graph.setNameSpace(item.getModuleName());
		graph.setOwnModule(item.getModuleName());
		String name = f.getName();
		name = name.substring(0,
				name.length() - Constant.MDFILE_BPF_SUFFIX.length());
		graph.setName(name);
		graph.setDisplayName(name);
		return graph;
	}
}
