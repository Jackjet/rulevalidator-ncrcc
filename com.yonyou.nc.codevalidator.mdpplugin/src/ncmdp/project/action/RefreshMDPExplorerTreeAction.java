package ncmdp.project.action;

import ncmdp.factory.ImageFactory;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;

/**
 * ˢ����Դ����������
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
		//��õ�ǰ����Դ������ͼ��Ȼ�����ˢ��
//		MDPExplorerTreeView.getMDPExploerTreeView(null).refreshTree();
		ProjectUtil.openMDPExplorer().refreshTree();
	}
}
