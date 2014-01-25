package ncmdp.project.action;

import ncmdp.factory.ImageFactory;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;

/**
 * 刷新资源管理树操作
 * @author wangxmn
 *
 */
public class RefreshMDPExplorerTreeAction extends Action {

	public RefreshMDPExplorerTreeAction() {
		super("refresh",ImageFactory.getRefreshImageDescriptor());
		setToolTipText("refresh");
	}

	@Override
	public void run() {
		//获得当前的资源管理视图，然后进行刷新
//		MDPExplorerTreeView.getMDPExploerTreeView(null).refreshTree();
		ProjectUtil.openMDPExplorer().refreshTree();
	}
}
