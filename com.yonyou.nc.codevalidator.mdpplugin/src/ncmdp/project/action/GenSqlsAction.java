package ncmdp.project.action;

import ncmdp.project.MDPExplorerTreeView;
import ncmdp.util.ProjectUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * ���ɽ����sql
 * @author wangxmn
 *
 */
public class GenSqlsAction extends Action {
	private boolean isExecSqls = false;
	public GenSqlsAction(boolean isExecSqls) {
		super();
		this.isExecSqls = isExecSqls;
		if (isExecSqls) {
			setText(Messages.GenSqlsAction_0);//���ɽ���sql�ű���ִ��
		} else {
			setText(Messages.GenSqlsAction_1);//���ɽ���sql�ű�
		}
	}
	@Override
	public void run() {
//		MDPExplorerTreeView view = MDPExplorerTreeView.getMDPExploerTreeView(null);
		MDPExplorerTreeView view = ProjectUtil.openMDPExplorer();
		String errorMsg = null;
		try {
			if (view != null) {
				errorMsg = view.genSqls(isExecSqls);
			}
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String message = e.getMessage();
			MessageDialog.openError(shell, errorMsg, message);
		}
	}

}
