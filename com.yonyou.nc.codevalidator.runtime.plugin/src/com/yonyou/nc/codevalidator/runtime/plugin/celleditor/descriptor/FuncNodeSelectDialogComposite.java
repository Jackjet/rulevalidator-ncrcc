package com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.yonyou.nc.codevalidator.resparser.function.utils.NCFunnodeVO;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.AbstractDialogComposite;
import com.yonyou.nc.codevalidator.runtime.plugin.config.funnode.FunnodeSelectComposite;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

public class FuncNodeSelectDialogComposite extends AbstractDialogComposite {

	public static final String SPLITTER = ",";

	private FunnodeSelectComposite ruleCaseSelectComposite;
	private String funcodeValue;

	@Override
	public void createDialogArea(Composite parent, Object value) {
		try {
			funcodeValue = value == null ? null : String.valueOf(value);
			List<String> selectedNodes = new ArrayList<String>();
			if (StringUtils.isNotBlank(funcodeValue)) {
				selectedNodes = Arrays.asList(funcodeValue.split(SPLITTER));
			}
			ruleCaseSelectComposite = new FunnodeSelectComposite(selectedNodes, parent, SWT.NONE);
		} catch (RuleBaseException e) {
			MessageDialog.openInformation(parent.getShell(), "功能节点错误标题", "打开配置界面出错：" + e.getMessage());
		}
	}

	@Override
	public void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			List<NCFunnodeVO> selectedVos = ruleCaseSelectComposite.getSelectedVos();
			StringBuilder codeBuilder = new StringBuilder();
			if (null != selectedVos && selectedVos.size() > 0) {
				for (int i = 0; i < selectedVos.size(); i++) {
					if (i != 0) {
						codeBuilder.append(SPLITTER);
					}
					codeBuilder.append(selectedVos.get(i).getFuncode());
				}
			}
			funcodeValue = codeBuilder.toString();
		}
	}

	@Override
	public Object getResultValue() {
		return funcodeValue;
	}

	@Override
	public void createActualContents(Control parent, Object value) {
		parent.getShell().setText("NC功能节点选择对话框");
		parent.getShell().setSize(800, 400);
	}

}
