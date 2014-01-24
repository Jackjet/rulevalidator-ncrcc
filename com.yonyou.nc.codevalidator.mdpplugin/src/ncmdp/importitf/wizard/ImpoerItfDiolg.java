package ncmdp.importitf.wizard;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ImpoerItfDiolg extends TitleAreaDialog {
	private Text importItfText;
	private String fullClassName = ""; //$NON-NLS-1$

	public ImpoerItfDiolg(Shell parentShell) {
		super(parentShell);
	}

	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.ImpoerItfDiolg_1);
		setMessage(Messages.ImpoerItfDiolg_2);
		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayout(new GridLayout(1, false));

		Composite topComp = new Composite(comp, SWT.NULL);
		topComp.setLayout(new GridLayout(4, false));
		// нд╪Ч©Р
		Label sourceMDLable = new Label(topComp, SWT.NONE);
		sourceMDLable.setText(Messages.ImpoerItfDiolg_3);
		importItfText = new Text(topComp, SWT.BORDER);
		importItfText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				fullClassName = importItfText.getText();
			}
		});
		GridData textData = new GridData(300, -1);
		textData.horizontalSpan = 2;
		importItfText.setLayoutData(textData);
		return comp;
	}

	public String getImportItf() {
		return fullClassName;
	}

}
