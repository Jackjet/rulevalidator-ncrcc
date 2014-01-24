package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;

public class RuleExecResultDetailDialog extends TitleAreaDialog {

	protected Object result;
	private RuleExecuteResultDetailComposite ruleExecResultDetailComposite;
	private IRuleExecuteResult ruleExecuteResult;

	private List<IRuleExecuteResult> ruleExecuteResults;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public RuleExecResultDetailDialog(Shell parent, IRuleExecuteResult ruleExecuteResult,
			List<IRuleExecuteResult> ruleExecuteResults) {
		super(parent);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		this.ruleExecuteResult = ruleExecuteResult;
		this.ruleExecuteResults = ruleExecuteResults;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Composite composite1 = new Composite(container, SWT.NONE);
		composite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite1.setLayout(new GridLayout(1, false));

		ruleExecResultDetailComposite = new RuleExecuteResultDetailComposite(composite1, SWT.NONE, ruleExecuteResult,
				ruleExecuteResults);
		ruleExecResultDetailComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// ruleExecResultDetailComposite.loadRuleExecuteResult(ruleExecuteResult);

		return container;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		setTitle("规则执行结果");
		setMessage("显示具体规则的执行结果和修改意见");
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
		return new Point(850, 700);
	}

}
