package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * 规则批量选择对话框
 * 
 * @author luoweid
 * @modify mazhqa 进行了重构，删除不必要的方法
 */
public class RuleCaseSelectDialog extends TitleAreaDialog {

	private List<RuleDefinitionAnnotationVO> configedVos;
	private RuleCaseSelectComposite ruleCaseSelectComposite;
	private RuleDefinitionAnnotationVO[] selectVos;

	public RuleCaseSelectDialog(Shell parentShell, List<RuleDefinitionAnnotationVO> configedVos) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);

		this.configedVos = configedVos;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("批量新增规则对话框");
		setMessage("NC规则检查配置");
		ruleCaseSelectComposite = new RuleCaseSelectComposite(this.configedVos, parent, SWT.NONE);
		return ruleCaseSelectComposite;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			selectVos = ruleCaseSelectComposite.getSelectedDefinitionVos();
		}
		super.buttonPressed(buttonId);
	}

	public RuleDefinitionAnnotationVO[] getSelectVos() {
		return selectVos;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

}
