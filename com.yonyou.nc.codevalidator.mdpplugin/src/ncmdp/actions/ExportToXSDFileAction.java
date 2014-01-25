package ncmdp.actions;

import java.io.File;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttoxsd.wizard.GenXSDFileWizard;
import ncmdp.model.Cell;
import ncmdp.util.ProjectUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;

public class ExportToXSDFileAction extends Action {

	public ExportToXSDFileAction() {
		super(Messages.ExportToXSDFileAction_0);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			List<?> list = editor.getGV().getSelectedEditParts();
			Cell cell = null;
			if (list.size() > 0) {
				EditPart ep = (EditPart) list.get(0);
				Object o = ep.getModel();
				if (o instanceof Cell) {
					cell = (Cell) o;
				}
			}
			if (cell != null) {
				String id = cell.getId();
				File file = editor.getFile();
//				IProject project = MDPExplorerTreeView.getMDPExploerTreeView(editor.getSite().getPage()).getFileOwnProject(file);
				IProject project = ProjectUtil.getFileOwnProject(file);
				if(project != null){
					File f = project.getLocation().toFile();
					File ff = new File(f, "src/websevice"); //$NON-NLS-1$
					GenXSDFileWizard wizard = new GenXSDFileWizard(id,ff.getAbsolutePath(),project);
					WizardDialog wd = new WizardDialog(editor.getSite().getShell(),wizard);
					wd.open();
				}
			} else {
				MessageDialog.openInformation(editor.getSite().getShell(), Messages.ExportToXSDFileAction_2, Messages.ExportToXSDFileAction_3);
			}
		}
	}


}
