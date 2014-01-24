package ncmdp.project.action;

import ncmdp.project.MDPExplorerTreeView;
import ncmdp.tool.basic.StringUtil;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GeneratePDMAction extends Action {

	public GeneratePDMAction() {
		super();
		setText(Messages.GeneratePDMAction_0);//批量导出PDM
	}

	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		Shell shell = new Shell(Display.getCurrent());
		String title = Messages.GeneratePDMAction_1;//导出PDM
		String errMsg = "";
		try {
			if (view != null)
				errMsg = view.generatePDM();
			if (!StringUtil.isEmptyWithTrim(errMsg)) {
				MessageDialog.openError(shell, title, errMsg);
			}
		} catch (Exception e) {
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
}
