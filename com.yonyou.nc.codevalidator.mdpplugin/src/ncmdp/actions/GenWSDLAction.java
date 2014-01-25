package ncmdp.actions;

import java.io.File;
import java.util.List;
import ncmdp.editor.NCMDPEditor;
import ncmdp.model.Service;
import ncmdp.util.ProjectUtil;
import ncmdp.wizard.GenWSDLWizard;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;

public class GenWSDLAction extends Action {

	public GenWSDLAction() {
		super(Messages.GenWSDLAction_0);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			List<?> list = editor.getGV().getSelectedEditParts();
//			JGraph graph = editor.getModel();
			Service service = null;
			if (list.size() > 0) {
				EditPart ep = (EditPart) list.get(0);
				Object o = ep.getModel();
				if (o instanceof Service) {
					service = (Service) o;
				}
			}
			if (service != null) {
				File file = editor.getFile();
//				IProject project = MDPExplorerTreeView.getMDPExploerTreeView(editor.getSite().getPage()).getFileOwnProject(file);
				IProject project = ProjectUtil.getFileOwnProject(file);
				if(project != null){
					File f = project.getLocation().toFile();
					File ff = new File(f, "src/websevice"); //$NON-NLS-1$
					GenWSDLWizard wizard = new GenWSDLWizard(service,ff.getAbsolutePath(),project);
					WizardDialog wd = new WizardDialog(editor.getSite().getShell(),wizard);
					wd.open();
				}
			} else {
				MessageDialog.openInformation(editor.getSite().getShell(), Messages.GenWSDLAction_2, Messages.GenWSDLAction_3);
			}

		}
	}


}
