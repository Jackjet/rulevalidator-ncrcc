package ncmdp.actions;

import ncmdp.editor.NCMDPEditor;
import ncmdp.exporttosql.wizard.GenSqlsWizard;
import ncmdp.model.JGraph;
import ncmdp.project.MDFileEditInput;
import ncmdp.tool.NCMDPTool;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;

public class GenSqlAction extends Action {
	private boolean isExecSqls = false;

	public GenSqlAction(boolean isExecSqls) {
		super();
		this.isExecSqls = isExecSqls;
		if (isExecSqls) {
			setText(Messages.GenSqlAction_0);
		} else {
			setText(Messages.GenSqlAction_1);
		}
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
            String path = ifile.getFullPath().toString();
            String rootPath = project.getFullPath().toString()+"METADATA";
            String sqlpath = path.substring(rootPath.length());
            if(project != null && graph != null)
            {
            	GenSqlsWizard wizard = new GenSqlsWizard(graph, project,sqlpath,isExecSqls);
				if(NCMDPTool.isShowWizardWhenGenSqls()){
					WizardDialog wd = new WizardDialog(editor.getSite().getShell(), wizard);
					wd.open();
				}else{
					wizard.doGenSql(NCMDPTool.getGenSqlsRootDir());
				}
            }
        }
	}

}
