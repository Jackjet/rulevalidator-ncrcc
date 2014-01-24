package ncmdp.project.action;

import ncmdp.factory.ImageFactory;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;

/**
 * 更新状态，主要是图标等
 * @author wangxmn
 *
 */
public class UpdateTreeItemStateAction extends Action {

	public UpdateTreeItemStateAction() {
		super(Messages.UpdateTreeItemStateAction_0,ImageFactory.getUpdateStateImageDescriptor());
		setToolTipText(Messages.UpdateTreeItemStateAction_1);
	}
	@Override
	public void run() {
//		MDPExplorerTreeView.getMDPExploerTreeView(null).updateTreeItemState();
		ProjectUtil.openMDPExplorer().updateTreeItemState();
	}

}
