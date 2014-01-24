package ncmdp.actions;

import java.util.ArrayList;
import java.util.List;

import ncmdp.NCMDPActivator;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Attribute;
import ncmdp.model.BusinInterface;
import ncmdp.model.Cell;
import ncmdp.model.Entity;
import ncmdp.model.EnumItem;
import ncmdp.model.Enumerate;
import ncmdp.model.JGraph;
import ncmdp.model.Service;
import ncmdp.model.ValueObject;
import ncmdp.model.activity.BusiActivity;
import ncmdp.model.activity.BusiOperation;
import ncmdp.model.activity.OpInterface;
import ncmdp.model.activity.Operation;
import ncmdp.wizard.multiwizard.MultiLangWizard;
import ncmdp.wizard.multiwizard.MultiWizardDialog;
import ncmdp.wizard.multiwizard.util.IMultiElement;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author dingxm
 */
public class MultiLangAction extends Action {

	public MultiLangAction() {
		super(Messages.exportMulitiLanAction);
	}

	@Override
	public void run() {

		List<IMultiElement> multiAttrList = new ArrayList<IMultiElement>();
		IWorkbenchPage curPage = NCMDPActivator.getDefault().getWorkbench().getActiveWorkbenchWindow()
				.getActivePage();
		NCMDPEditor editor = (NCMDPEditor) curPage.getActiveEditor();
		JGraph graph = editor.getModel();
		multiAttrList.add(graph);//component
		String ownResModule = graph.getResModuleName();

		for (Cell cell : graph.getCells()) {
			if (cell instanceof Entity) {
				multiAttrList.add(cell);
				for (Attribute attr : ((Entity) cell).getProps()) {
					multiAttrList.add(attr);
				}
			} else if (cell instanceof ValueObject) {
				multiAttrList.add(cell);
			} else if (cell instanceof OpInterface) {
				multiAttrList.add(cell);
				for (Operation op : ((OpInterface) cell).getOperations()) {
					multiAttrList.add(op);
				}
			} else if (cell instanceof Enumerate) {
				multiAttrList.add(cell);
				for (EnumItem item : ((Enumerate) cell).getEnumItems()) {
					multiAttrList.add(item);
				}
			} else if (cell instanceof BusinInterface) {
				multiAttrList.add(cell);
			} else if (cell instanceof Service) {
			} else if (cell instanceof BusiOperation) {
				multiAttrList.add(cell);
			} else if (cell instanceof BusiActivity) {
				multiAttrList.add(cell);
			}
		}
		WizardDialog dilog = new MultiWizardDialog(curPage.getActiveEditor().getSite().getShell(),
				new MultiLangWizard(multiAttrList, ownResModule));
		dilog.setPageSize(600, 300);
		dilog.open();
	}
}
