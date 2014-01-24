package ncmdp.actions;

import java.util.ArrayList;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.views.CellPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * ʵ���������Ʋ���
 * @author wangxmn
 *
 */
public class AttrMoveDownAction extends Action {
	private class AttrMoveDownCommand extends Command {
		private List<Attribute> al = null;
		private int index = -1;

		public AttrMoveDownCommand(List<Attribute> al, int index) {
			super(Messages.AttrMoveDownAction_0);
			this.al = al;
			this.index = index;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			if (index < al.size() - 1) {
				TreeViewer treeView = view.getTv();
				Attribute attr = al.remove(index);
				al.add(index + 1, attr);
				treeView.refresh(al);
			}
		}

		@Override
		public void undo() {
			if (index > 0) {
				TreeViewer treeView = view.getTv();
				Attribute attr = al.remove(index + 1);
				al.add(index, attr);
				treeView.refresh(al);
			}
		}

	}

	private CellPropertiesView view = null;

	public AttrMoveDownAction(CellPropertiesView view) {
		super(Messages.AttrMoveDownAction_1);
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		TreeViewer treeView = view.getTv();
		treeView.cancelEditing();
		TreeSelection sel = (TreeSelection) treeView.getSelection();
		Object[] objs = sel.toArray();
		for (int i = objs.length - 1; i >= 0; i--) {
			Object o = objs[i];
			if (o instanceof Attribute) {
				ArrayList<Attribute> al = (ArrayList<Attribute>) ((List<Object>) treeView
						.getInput()).get(0); 
				int index = al.indexOf(o);
				if (index < al.size() - 1) {
					AttrMoveDownCommand cmd = new AttrMoveDownCommand(al, index);
					if (NCMDPEditor.getActiveMDPEditor() != null)
						NCMDPEditor.getActiveMDPEditor().executComand(cmd);
				}
			}
		}
	}

}