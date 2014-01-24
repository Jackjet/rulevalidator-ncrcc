package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;
import ncmdp.parts.CellPart;
import ncmdp.views.EnumItemView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
/**
 * 删除枚举，只能一条条删除，不支持批量删除
 * @author wangxmn
 *
 */
public class DelEnumItemAction extends Action {
	private class DelEnumItemCommand extends Command {
		private Enumerate enumerate = null;
		private EnumItem enumItem = null;
		private int index = -1;

		public DelEnumItemCommand(Enumerate enumerate, EnumItem enumItem) {
			super();
			this.enumerate = enumerate;
			this.enumItem = enumItem;
		}

		@Override
		public void execute() {
			index = enumerate.getEnumItems().indexOf(enumItem);
			redo();
		}

		@Override
		public void redo() {
			enumerate.removeEnumItem(enumItem);
		}

		@Override
		public void undo() {
			enumerate.addEnumItem(index, enumItem);
		}

	}

	private EnumItemView view = null;

	public DelEnumItemAction(EnumItemView view) {
		super(Messages.DelEnumItemAction_0);
		this.view = view;
	}

	@Override
	public void run() {
		TableViewer tv = view.getEnumItemTableViewer();
		Table table = tv.getTable();
		TableItem[] tis = table.getSelection();
		if (tis != null && tis.length > 0) {
			TableItem ti = tis[0];
			EnumItem enumItem = (EnumItem) ti.getData();
			if (NCMDPEditor.getActiveMDPEditor().getModel()
					.isIndustryIncrease()
					&& enumItem.isSource()) {
				MessageDialog.openError(null, Messages.DelEnumItemAction_1,
						Messages.DelEnumItemAction_2);
				return;
			}
			CellPart part = view.getCellPart();
			Enumerate enumerate = (Enumerate) part.getModel();
			DelEnumItemCommand cmd = new DelEnumItemCommand(enumerate, enumItem);
			if (NCMDPEditor.getActiveMDPEditor() != null)
				NCMDPEditor.getActiveMDPEditor().executComand(cmd);
		}
	}

}
