package ncmdp.actions;

import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.property.XMLAttribute;
import ncmdp.model.property.XMLElement;
import ncmdp.views.OperationPropertyView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class DelOperationPropertyAction extends Action {
	private class DelOperationPropertyCommand extends Command {
		private Object parentObj = null;

		private Object selObj = null;

		private int index = -1;

		public DelOperationPropertyCommand(Object parentObj, Object selobj) {
			super();
			this.parentObj = parentObj;
			this.selObj = selobj;
		}

		@Override
		public void execute() {
			TreeViewer tv = view.getTreeViewer();

			if (parentObj instanceof ArrayList) {
				index = ((ArrayList) parentObj).indexOf(selObj);
			} else if (parentObj instanceof XMLElement) {
				index = ((XMLElement) parentObj).getChilds().indexOf(selObj);
			}
			redo();
		}

		@Override
		public void redo() {
			TreeViewer tv = view.getTreeViewer();
			// if(parentItem == null){
			// ((List)tv.getInput()).remove(obj);
			// }else{
			// parentEle.removeXMLElement((XMLElement)obj);
			//		
			// }
			if (parentObj instanceof ArrayList) {
				((ArrayList) parentObj).remove(selObj);
			} else if (parentObj instanceof XMLElement) {
				((XMLElement) parentObj).removeXMLElement((XMLElement) selObj);
			}
			tv.refresh();
			tv.expandAll();
		}

		@Override
		public void undo() {
			TreeViewer tv = view.getTreeViewer();
			// if(parentItem == null){
			// ((List)tv.getInput()).add(index, obj);
			// }else{
			// // XMLElement ele = ((XMLElement)parentItem.getData());
			// parentEle.addXMLElement(index,(XMLElement)obj);
			// }
			if (parentObj instanceof ArrayList) {
				((ArrayList) parentObj).add(index, selObj);
			} else if (parentObj instanceof XMLElement) {
				((XMLElement) parentObj).addXMLElement(index, (XMLElement) selObj);
			}
			tv.refresh();
			tv.expandAll();
		}

	}

	private OperationPropertyView view = null;

	public DelOperationPropertyAction(OperationPropertyView view) {
		super(Messages.DelOperationPropertyAction_0);
		this.view = view;
	}

	@Override
	public void run() {
		TreeViewer tv = view.getTreeViewer();
		Tree tree = tv.getTree();
		TreeItem[] items = tree.getSelection();
		if (items != null && items.length > 0) {
			TreeItem selItem = items[0];
			TreeItem parentItem = selItem.getParentItem();
			Object parentObj = parentItem.getData();
			Object selObj = selItem.getData();
			if (selObj instanceof XMLElement) {
				DelOperationPropertyCommand cmd = new DelOperationPropertyCommand(parentObj, selObj);
				if (NCMDPEditor.getActiveMDPEditor() != null)
					NCMDPEditor.getActiveMDPEditor().executComand(cmd);
			}
			// Object o = selItem.getData();
			// if(parentItem == null){
			// ((List)tv.getInput()).remove(o);
			// }else{
			// XMLElement ele = ((XMLElement)parentItem.getData());
			// ele.removeXMLElement((XMLElement)o);
			// }
			// selItem.dispose();

		}
	}

}
