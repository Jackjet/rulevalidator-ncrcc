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
			setText(Messages.PublishModuleMetaDataAction_0);/* 发布元数据(忽略版本) */
		} else {
			setText(Messages.PublishModuleMetaDataAction_1);/* 发布元数据 */
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
					Messages.PublishModuleMetaDataAction_1/* 发布元数据 */, message);
		}
	}
}
