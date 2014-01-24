package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttopdm.wizard.ExportPDMFileWizard;
import ncmdp.model.JGraph;
import ncmdp.pdmpath.PDMPSServiceProxy;
import ncmdp.project.MDFileEditInput;
import ncmdp.tool.NCMDPTool;
import ncmdp.tool.basic.StringUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
/**
 * 导出pdm文件并打开
 * @author wangxmn
 *
 */
public class ExportToPdmFileActionAndOpen extends Action {
	public ExportToPdmFileActionAndOpen() {
		super(Messages.ExportToPdmFileActionAndOpen_0);
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
        if(editor != null)
        {
        	IProject project = NCMDPTool.getProjectOfEditor(editor);
			if(project==null){
				return;
			}
			JGraph graph = editor.getModel();
            IFile ifile = ((MDFileEditInput)editor.getEditorInput()).getIFile();
            String mdePath = PDMPSServiceProxy.getSuggestionPDMPath(ifile);
            String defaultPath = StringUtil.isEmpty(mdePath) ? "" : mdePath;
            if(project != null && graph != null)
            {
                ExportPDMFileWizard wizard = new ExportPDMFileWizard(graph, defaultPath, project);
                wizard.setOpen(true);
                WizardDialog wd = new WizardDialog(editor.getSite().getShell(), wizard);
                wd.open();
            }
        }
	}
}
