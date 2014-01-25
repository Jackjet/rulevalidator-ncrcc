package ncmdp.project.action;

import ncmdp.project.MDPExplorerTreeView;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * 新建目录文件夹
 * @author wangxmn
 *
 */
public class NewDirAction extends Action {

	public NewDirAction() {
		super(Messages.NewDirAction_0);
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		if (view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell,
				Messages.NewDirAction_0, Messages.NewDirAction_2, "", null);
		if (input.open() == InputDialog.OK) {
			String dirName = input.getValue();
			if (dirName != null && dirName.trim().length() > 0) {
				dirName = dirName.trim();
				try {
					view.addDirTreeNode(dirName);
				} catch (Exception e) {
					String title = Messages.NewDirAction_0;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}else{
				MessageDialog.openError(shell, Messages.NewDirAction_0, Messages.NewDirAction_1);
			}
		}
	}

}
