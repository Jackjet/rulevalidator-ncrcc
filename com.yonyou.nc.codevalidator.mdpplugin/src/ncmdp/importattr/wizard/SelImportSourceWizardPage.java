package ncmdp.importattr.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
/**
 * 导入数据源界面
 * @author wangxmn
 *
 */
public class SelImportSourceWizardPage extends WizardPage {
	private Button fromPDMXML = null;
	private Button fromDB = null;
	private Button fromBillItem = null;
	public SelImportSourceWizardPage(String pageName) {
		super(pageName);
	}

	public SelImportSourceWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new FillLayout());
		Group group = new Group(container, SWT.NONE);
		group.setText(Messages.SelImportSourceWizardPage_0);
		group.setLayout(new FillLayout(SWT.VERTICAL));
		fromPDMXML = new Button(group, SWT.RADIO);
		fromPDMXML.setText(Messages.SelImportSourceWizardPage_1);
		fromDB = new Button(group,SWT.RADIO);
		fromDB.setText(Messages.SelImportSourceWizardPage_2);
		fromBillItem = new Button(group, SWT.RADIO);
		fromBillItem.setText(Messages.SelImportSourceWizardPage_3);
		setControl(container);
	}

	@Override
	public IWizardPage getNextPage() {
		SelTableColumnWizardPage selColPage =(SelTableColumnWizardPage) getWizard().getPage(SelTableColumnWizardPage.class.getCanonicalName());
		if(fromPDMXML.getSelection()){
			SelPDMFilePathWizardPage page = (SelPDMFilePathWizardPage)getWizard().getPage(SelPDMFilePathWizardPage.class.getCanonicalName());
			selColPage.setImportTables(page);
			return page;
		}else if(fromDB.getSelection()){
			SelDBTableWizardPage page = (SelDBTableWizardPage)getWizard().getPage(SelDBTableWizardPage.class.getCanonicalName());
			selColPage.setImportTables(page);
			return page;
		}else if(fromBillItem.getSelection()){
			SelTableFromBillItem page = (SelTableFromBillItem)getWizard().getPage(SelTableFromBillItem.class.getCanonicalName());
			selColPage.setImportTables(page);
			return page;
		}else
			return super.getNextPage();
	}
}
