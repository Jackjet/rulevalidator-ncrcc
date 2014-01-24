package ncmdp.actions;

import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.EnumItem;
import ncmdp.model.activity.Parameter;
import ncmdp.views.EnumItemView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;

public class EnumItemMoveUpOrDownAction extends Action {
	private class EnumItemMoveUpOrDownCommand extends Command{
		private ArrayList<EnumItem> al = null;
		private int index = -1;
		public EnumItemMoveUpOrDownCommand(ArrayList<EnumItem> al, int index) {
			super();
			if(isUp){
				setLabel(Messages.EnumItemMoveUpOrDownAction_0);
			}else{
				setLabel(Messages.EnumItemMoveUpOrDownAction_1);
			}
			this.al = al;
			this.index = index;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			TableViewer tv = view.getEnumItemTableViewer();
			tv.cancelEditing();
			EnumItem enumItem = al.remove(index);
			if(isUp){
				al.add(index - 1, enumItem);
			}else{
				al.add(index + 1, enumItem);
			}
			tv.refresh(al);
		}
 
		@Override
		public void undo() {
			TableViewer tv = view.getEnumItemTableViewer();
			tv.cancelEditing();
			EnumItem enutitem = null;
			if(isUp){
				enutitem = al.remove(index-1);
			}else{
				enutitem = al.remove(index+1);
			}
			al.add(index , enutitem);
			tv.refresh(al);
		}

	}
	private boolean isUp = true;
	private EnumItemView view = null;
	public EnumItemMoveUpOrDownAction(EnumItemView view,boolean isUp) {
		super();
		this.isUp = isUp;
		this.view = view;
		if(isUp){
			setText(Messages.EnumItemMoveUpOrDownAction_2);
		}else{
			setText(Messages.EnumItemMoveUpOrDownAction_3);
		}
		
	}
	public void run() {
		TableViewer tv =view.getEnumItemTableViewer(); 
		Table table = tv.getTable();
		int index = table.getSelectionIndex();
		ArrayList<EnumItem> al = (ArrayList<EnumItem>) tv.getInput();
		if(isUp){
			if (index <= 0)
				return;
		}else{
			if (index == -1 || index >= al.size()-1)
				return;
		}
		EnumItemMoveUpOrDownCommand cmd = new EnumItemMoveUpOrDownCommand(al, index);
		if(NCMDPEditor.getActiveMDPEditor() != null)
			NCMDPEditor.getActiveMDPEditor().executComand(cmd);

	}

}
