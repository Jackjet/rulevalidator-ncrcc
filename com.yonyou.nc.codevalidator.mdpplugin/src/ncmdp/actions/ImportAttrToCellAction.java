package ncmdp.actions;

import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.importattr.wizard.ImportAttrWizard;
import ncmdp.importattr.wizard.SelTableColumnWizardPage;
import ncmdp.model.ValueObject;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * 导入属性的功能
 * @author wangxmn
 *
 */
public class ImportAttrToCellAction extends Action {

	public ImportAttrToCellAction() {
		super(Messages.ImportAttrToCellAction_0);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			List<?> list = editor.getGV().getSelectedEditParts();
			ValueObject vo = null;
			if (list.size() > 0) {
				EditPart ep = (EditPart) list.get(0);
				Object o = ep.getModel();
				if (o instanceof ValueObject) {
					vo = (ValueObject) o;
				}
			}
			if (vo != null) {
				WizardDialog wd = new WizardDialog(editor.getSite().getShell(), new ImportAttrWizard(vo)) {
					@Override
					protected void nextPressed() {
						IWizardPage page = getCurrentPage().getNextPage();
						if (page instanceof SelTableColumnWizardPage) {
							((SelTableColumnWizardPage)page).initTableListData();
						}
						super.nextPressed();
					}
				};
				String title = Messages.ImportAttrToCellAction_1 + vo.getDisplayName() + Messages.ImportAttrToCellAction_2;
				wd.setTitle(title);
				wd.setPageSize(-1, 300);
				wd.open();
			} else {
				MessageDialog.openConfirm(editor.getSite().getShell(), Messages.ImportAttrToCellAction_3, Messages.ImportAttrToCellAction_4);
			}
		}
	}
}
