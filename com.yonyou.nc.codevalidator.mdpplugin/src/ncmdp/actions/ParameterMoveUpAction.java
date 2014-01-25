package ncmdp.actions;

import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.activity.Parameter;
import ncmdp.views.OperationView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;

public class ParameterMoveUpAction extends Action {
	private class ParamMoveUpCommand extends Command{
		private ArrayList<Parameter> al = null;
		private int index = -1;
		public ParamMoveUpCommand(ArrayList<Parameter> al, int index) {
			super(Messages.ParameterMoveUpAction_0);
			this.al = al;
			this.index = index;
		}
		@Override
		public void execute() {
			redo();
		}
		@Override
		public void redo() {
			TableViewer tv = view.getParamTableView();
			tv.cancelEditing();
			Parameter param = al.remove(index);
			al.add(index - 1, param);
			tv.refresh(al);
		}
		@Override
		public void undo() {
			TableViewer tv = view.getParamTableView();
			tv.cancelEditing();
			Parameter param = al.remove(index-1);
			al.add(index, param);
			tv.refresh(al);
		}
		
	}
	private OperationView view = null;
	public ParameterMoveUpAction(OperationView view) {
		super(Messages.ParameterMoveUpAction_1);
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		TableViewer tv = view.getParamTableView();
		tv.cancelEditing();
		Table table = tv.getTable();
		int index = table.getSelectionIndex();
		if (index <= 0)
			return;
		ArrayList<Parameter> al = (ArrayList<Parameter>) tv.getInput();
		ParamMoveUpCommand cmd = new ParamMoveUpCommand(al, index);
		if(NCMDPEditor.getActiveMDPEditor() != null)
			NCMDPEditor.getActiveMDPEditor().executComand(cmd);
//		Parameter param = al.remove(index);
//		al.add(index - 1, param);
//		tv.refresh(al);

	}

}
