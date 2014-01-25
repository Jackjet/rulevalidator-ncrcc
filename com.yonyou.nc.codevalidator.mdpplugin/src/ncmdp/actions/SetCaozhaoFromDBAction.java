package ncmdp.actions;

import java.io.File;
import java.util.ArrayList;

import ncmdp.editor.NCMDPEditor;
import ncmdp.model.CanZhao;
import ncmdp.model.ValueObject;
import ncmdp.ui.CanZhaoSelDialog;
import ncmdp.util.ProjectUtil;
import ncmdp.views.CanzhaoView;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
/**
 * 从数据库中查找相应的参照
 * @author wangxmn
 *
 */
public class SetCaozhaoFromDBAction extends Action {
	private class SetCanzhaoFromDBCommand extends Command{
		private CanZhao[] czs = null;
		private ValueObject vo = null;
		private ArrayList<CanZhao> old = new ArrayList<CanZhao>();
		public SetCanzhaoFromDBCommand(CanZhao[] czs, ValueObject vo,IProject project) {
			super();
			this.czs = czs;
			this.vo = vo;
		}
		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public void execute() {
			old.addAll(vo.getAlCanzhao());
			redo();
		}
		@Override
		public void redo() {
			vo.getAlCanzhao().clear();
			int count = czs==null?0:czs.length;
			for (int i = 0; i < count; i++) {
				vo.addCanzhao(czs[i]);
			}
			view.getTreeViewer().refresh();
		}
		@Override
		public void undo() {
			vo.getAlCanzhao().clear();
			vo.getAlCanzhao().addAll(old);
			view.getTreeViewer().refresh();
		}
	}
	private CanzhaoView view = null;
	public SetCaozhaoFromDBAction(CanzhaoView czView) {
		super(Messages.SetCaozhaoFromDBAction_0);
		view = czView;
		setToolTipText(Messages.SetCaozhaoFromDBAction_1);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			Shell shell = view.getShell();
			File file = editor.getFile();
//			IProject project = MDPExplorerTreeView.getMDPExploerTreeView(editor.getSite().getPage()).getFileOwnProject(file);
			IProject project = ProjectUtil.getFileOwnProject(file);
			if(project != null){
				ValueObject vo = (ValueObject) view.getCellPart().getModel();
				CanZhaoSelDialog dlg = new CanZhaoSelDialog(shell,vo.getAlCanzhao().toArray(new CanZhao[0]),vo.getName(),project);
				if(dlg.open() == IDialogConstants.OK_ID){
					CanZhao[] czs = dlg.getSelCanzhaos();
					SetCanzhaoFromDBCommand cmd = new SetCanzhaoFromDBCommand(czs,vo,project);
					editor.executComand(cmd);
				}
			}
		}
	}
}
