package ncmdp.wizard.multiwizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import ncmdp.util.MDPLogger;
import ncmdp.wizard.multiwizard.util.IMultiElement;
import ncmdp.wizard.multiwizard.util.UTFProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

/**
 * ������Դ�༭
 * @author dingxm 2010-08-10
 */
public class MultiLangWizard extends Wizard {
	private List<IMultiElement> multiAttrList = null;

	private MultiWizardPage1 page1 = null;

	private MultiWizardPage2 page2 = null;

	private MultiWizardPage3 page3 = null;

	private String ownResModule = null;

	public MultiLangWizard(List<IMultiElement> multiAttrList, String ownModule) {
		super();
		this.multiAttrList = multiAttrList;
		this.ownResModule = ownModule;
	}

	@Override
	/**
	 * ����ҳ��
	 */
	public void addPages() {
		page1 = new MultiWizardPage1("page1", multiAttrList); //$NON-NLS-1$
		page2 = new MultiWizardPage2("page2", ownResModule); //$NON-NLS-1$
		page3 = new MultiWizardPage3("page3"); //$NON-NLS-1$
		page1.setPage3(page3);
		page2.setPage3(page3);
		addPage(page1);
		addPage(page2);
		addPage(page3);
	}

	@Override
	/**
	 * "Finish"��ť�Ƿ���Ч
	 */
	public boolean canFinish() {
		if (getContainer().getCurrentPage() != page3) { return false; }
		return super.canFinish();
	}

	@Override
	/**
	 * ��"Finish"�رնԻ���ʱ���Ĵ���
	 */
	public boolean performFinish() {
		//checkout��Դ�ļ����޸�
		updadeFile(page3.getUTFProperty(), page3.getFilePath());
		//����ʵ��
		page3.updateElements();
		return true;
	}

	private void updadeFile(UTFProperties prop, String fileDir) {
		if (prop == null) { return; }
		//checkout��Դ�ļ�
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(fileDir));
		boolean proLocation = file == null ? false : true;//�Ƿ񱾵ع����е��ļ�

		if (proLocation) {
			IStatus statu = ResourcesPlugin.getWorkspace().validateEdit(new IFile[] { file }, getShell());
			if (!statu.isOK()) {
				MessageDialog.openInformation(getShell(), Messages.MultiLangWizard_3, statu.getMessage());
				return;
			}
		}
		//д����Դ�ļ�
		updateProp(prop, new File(fileDir));
	}

	/**
	 * �ύprop
	 * @param prop
	 * @param curFile
	 */
	private void updateProp(UTFProperties prop, File curFile) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(curFile);
			prop.store(fos);
		} catch (IOException e) {
			MDPLogger.error(e.getMessage(), e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					MDPLogger.error(e.getMessage(), e);
				}
			}
		}
	}

}
