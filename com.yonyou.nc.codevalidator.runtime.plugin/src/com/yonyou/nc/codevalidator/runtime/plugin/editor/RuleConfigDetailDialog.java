package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.yonyou.nc.codevalidator.rule.vo.RuleItemConfigVO;

public class RuleConfigDetailDialog extends TitleAreaDialog {

	protected Object result;
	private RuleConfigDetailComposite ruleConfigDetailComposite;
	private RuleItemConfigVO ruleItemConfigVO;
	
	private IRuleEditorListener ruleEditorListener;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public RuleConfigDetailDialog(Shell parent, RuleItemConfigVO ruleItemConfigVO, IRuleEditorListener ruleEditorListener) {
		super(parent);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		this.ruleItemConfigVO = ruleItemConfigVO;
		this.ruleEditorListener = ruleEditorListener;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Composite innerComposite = new Composite(container, SWT.NONE);
		innerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		innerComposite.setLayout(new GridLayout(1, false));

		ruleConfigDetailComposite = new RuleConfigDetailComposite(innerComposite, SWT.NONE, ruleEditorListener);
		ruleConfigDetailComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		ruleConfigDetailComposite.loadRuleItemConfigVO(ruleItemConfigVO);

		return container;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		setTitle("规则配置");
		setMessage("显示具体规则的详情及配置");
		return control;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case IDialogConstants.OK_ID:
			break;
		case IDialogConstants.CANCEL_ID:
			break;
		default:
			break;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	public boolean isModified() {
		return ruleConfigDetailComposite.isModified();
	}

}
