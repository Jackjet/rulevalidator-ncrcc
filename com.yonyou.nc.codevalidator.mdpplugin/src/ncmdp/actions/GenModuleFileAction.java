package ncmdp.actions;

import java.io.File;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.JGraph;
import ncmdp.serialize.ModuleSerialize;
import ncmdp.ui.tree.mdptree.MDPFileTreeItem;
import ncmdp.util.MDPLogger;
import ncmdp.util.ProjectUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TreeItem;

public class GenModuleFileAction extends Action {
	public static enum GenModuleFileOption {
		ONLY_USM, ONLY_UPM, ALL
	}

	private GenModuleFileOption option = GenModuleFileOption.ALL;

	public GenModuleFileAction(GenModuleFileOption option) {
		super();
		if (option == GenModuleFileOption.ONLY_UPM) {
			setText(Messages.GenModuleFileAction_0);
		} else if (option == GenModuleFileOption.ONLY_USM) {
			setText(Messages.GenModuleFileAction_1);
		} else if (option == GenModuleFileOption.ALL) {
			setText(Messages.GenModuleFileAction_2);
		}
		this.option = option;
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			JGraph graph = editor.getModel();
			File file = editor.getFile();
//			IProject project = MDPExplorerTreeView.getMDPExploerTreeView(
//					editor.getSite().getPage()).getFileOwnProject(file);
			IProject project = ProjectUtil.getFileOwnProject(file);
//			TreeItem item = MDPExplorerTreeView.getMDPExploerTreeView(
//					editor.getSite().getPage()).findMDFileTreeItem(file);
			TreeItem item = ProjectUtil.findMDFileTreeItem(file);
			if (project != null && item != null) {
				String fileName = null;
				try {
					if (option == GenModuleFileOption.ONLY_USM
							|| option == GenModuleFileOption.ALL) {
						ModuleSerialize.genBsmFile(graph, project,
								(MDPFileTreeItem) item);
					}
					if (option == GenModuleFileOption.ONLY_UPM
							|| option == GenModuleFileOption.ALL) {
						fileName = ModuleSerialize.genBpmFile(graph, project,
								(MDPFileTreeItem) item);
					}
				} catch (Exception e) {
					MDPLogger.error(e.getMessage(), e);
					String msg = e.getClass().getCanonicalName() + ":" //$NON-NLS-1$
							+ e.getMessage();
					MessageDialog.openError(editor.getSite().getShell(), Messages.GenModuleFileAction_4,
							msg);
					return;
				}
				MessageDialog.openInformation(editor.getSite().getShell(),
						Messages.GenModuleFileAction_5, Messages.GenModuleFileAction_6 + fileName);
			}
		}
	}
}
