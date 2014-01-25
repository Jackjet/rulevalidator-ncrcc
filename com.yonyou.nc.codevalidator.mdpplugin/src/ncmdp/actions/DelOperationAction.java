package ncmdp.actions;

import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.views.OperationView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class DelOperationAction extends Action {
	private class DelOperationCommand extends Command{
		private ArrayList al = null;
		private Operation oper = null;
		private Cell cell = null;
		private int index = -1;
		public DelOperationCommand(ArrayList al, Cell cell, Operation oper) {
			super();
			this.al = al;
			this.cell = cell;
			this.oper = oper;
		}

		@Override
		public void execute() {
			if(cell instanceof ValueObject){
				index = ((ValueObject)cell).getOperations().indexOf(oper);
			}else if(cell instanceof OpInterface){
				index = ((OpInterface)cell).getOperations().indexOf(oper);
			}
			redo();
		}

		@Override
		public void redo() {
			TreeViewer tv =view.getTreeView();
			tv.cancelEditing();
			if(cell instanceof ValueObject){
				((ValueObject)cell).removeOperation(oper);
			}else if(cell instanceof OpInterface){
				((OpInterface)cell).removeOperation(oper);
			}else{
				throw new RuntimeException(Messages.DelOperationAction_0+cell);
			}
			tv.refresh(al);
			tv.expandAll();
			view.getParamTableView().setInput(null);
			view.getParamTableView().refresh();
		}

		@Override
		public void undo() {
			TreeViewer tv =view.getTreeView();
			tv.cancelEditing();
			if(cell instanceof ValueObject){
				((ValueObject)cell).addOperation(index,oper);
			}else if(cell instanceof OpInterface){
				((OpInterface)cell).addOperation(index,oper);
			}else{
				throw new RuntimeException(Messages.DelOperationAction_1+cell);
			}
			tv.refresh(al);
			tv.expandAll();
		}
		
	}
	private OperationView view = null;
	public DelOperationAction(OperationView view) {
		super(Messages.DelOperationAction_2);
		this.view = view;
	}

	@Override
	public void run() {
		TreeViewer tv =view.getTreeView(); 
		Tree tree = tv.getTree();
		TreeItem[] tis = tree.getSelection();
		if(tis != null && tis.length > 0){
			TreeItem ti = tis[0];
			Object o = ti.getData();
			ArrayList al = null;
			if(o instanceof Operation){
				Operation oper = (Operation)o;
				al =(ArrayList) ti.getParentItem().getData();
//				al.remove(oper);
				Cell cell = (Cell)view.getCellPart().getModel();
				DelOperationCommand cmd = new DelOperationCommand(al, cell, oper);
				if(NCMDPEditor.getActiveMDPEditor() != null)
					NCMDPEditor.getActiveMDPEditor().executComand(cmd);
//				if(cell instanceof Entity){
//					((Entity)cell).removeOperation(oper);
//				}else if(cell instanceof BusinessOperation){
//					((BusinessOperation)cell).removeOperation(oper);
//				}else{
//					throw new RuntimeException("删除操作时出错,非法的图元:"+cell);
//				}
//				tv.refresh(al);
//				tv.expandAll();
			}

		}
	}


}
