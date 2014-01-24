package ncmdp.newwizard;

import ncmdp.model.Constant;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewMDFilePage extends WizardNewFileCreationPage {
	public NewMDFilePage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle("new MD FILE");
	}
    protected IFile createFileHandle(IPath filePath) {
    	String path = filePath.toString().toLowerCase();
    	if(!path.endsWith(Constant.MDFILE_SUFFIX))
    		filePath = filePath.addFileExtension(Constant.MDFILE_SUFFIX.substring(1));
        return super.createFileHandle(filePath);
    }


}
