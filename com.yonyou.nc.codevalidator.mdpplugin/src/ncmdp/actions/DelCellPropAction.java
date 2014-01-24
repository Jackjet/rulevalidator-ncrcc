package ncmdp.actions;

import java.util.ArrayList;
import java.util.List;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.ValueObject;
import ncmdp.util.MDPCommonUtil;
import ncmdp.views.CellPropertiesView;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
/**
 * 删除实体属性的操作
 * @author wangxmn
 *
 */
public class DelCellPropAction extends Action {
	private class DelCellPropCommand extends Command {
		private ValueObject vo = null;
		private List<Attribute> al = null;
		private int index = -1;
		private boolean hasAttributes = false;

		public DelCellPropCommand(List<Attribute> al, ValueObject vo) {
			super(Messages.DelCellPropAction_0);
			this.al = al;
			this.vo = vo;
			if(al.size()>0){
				hasAttributes = true;
			}
		}

		@Override
		public void execute() {
			if(hasAttributes){
				index = vo.getProps().indexOf(al.get(0));
				redo();
			}
		}

		@Override
		public void redo() {
			for(Attribute attr:al){
				vo.removeProp(attr);
			}
			TreeViewer tv = getPropertiesView().getTv();
			tv.cancelEditing();
//			tv.refresh(al);
			tv.refresh();
			tv.expandAll();
		}

		@Override
		public void undo() {
			if(hasAttributes){
				for(int i=0,j=al.size();i<j;i++){
					vo.addProp(index+i, al.get(i));
				}
				TreeViewer tv = getPropertiesView().getTv();
				tv.cancelEditing();
//				tv.refresh(al);
				tv.refresh();
				tv.expandAll();
			}
			
		}

	}

	private CellPropertiesView view = null;

	public DelCellPropAction(CellPropertiesView view) {
		setText(Messages.DelCellPropAction_1);
		this.view = view;
	}

	private CellPropertiesView getPropertiesView() {
		return view;
	}

	@Override
	public void run() {
		TreeViewer tv = getPropertiesView().getTv();
		Tree tree = tv.getTree();
		TreeItem[] tis = tree.getSelection();
		List<Attribute> al = new ArrayList<Attribute>();
		Object model = getPropertiesView().getCellPart().getModel();
		if(model instanceof ValueObject){
			ValueObject vo = (ValueObject) model;
			if (tis != null && tis.length > 0) {
				for(TreeItem ti : tis){
					Object o = ti.getData();
					if (o instanceof Attribute && model instanceof ValueObject) {
						Attribute prop = (Attribute) o;
						if (!MDPCommonUtil.attrCanModify(NCMDPEditor
								.getActiveMDPEditor().getModel(), (ValueObject) model,
								prop)) {
							MessageDialog.openError(null, Messages.DelCellPropAction_2,
									Messages.DelCellPropAction_3);
							return;
						}
						al.add(prop);
					}
				}
				DelCellPropCommand cmd = new DelCellPropCommand(al, vo);
				if (NCMDPEditor.getActiveMDPEditor() != null)
					NCMDPEditor.getActiveMDPEditor().executComand(cmd);
			}
		}
	}
}
