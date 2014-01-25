package ncmdp.actions;

import java.util.List;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.views.CellPropertiesView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 *  Ù–‘÷√∂•≤Ÿ◊˜
 * @author wangxmn
 *
 */
public class AttrMoveTopAction extends Action {
	private class AttrMoveupCommand extends Command {
		private List<Attribute> al = null;
		private int index = -1;
		private int topindex = 0;

		public AttrMoveupCommand(List<Attribute> al, int index) {
			super(Messages.AttrMoveTopAction_0);
			this.al = al;
			this.index = index;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			if (index > 0) {
				TreeViewer treeView = view.getTv();
				Attribute attr = al.remove(index);
				al.add(topindex, attr);
				treeView.refresh(al);
			}
		}

		@Override
		public void undo() {
			if (index < al.size() - 1) {
				TreeViewer treeView = view.getTv();
				Attribute attr = al.remove(index - 1);
				al.add(index, attr);
				treeView.refresh(al);
			}
		}

	}

	private CellPropertiesView view = null;

	public AttrMoveTopAction(CellPropertiesView view) {
		super(Messages.AttrMoveTopAction_1);
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		TreeViewer treeView = view.getTv();
		treeView.cancelEditing();
		TreeSelection sel = (TreeSelection) treeView.getSelection();

		Object[] objs = sel.toArray();
		for (int i = 0; i < objs.length; i++) {
			Object o = objs[i];
			if (o instanceof Attribute) {
				List<Attribute> al = (List<Attribute>) ((List<Object>) treeView
						.getInput()).get(0);
				int index = al.indexOf(o);
				if (index > 0) {
					AttrMoveupCommand cmd = new AttrMoveupCommand(al, index);
					if (NCMDPEditor.getActiveMDPEditor() != null)
						NCMDPEditor.getActiveMDPEditor().executComand(cmd);
				}
			}
		}

	}
}
