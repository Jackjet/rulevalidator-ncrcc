package ncmdp.actions;

import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.activity.Parameter;
import ncmdp.views.OperationView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;

public class AddParameterAction extends Action {
	private class AddParameterCommand extends Command{
		private ArrayList<Parameter> al = null;
		private Parameter param = null;
		public AddParameterCommand(ArrayList<Parameter> al, Parameter param) {
			super(Messages.AddParameterAction_0);
			this.al = al;
			this.param = param;
		}
		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			TableViewer tv = view.getParamTableView();
			al.add(param);
			tv.refresh(al);
			TreeViewer treeView = view.getTreeView();
			treeView.refresh();

		}
		@Override
		public void undo() {
			TableViewer tv = view.getParamTableView();
			al.remove(param);
			tv.refresh(al);
			TreeViewer treeView = view.getTreeView();
			treeView.refresh();
		}
		
	}
	private OperationView view = null;
	public AddParameterAction(OperationView view) {
		super(Messages.AddParameterAction_1);
		this.view = view; 
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		TableViewer tv = view.getParamTableView();
		ArrayList<Parameter> al =(ArrayList<Parameter>) tv.getInput();
		if(al == null)
			return;
		AddParameterCommand cmd = new AddParameterCommand(al, new Parameter());
		if(NCMDPEditor.getActiveMDPEditor() != null)
			NCMDPEditor.getActiveMDPEditor().executComand(cmd);
	}

}
