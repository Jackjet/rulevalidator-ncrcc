package ncmdp.project.action;

import ncmdp.factory.ImageFactory;
import ncmdp.tool.NCMDPTool;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;

/**
 * 定位操作，定位到当前编辑器正在进行编辑的操作
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
		NCMDPTool.setLocator(isChecked());//按钮进入选中状态
		doLocator();
		this.setChecked(false);//可以使按钮回复非选中状态
	}

	public static void doLocator(){
		if(NCMDPTool.isSetLocator()){
//			MDPExplorerTreeView view = MDPExplorerTreeView.getMDPExploerTreeView(null);
//			view.locator();
			ProjectUtil.locator();
		}
	}
}
