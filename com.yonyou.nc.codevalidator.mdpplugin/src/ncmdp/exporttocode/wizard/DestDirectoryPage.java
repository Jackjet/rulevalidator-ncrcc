package ncmdp.exporttocode.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DestDirectoryPage extends WizardPage {
	private Text destDirText = null;

	private String defaultPath = null;

	private Button openBtn = null;

	private Button check1 = null;

	private Button check2 = null;

	private boolean forSql = false;

	public DestDirectoryPage(String pageName) {
		super(pageName);
	}

	public DestDirectoryPage(String pageName, String defaultPath, String title,
			ImageDescriptor titleImage, boolean forSql) {
		this(pageName, defaultPath, title, titleImage);
		this.forSql = forSql;
	}

	public DestDirectoryPage(String pageName, String defaultPath, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.defaultPath = defaultPath;
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		destDirText = new Text(container, SWT.BORDER);
		destDirText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		destDirText.setText(defaultPath);
		openBtn = new Button(container, SWT.BORDER);
		openBtn.setText("...");
		openBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBtnClicked(e);
			}

		});
		if (forSql) {
			check1 = new Button(container, SWT.CHECK);
			check1.setText("export sql with foreignkey Info ");
			new Label(container, SWT.NULL);
			check2 = new Button(container, SWT.CHECK);
			check2.setText("unicode Charactor");
		}

		setControl(container);

	}

	public boolean isGenFk() {
		return check1.getSelection();
	}

	public boolean forLocale() {
		return check2.getSelection();
	}

	protected void handleBtnClicked(SelectionEvent e) {
		DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.SAVE);
		dd.setText(destDirText.getText());
		String text = dd.open();
		if (text != null) {
			destDirText.setText(text);
		}
	}

	public String getDestDirPath() {
		return destDirText.getText();
	}
}
