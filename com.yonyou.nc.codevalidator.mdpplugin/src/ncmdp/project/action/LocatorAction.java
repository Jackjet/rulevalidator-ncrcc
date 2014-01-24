package ncmdp.project.action;

import ncmdp.factory.ImageFactory;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;

/**
 * ��λ��������λ����ǰ�༭�����ڽ��б༭�Ĳ���
 * @author wangxmn
 *
 */
public class LocatorAction extends Action {

	public LocatorAction() {
		super(Messages.LocatorAction_0);
		setImageDescriptor(ImageFactory.getLocatorImageDescriptor());
		setToolTipText(Messages.LocatorAction_1);
		setChecked(NCMDPTool.isSetLocator());
	}

	@Override
	public void run() {
		NCMDPTool.setLocator(isChecked());//��ť����ѡ��״̬
		doLocator();
		this.setChecked(false);//����ʹ��ť�ظ���ѡ��״̬
	}

	public static void doLocator(){
		if(NCMDPTool.isSetLocator()){
//			MDPExplorerTreeView view = MDPExplorerTreeView.getMDPExploerTreeView(null);
//			view.locator();
			ProjectUtil.locator();
		}
	}
}
