package com.yonyou.nc.codevalidator.runtime.plugin.celleditor;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * ²ÎÕÕµÄCellEditor
 * 
 * @author mazqa
 * 
 */
public class RefPaneCellEditor extends WrapperDialogCellEditor {
	private Object valueObj;
	private Text text;
	private RefPaneDialog dialog;
	private IDialogComposite dialogComposite;

	public RefPaneCellEditor(final Composite parent, int style, Object valueObj, IDialogComposite dialogComposite) {
		super(parent, style);
		this.valueObj = valueObj;
		this.dialogComposite = dialogComposite;
		if (text != null) {
			text.setText(dialogComposite.getDisplayText(valueObj));
			text.setEditable(dialogComposite.isTextEnable());
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					if (RefPaneCellEditor.this.dialogComposite.isTextEnable()) {
						try {
							RefPaneCellEditor.this.valueObj = RefPaneCellEditor.this.dialogComposite
									.getObjectFromText(text.getText());
						} catch (Exception e1) {
							MessageDialog.openError(parent.getShell(), "", "");
						}
					}
				}
			});
		}
	}

	@Override
	protected Control createContents(Composite cell) {
		text = new Text(cell, SWT.NONE);
		text.setFont(cell.getFont());
		text.setBackground(cell.getBackground());
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					valueObj = null;
				}
			}
		});
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				RefPaneCellEditor.this.focusLost();
			}

			@Override
			public void focusGained(FocusEvent e) {
				RefPaneCellEditor.this.setFocus();
			}
		});
		return text;
	}

	@Override
	protected Object doGetValue() {
		return valueObj;
	}

	@Override
	protected void doSetFocus() {
	}

	@Override
	protected void doSetValue(Object value) {
		valueObj = value;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		if (dialogComposite == null) {
			MessageDialog.openError(cellEditorWindow.getShell(), "", "");
			return null;
		}
		// textEditorChanged = false;
		dialog = new RefPaneDialog(cellEditorWindow.getShell(), valueObj, dialogComposite);
		int result = dialog.open();
		if(result == IDialogConstants.OK_ID) {
			valueObj = dialogComposite.getResultValue();
			// if(valueObj != null){
			text.setText(valueObj == null ? "" : dialogComposite.getDisplayText(valueObj));
			text.setFocus();
		}
		// }
		return valueObj;
	}
}
