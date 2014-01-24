package com.yonyou.nc.codevalidator.runtime.plugin.config.funnode;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.yonyou.nc.codevalidator.resparser.function.utils.NCFunnodeVO;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class FunnodeSelectDialog extends TitleAreaDialog {

	private List<String> selectedcodes;
	private FunnodeSelectComposite ruleCaseSelectComposite;
	private List<NCFunnodeVO> selectVos;

	public FunnodeSelectDialog(Shell parentShell, List<String> selectedcodes) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);

		this.selectedcodes = selectedcodes;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("NC功能节点选择");
		try {
			ruleCaseSelectComposite = new FunnodeSelectComposite(this.selectedcodes, parent, SWT.NONE);
		} catch (RuleBaseException e) {
			MessageDialog.openError(parent.getShell(), "错误", "初始化选择对话框失败，可能是NC数据源未连接!" + e.getMessage());
			Logger.error(e.getMessage(), e);
		}
		return ruleCaseSelectComposite;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			selectVos = ruleCaseSelectComposite.getSelectedVos();
		}
		super.buttonPressed(buttonId);
	}

	public List<NCFunnodeVO> getSelectVos() {
		return selectVos;
	}

}
