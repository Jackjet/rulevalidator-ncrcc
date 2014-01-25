package ncmdp.editor;

import ncmdp.project.action.LocatorAction;
import ncmdp.views.NCMDPViewSheet;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class PartListener implements IPartListener {
	public void partActivated(IWorkbenchPart part) {
		//判断当前活动的视图是否为NCMDPEditor
		if(part instanceof NCMDPEditor){
			NCMDPEditor editor = (NCMDPEditor) part;
			StructuredSelection sele = (StructuredSelection) editor.getGV().getSelection();
			Object o = sele.getFirstElement();
			StructuredSelection ss = new StructuredSelection(o);
			if (NCMDPViewSheet.getNCMDPViewPage() != null)
				NCMDPViewSheet.getNCMDPViewPage().selectionChanged(part, ss);
			LocatorAction.doLocator();
		}
	}

	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	public void partDeactivated(IWorkbenchPart part) {
		// if(part instanceof NCMDPEditor){
		// EnumItemView.getEnumItemView().getEnumItemTableViewer().setInput(null);
		// EnumItemView.getEnumItemView().setCellPart(null);
		// OperationView.getOperationView().getTreeView().setInput(null);
		// OperationView.getOperationView().setCellPart(null);
		// OperationView.getOperationView().getParamTableView().setInput(null);
		// CellPropertiesView.getPropertiesView().getTv().setInput(null);
		// CellPropertiesView.getPropertiesView().setCellPart(null);
		// }

	}

	public void partOpened(IWorkbenchPart part) {

	}

}
