package ncmdp.newwizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class NewMDFileWizard extends BasicNewResourceWizard implements
		INewWizard {
	private NewMDFilePage newMDFilePage = null;

	public NewMDFileWizard() {
		super();
	}

	@Override
	public boolean performFinish() {
		IFile file = newMDFilePage.createNewFile();
		if (file == null) {
			return false;
		}

		selectAndReveal(file);

		IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
		try {
			if (dw != null) {
				IWorkbenchPage page = dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, file, true);
				}
			}
		} catch (PartInitException e) {
			MessageDialog.openError(dw.getShell(), "note", e.getMessage());
		}

		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		newMDFilePage = new NewMDFilePage("new MD File", selection);// 新建模型文件

	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(newMDFilePage);
	}

}
