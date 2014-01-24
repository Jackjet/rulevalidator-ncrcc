package ncmdp.project.action;

import ncmdp.project.MDPExplorerTreeView;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PublishModuleMetaDataAction extends Action {
	private boolean allowLowVersionPublish = false;

	public PublishModuleMetaDataAction(boolean allowLowVersionPublish) {
		super();
		if (allowLowVersionPublish) {
			setText(Messages.PublishModuleMetaDataAction_0);/* ����Ԫ����(���԰汾) */
		} else {
			setText(Messages.PublishModuleMetaDataAction_1);/* ����Ԫ���� */
		}
		this.allowLowVersionPublish = allowLowVersionPublish;
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView
//				.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		try {
			if (view != null)
				view.publishMetaData(allowLowVersionPublish);
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String message = e.getMessage();
			MessageDialog.openError(shell,
					Messages.PublishModuleMetaDataAction_1/* ����Ԫ���� */, message);
		}
	}
}
