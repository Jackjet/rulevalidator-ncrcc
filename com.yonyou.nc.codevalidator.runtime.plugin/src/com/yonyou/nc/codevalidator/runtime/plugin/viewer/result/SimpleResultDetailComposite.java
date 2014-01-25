package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.yonyou.nc.codevalidator.rule.impl.AbstractSimpleExecuteResult;

/**
 * 简单执行结果详细信息的界面
 * @author mazhqa
 * @since V2.1
 */
public class SimpleResultDetailComposite extends Composite implements IResultDetailComposite<AbstractSimpleExecuteResult>{
	
	private Text resultText;
	private Text memoText;

	public SimpleResultDetailComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		resultText = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		resultText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		memoText = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		memoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Override
	public void loadRuleExecuteResult(AbstractSimpleExecuteResult executeResult) {
		resultText.setText(executeResult.getResult());
		memoText.setText(executeResult.getNote());
	}

}
