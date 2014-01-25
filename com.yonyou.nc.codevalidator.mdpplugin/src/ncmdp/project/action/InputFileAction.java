package ncmdp.project.action;

import java.io.File;

import ncmdp.model.JGraph;
import ncmdp.project.BuildFileTreeUtil;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.tool.basic.StringUtil;
import ncmdp.ui.tree.mdptree.IMDPTreeNode;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 导入元数据的功能
 * @author wangxmn
 *
 */
public class InputFileAction extends Action {
	public InputFileAction() {
		super(Messages.InputFileAction_0);
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		if (view == null)
			return;
		Tree tree = view.getExplorerTree();
		TreeItem[] selTIs = tree.getSelection();
		Shell shell = new Shell(Display.getCurrent());
		try {
			if (selTIs == null || selTIs.length == 0)
				throw new Exception(Messages.InputFileAction_1);
			TreeItem selTI = selTIs[0];
			if (!(selTI instanceof IMDPTreeNode))
				return;
			File parentFile = ((IMDPTreeNode) selTI).getFile();
			if (parentFile.isFile())
				throw new Exception(Messages.InputFileAction_2);
			//选择元数据文件
			FileDialog fileD = new FileDialog(shell, SWT.OPEN);
			fileD.setFilterNames(new String[] { Messages.InputFileAction_3,
					Messages.InputFileAction_4 });
			fileD.setFilterExtensions(new String[] { "*.bmf", "*.bpf" }); //$NON-NLS-1$ //$NON-NLS-2$
			String filePath = fileD.open();
			if (StringUtil.isEmptyWithTrim(filePath)) {
				return;
			}
			// 新建一个备份文件
			File sourceFile = new File(filePath);//原始文件
			String fileName = sourceFile.getName();
			File newFile = new File(parentFile, fileName);//复制后的文件
			if (!newFile.exists()) {
				newFile.createNewFile();
				BuildFileTreeUtil.copyFile(sourceFile, newFile);
				JGraph graph = JGraphSerializeTool.loadFromFile(newFile, false);
				MDPFileTreeItem fileTreeItem = view.addFileTreeNode(selTI,
						fileName, graph);
				fileTreeItem.setGraph(graph);
				view.openEditorTemp(fileTreeItem);
			} else {
				throw new Exception(Messages.InputFileAction_7 + newFile.getPath());
			}
		} catch (Exception e) {
			MessageDialog.openError(shell, Messages.InputFileAction_8, e.getMessage());
		}
	}
}
