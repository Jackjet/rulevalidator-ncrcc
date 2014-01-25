package ncmdp.project.action;

import java.io.File;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.JGraph;
import ncmdp.project.MDFileEditInput;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.serialize.JGraphSerializeTool;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.ProjectUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

/**
 * 保存元数据文件
 * @author wangxmn
 *
 */
public class SaveAllMDFilesAction extends Action {

	public SaveAllMDFilesAction() {
		super(Messages.SaveAllMDFilesAction_0);
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		try {
			if (view != null) {
				IWorkbenchPage page = view.getViewSite().getPage();
				Tree tree = view.getExplorerTree();
				int count = tree.getItemCount();
				for (int i = 0; i < count; i++) {
					TreeItem ti = tree.getItem(i);
					SaveAllMDFiles(ti, page);
				}
				Shell shell = page.getWorkbenchWindow().getShell();
				MessageDialog.openInformation(shell, "save", Messages.SaveAllMDFilesAction_2);
			}
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String message = e.getMessage();
			MessageDialog.openError(shell, "save", message);
		}
	}

	/**
	 * 在当前工作页面下保存树节点
	 * @param ti
	 * @param page
	 */
	private void SaveAllMDFiles(TreeItem ti, IWorkbenchPage page) {
		if (ti instanceof MDPFileTreeItem) {
			MDPFileTreeItem mdpItem = (MDPFileTreeItem) ti;
			IEditorInput input = mdpItem.getEditorInput();
			IEditorPart editor = page.findEditor(input);

			if (editor != null && editor instanceof NCMDPEditor) {
				NCMDPEditor mdpEditor = (NCMDPEditor) editor;
				mdpEditor.save();
			} else {
				File file = null;
				if (input instanceof MDFileEditInput) {
					Shell shell = page.getWorkbenchWindow().getShell();
					MDFileEditInput fei = (MDFileEditInput) input;
					file = fei.getFile();
					IStatus statu = ResourcesPlugin.getWorkspace().validateEdit(new IFile[] { fei.getIFile() }, shell);
					if (!statu.isOK()) {
						MessageDialog.openInformation(shell, Messages.SaveAllMDFilesAction_4, statu.getMessage());
						return;
					}
				}
				if (file != null) {
					JGraph graph = mdpItem.getGraph();
					graph.updateVersion();
					try {
						JGraphSerializeTool.saveToFile(graph, file);
					} catch (Exception e) {
						MDPLogger.error(e.getMessage(), e);
					}
				}
			}
		} else {
			TreeItem[] childs = ti.getItems();
			int count = childs == null ? 0 : childs.length;
			for (int i = 0; i < count; i++) {
				SaveAllMDFiles(childs[i], page);
			}
		}
	}

}
