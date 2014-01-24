package ncmdp.project.action;

import ncmdp.factory.ImageFactory;
import ncmdp.project.MDPExplorerTreeView;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DeleteAction extends Action {
	public DeleteAction() {
		super(Messages.DeleteAction_0,ImageFactory.getDeleteImageDescriptor());
		setToolTipText(Messages.DeleteAction_0);
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		try {
			if(view != null)
				view.deleteSelectedTreeNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =Messages.DeleteAction_0;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
