package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.BusiItfAttr;
import ncmdp.model.BusinInterface;
import ncmdp.views.BusinessInterfaceAttrsView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 删除业务接口属性
 * @author wangxmn
 *
 */
public class DelBusiItfAttrAction extends Action {
	private class DelBusiItfAttrCommand extends Command{
		private BusinInterface itf = null;
		private BusiItfAttr attr = null;
		private int index = -1;
		public DelBusiItfAttrCommand(BusinInterface itf, BusiItfAttr attr) {
			super(Messages.DelBusiItfAttrAction_0);
			this.itf = itf;
			this.attr = attr;
		}
		@Override
		public void execute() {
			index = itf.getBusiItAttrs().indexOf(attr);
			redo();
		}

		@Override
		public void redo() {
			itf.removeBusiAttr(attr);
			TreeViewer tv = view.getTreeViewer();
			tv.cancelEditing();
			tv.expandAll();
		}

		@Override
		public void undo() {
			itf.addBusiAttr(index, attr);
			TreeViewer tv = view.getTreeViewer();
			tv.cancelEditing();
			tv.expandAll();
		}
		
	}
	private BusinessInterfaceAttrsView view = null;
	public DelBusiItfAttrAction(BusinessInterfaceAttrsView view) {
		super(Messages.DelBusiItfAttrAction_1);
		this.view = view;
	}

	@Override
	public void run() {
		TreeViewer tv = view.getTreeViewer();
		Tree tree = tv.getTree();
		TreeItem[] tis = tree.getSelection();
		if (tis != null && tis.length > 0) {
			TreeItem ti = tis[0];
			Object o = ti.getData();
			Object model =view.getCellPart().getModel();
			if (o instanceof BusiItfAttr && model instanceof BusinInterface) {
				BusiItfAttr attr = (BusiItfAttr) o;
				BusinInterface vo = (BusinInterface) model;
				DelBusiItfAttrCommand cmd = new DelBusiItfAttrCommand(vo, attr);
				if(NCMDPEditor.getActiveMDPEditor() != null)
					NCMDPEditor.getActiveMDPEditor().executComand(cmd);

			}

		}
	}


}
