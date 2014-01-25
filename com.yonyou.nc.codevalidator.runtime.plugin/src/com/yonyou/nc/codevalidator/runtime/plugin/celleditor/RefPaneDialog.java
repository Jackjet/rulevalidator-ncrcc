package com.yonyou.nc.codevalidator.runtime.plugin.celleditor;


import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.IDialogComposite.DialogButtonObj;

/**
 * 用于参照显示的对话框
 * @author mazqa
 *
 */
public class RefPaneDialog extends TitleAreaDialog {
	
	private Object valueObj;
	
	private IDialogComposite dialogComposite;

	protected RefPaneDialog(Shell parentShell, Object valueObj, IDialogComposite dialogComposite) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		this.valueObj = valueObj;
		this.dialogComposite = dialogComposite;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		dialogComposite.createContents(control, valueObj);
		setTitle(dialogComposite.getTitle());
		setMessage(dialogComposite.getDescription());
		return control;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
//		parent.setLayout(new GridLayout(1, true));
		dialogComposite.createDialogArea(container, valueObj);
		return container;
	}
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if(dialogComposite.getDialogButtons() != null && dialogComposite.getDialogButtons().size() > 0){
			for (DialogButtonObj button : dialogComposite.getDialogButtons()) {
				createButton(parent, button.getId(), button.getName(), button.isDefaultButton());
			}
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		dialogComposite.buttonPressed(buttonId);
		super.buttonPressed(buttonId);
	}
	
	public Object getResultValue(){
		return dialogComposite.getResultValue();
	}

}
