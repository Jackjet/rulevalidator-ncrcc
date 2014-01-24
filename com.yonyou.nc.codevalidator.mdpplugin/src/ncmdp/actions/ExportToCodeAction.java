package ncmdp.actions;

import java.io.File;
import java.util.List;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttocode.wizard.ExportToCodeWizard;
import ncmdp.model.Cell;
import ncmdp.model.Constant;
import ncmdp.model.JGraph;
import ncmdp.model.activity.OpInterface;
import ncmdp.project.MDFileEditInput;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
/**
 * Éú³ÉjavaÔ´Âë
 * @author wangxmn
 *
 */
public class ExportToCodeAction extends Action {

	public ExportToCodeAction() {
		super(Messages.ExportToCodeAction_0);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			IProject project = NCMDPTool.getProjectOfEditor(editor);
			if(project==null){
				return;
			}
			List<?> list = editor.getGV().getSelectedEditParts();
			JGraph graph = editor.getModel();
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
				String version = graph.getVersion() + ""; 
				if (project != null) {
					File f = project.getLocation().toFile();
					String componentNameOfNC = ""; 
					componentNameOfNC = ((MDFileEditInput) editor
							.getEditorInput()).getComponentName();
					String subPath = StringUtil
							.isEmptyWithTrim(componentNameOfNC) ? "src/public" 
							: componentNameOfNC + "/src/public"; 
					File ff = new File(f, subPath);
					String cellType = Constant.CELL_EMTITY;
					if (cell instanceof OpInterface) {
						cellType = Constant.CELL_OPINTERFACE;
					}
					ExportToCodeWizard wizard = new ExportToCodeWizard(id,
							version, ff.getAbsolutePath(), project,
							graph.getGenCodeStyle(), cellType);
					WizardDialog wd = new WizardDialog(editor.getSite()
							.getShell(), wizard);
					wd.open();
				}
			} else {
				MessageDialog.openInformation(editor.getSite().getShell(),
						Messages.ExportToCodeAction_5, Messages.ExportToCodeAction_6);
			}
		}
	}
}
