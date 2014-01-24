package ncmdp.actions;

import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.activity.Parameter;
import ncmdp.views.OperationView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class DelParameterAction extends Action {
	private class DelParamCommand extends Command{
		private ArrayList<Parameter> al = null;
		private Parameter para = null;
		private int index = -1;
		public DelParamCommand(ArrayList<Parameter> al , Parameter para) {
			super();
			this.al = al;
			this.para = para;
		}

		@Override
		public void execute() {
			index = al.indexOf(para);
			redo();
		}

		@Override
		public void redo() {
			TableViewer tv = view.getParamTableView();
			al.remove(para);
			tv.refresh(al);
		}

		@Override
		public void undo() {
			TableViewer tv = view.getParamTableView();
			al.add(index,para);
			tv.refresh(al);
		}
		
	}
	private OperationView view = null;
	public DelParameterAction(OperationView view) {
		super(Messages.DelParameterAction_0);
		this.view =view;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		TableViewer tv = view.getParamTableView();
		Table table = tv.getTable();
		TableItem [] tis = table.getSelection();
		if(tis != null && tis.length > 0){
			ArrayList<Parameter> al = (ArrayList<Parameter>)tv.getInput();
			TableItem ti = tis[0];
			Parameter param = (Parameter)ti.getData();
			DelParamCommand cmd = new DelParamCommand(al,param);
			if(NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
//			al.remove(param);
//			tv.refresh(al);
		}
	}

}
