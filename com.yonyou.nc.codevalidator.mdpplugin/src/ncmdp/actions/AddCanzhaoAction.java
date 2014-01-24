package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.CanZhao;
import ncmdp.model.ValueObject;
import ncmdp.views.CanzhaoView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;

public class AddCanzhaoAction extends Action {
	private class AddCanzhaoCommand extends Command {
		private ValueObject vo = null;

		private CanZhao cz = null;

		public AddCanzhaoCommand(ValueObject vo, CanZhao cz) {
			super();
			this.vo = vo;
			this.cz = cz;
		}

		@Override
		public void execute() {
			redo();
		}

		@Override
		public void redo() {
			vo.addCanzhao(cz);
			czView.getTreeViewer().expandAll();
			czView.getTreeViewer().refresh();
		}

		@Override
		public void undo() {
			vo.delCanzhao(cz);
			czView.getTreeViewer().expandAll();
			czView.getTreeViewer().refresh();
		}

	}

	private CanzhaoView czView = null;

	public AddCanzhaoAction(CanzhaoView czView) {
		super(Messages.AddCanzhaoAction_0);
		this.czView = czView;
	}

	@Override
	public void run() {
		if (czView.getCellPart() != null && czView.getCellPart().getModel() instanceof ValueObject) {
			InputDialog input = new InputDialog(czView.getShell(), Messages.AddCanzhaoAction_1, Messages.AddCanzhaoAction_2, "", null); //$NON-NLS-3$
			if (input.open() == InputDialog.OK) {
				CanZhao cz = new CanZhao();
				String czName = input.getValue();
				cz.setName(czName);
				ValueObject vo = (ValueObject) czView.getCellPart().getModel();
				AddCanzhaoCommand cmd = new AddCanzhaoCommand(vo, cz);
				if(NCMDPEditor.getActiveMDPEditor() != null)
					NCMDPEditor.getActiveMDPEditor().executComand(cmd);
			}
		}

	}

}
