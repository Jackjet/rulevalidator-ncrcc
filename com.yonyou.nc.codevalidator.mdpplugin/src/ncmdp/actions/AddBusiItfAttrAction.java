package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.views.BusinessInterfaceAttrsView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 新添加业务接口属性
 * @author wangxmn
 *
 */
public class AddBusiItfAttrAction extends Action {
	private class AddBusiItfAttrCommand extends Command{
		private BusiItfAttr attr = null;
		private BusinInterface busiItf = null;
		public AddBusiItfAttrCommand(BusinInterface busiItf,BusiItfAttr attr){
			super(Messages.AddBusiItfAttrAction_0);
			this.attr = attr;
			this.busiItf = busiItf;
		}
		@Override
		public void execute() {
			redo();
		}
		@Override
		public void redo() {
			busiItf.addBusiAttr(attr);
			view.getTreeViewer().refresh();
			view.getTreeViewer().expandAll();
		}
		@Override
		public void undo() {
			busiItf.removeBusiAttr(attr);
			view.getTreeViewer().refresh();
			view.getTreeViewer().expandAll();
		}
	}
	
	private BusinessInterfaceAttrsView view = null;
	public AddBusiItfAttrAction(BusinessInterfaceAttrsView view) {
		super(Messages.AddBusiItfAttrAction_1);
		this.view = view;
	}
	@Override
	public void run() {
		Tree tree = view.getTreeViewer().getTree();
		TreeItem[] tis = tree.getSelection();
		if(tis != null && tis.length > 0){
			insertNullAttr();
		}
	}
	private void insertNullAttr(){
		BusiItfAttr attr =new BusiItfAttr();
		if(view.getCellPart() != null && view.getCellPart().getModel() instanceof BusinInterface){
			BusinInterface itf = (BusinInterface)view.getCellPart().getModel();
			AddBusiItfAttrCommand cmd = new AddBusiItfAttrCommand(itf,attr);
			if(NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}
}
