package ncmdp.views.opImpl;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Cell;
import ncmdp.model.activity.OPItfImpl;
import ncmdp.model.activity.OpInterface;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.actions.ActionGroup;

public class OpItfImplActionGroup extends ActionGroup {
	OpItfImplView view = null;

	public OpItfImplActionGroup(OpItfImplView view) {
		this.view = view;
	}

	class AddOpImplAction extends Action {
		public AddOpImplAction() {
			setText(Messages.OpItfImplActionGroup_0);
		}

		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof OpInterface) {
				OPItfImpl opImpl = new OPItfImpl();
				((OpInterface) cell).addOPItfImpl(opImpl);
				view.getTableViewer().refresh();
				 NCMDPEditor.getActiveMDPEditor().setDirty(true);
			}
		}
	}

	class DelOpImplAction extends Action {
		public DelOpImplAction() {
			setText(Messages.OpItfImplActionGroup_1);
		}

		@Override
		public void run() {
			Cell cell = (Cell) view.getCellPart().getModel();
			if (cell instanceof OpInterface) {
				StructuredSelection selection = (StructuredSelection) (view.getTableViewer().getSelection());
				Object obj = selection.getFirstElement();
				if (obj == null) {
					MessageDialog.openInformation(view.getShell(), Messages.OpItfImplActionGroup_2, Messages.OpItfImplActionGroup_3);
					return;
				} else if (obj instanceof OPItfImpl) {
					((OpInterface) cell).removeOPItfImpl((OPItfImpl) obj);
					view.getTableViewer().refresh();
					 NCMDPEditor.getActiveMDPEditor().setDirty(true);
				}
			}
		}
	}

	public void fillActionToolBar(ToolBarManager tm) {
		Action addAction = new AddOpImplAction();
		Action delAction = new DelOpImplAction();
		tm.add(addAction);
		tm.add(delAction);
		tm.update(true);
	}
}
