package ncmdp.importattr.wizard;

import java.io.File;
import java.util.ArrayList;

import ncmdp.pdmxml.Field;
import ncmdp.pdmxml.PDMParser;
import ncmdp.pdmxml.Table;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
/**
 * 从pdm中导入数据源界面
 * @author wangxmn
 *
 */
public class SelPDMFilePathWizardPage extends WizardPage implements IImportTables{
	private Text fileNameText = null;
	private Button openBtn = null;
	public SelPDMFilePathWizardPage(String pageName) {
		super(pageName);
	}

	public SelPDMFilePathWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout(2,false));
		fileNameText = new Text(container, SWT.BORDER);
		fileNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		openBtn = new Button(container, SWT.BORDER);
		openBtn.setText("..."); //$NON-NLS-1$
		openBtn.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBtnClicked(e);
			}
			
		});
		setControl(container);

	}

	protected void handleBtnClicked(SelectionEvent e) {
		FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
		fd.setFilterNames(new String[]{Messages.SelPDMFilePathWizardPage_1});
		fd.setFilterExtensions(new String[]{"*.pdm"}); //$NON-NLS-1$
		String text = fd.open();
		if(text != null){
			fileNameText.setText(text);
		}
	}
	


	@Override
	public IWizardPage getNextPage() {
		return getWizard().getPage(SelTableColumnWizardPage.class.getCanonicalName());
	}
	private String getPDMFilePath(){
		return fileNameText.getText();
	}

	public Table[] getAllTables() {
		Table[] tables = null;
		String pdmFilePath = getPDMFilePath();
		if (pdmFilePath != null && pdmFilePath.trim().length() > 0) {
			PDMParser parser = new PDMParser(new File(pdmFilePath));
			tables = parser.parse();
		}
		return tables;
	}

	public ArrayList<Field> getFields(Table table) {
		return table.getFields();
	}
}
