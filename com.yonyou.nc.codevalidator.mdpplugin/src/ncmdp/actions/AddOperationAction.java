package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.views.OperationView;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 增加业务接口属性
 * @author wangxmn
 *
 */
public class AddOperationAction extends Action {
	private class AddOperationCommand extends Command {

		private Operation oper = null;

		private Cell cell = null;

		public AddOperationCommand(Cell cell, Operation oper) {
			super(Messages.AddOperationAction_0);
			this.cell = cell;
			this.oper = oper;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			if (cell instanceof ValueObject) {
				((ValueObject) cell).addOperation(oper);
			} else if (cell instanceof OpInterface) {
				((OpInterface) cell).addOperation(oper);
			} else {
				throw new RuntimeException(Messages.AddOperationAction_1 + cell);
			}
			view.getTreeView().refresh();
			view.getTreeView().expandAll();

		}

		@Override
		public void undo() {
			if (cell instanceof ValueObject) {
				((ValueObject) cell).removeOperation(oper);
			} else if (cell instanceof OpInterface) {
				((OpInterface) cell).removeOperation(oper);
			} else {
				throw new RuntimeException(Messages.AddOperationAction_2 + cell);
			}
			view.getTreeView().refresh();
			view.getTreeView().expandAll();
		}

	}

	private OperationView view = null;

	public AddOperationAction(OperationView view) {
		super(Messages.AddOperationAction_3);
		this.view = view;
	}

	@Override
	public void run() {
		//新增一条实体的业务属性，并不设置createIndustry字段的内容，故为默认的值0
		Tree tree = view.getTreeView().getTree();
		TreeItem[] tis = tree.getSelection();
		if (tis != null && tis.length > 0) {
			insertNullOperation();
		}
	}

	private void insertNullOperation() {
		if (view.getCellPart() != null) {
			Operation oper = new Operation();
			Cell cell = (Cell) view.getCellPart().getModel();
			oper.setOpItfID(cell.getId());//为业务接口操作添加属性
			AddOperationCommand cmd = new AddOperationCommand(cell, oper);
			if (NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}
}
