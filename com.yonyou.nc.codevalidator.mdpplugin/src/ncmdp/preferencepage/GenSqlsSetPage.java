package ncmdp.preferencepage;

import ncmdp.NCMDPActivator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class GenSqlsSetPage extends PreferencePage implements IWorkbenchPreferencePage,ModifyListener {
	public static final String SQL_ROOT_DIR = "$sql_root_dir$";
	public static final String NOT_SHOW_WIZARD = "$not_show_wizard$";
	public static final String DEFAULT_SQL_PATH="c:/sqls";
	private Text sqlDirText = null;
	private Button noShowWizardCheckBox = null; 
	private Button openDirBtn = null; 
	private IPreferenceStore ps = null;
	
	public GenSqlsSetPage() {
		super();
	}

	public GenSqlsSetPage(String title) {
		super(title);
	}

	public GenSqlsSetPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE); 
		container.setLayout(new GridLayout(3,false));
		Label lbl = new Label(container, SWT.NONE);
		lbl.setText(Messages.GenSqlsSetPage_0);
		sqlDirText = new Text(container, SWT.BORDER);
		sqlDirText.addModifyListener(this);
		sqlDirText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		openDirBtn = new Button(container, SWT.BORDER);
		openDirBtn.setText("..."); 
		openDirBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				doOpenDirBtn();
			}
		});
		noShowWizardCheckBox = new Button(container, SWT.CHECK);
		noShowWizardCheckBox.setText(Messages.GenSqlsSetPage_4);
		
		sqlDirText.setText(ps.getString(SQL_ROOT_DIR));
		noShowWizardCheckBox.setSelection(ps.getBoolean(NOT_SHOW_WIZARD));
		return container;
	}
	@Override
	protected void performApply() {
		doSave();
	}
	private boolean doSave(){
		String dir = sqlDirText.getText().trim();
		if(dir == null || dir.trim().length() == 0){
			MessageDialog.openError(getShell(), Messages.GenSqlsSetPage_5, Messages.GenSqlsSetPage_6);
			return false;
		}
			
		ps.setValue(SQL_ROOT_DIR, dir);
		ps.setValue(NOT_SHOW_WIZARD, noShowWizardCheckBox.getSelection());
		return true;
	}
	@Override
	protected void performDefaults() {
		sqlDirText.setText(DEFAULT_SQL_PATH); 
		noShowWizardCheckBox.setSelection(false);
	}

	@Override
	public boolean performOk() {
		return doSave();
	}

	protected void doOpenDirBtn() {
		DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.SAVE);
		dd.setText(sqlDirText.getText());
		String text = dd.open();
		if(text != null){
			sqlDirText.setText(text);
		}
	}

	public void init(IWorkbench workbench) {
		ps = NCMDPActivator.getDefault().getPreferenceStore();
		String dir = ps.getString(SQL_ROOT_DIR);
		if(dir==null || dir.trim().length() == 0){
			dir = DEFAULT_SQL_PATH;//Ä¬ÈÏsql´æ·ÅÂ·¾¶
			ps.setValue(SQL_ROOT_DIR, dir);
		}
		setPreferenceStore(ps);
	}
	
	public void modifyText(ModifyEvent e) {
		
	}

}
