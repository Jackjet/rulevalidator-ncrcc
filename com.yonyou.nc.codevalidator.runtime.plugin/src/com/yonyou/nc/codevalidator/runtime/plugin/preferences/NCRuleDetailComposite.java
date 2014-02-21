package com.yonyou.nc.codevalidator.runtime.plugin.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.yonyou.nc.codevalidator.rule.RuleConstants;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;
import org.eclipse.swt.widgets.Button;

public class NCRuleDetailComposite extends Composite{
	
	private static final List<String> ALL_LOG_LEVELS = Arrays.asList(new String[] { "DEBUG", "INFO", "WARN", "ERROR" });

	public NCRuleDetailComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// String[][] entryNamesAndValues = { { "DEBUG", "DEBUG" }, { "INFO",
		// "INFO" }, { "WARN", "WARN" },
		// { "ERROR", "ERROR" } };
		// globalLogLevelField = new
		// StringFieldEditor(RuleConstants.GLOBAL_LOG_LEVEL, "日志级别", 50,
		// StringFieldEditor.VALIDATE_ON_FOCUS_LOST, subComp);

		Label label = new Label(this, SWT.NONE);
		label.setText("日志级别");

		CCombo logLevelCombo = new CCombo(this, SWT.BORDER);
		logLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		logLevelCombo.setItems(ALL_LOG_LEVELS.toArray(new String[ALL_LOG_LEVELS.size()]));
//		String logLevel = StringUtils.isBlank(preferenceStore.getString(RuleConstants.GLOBAL_LOG_LEVEL)) ? "ERROR"
//				: preferenceStore.getString(RuleConstants.GLOBAL_LOG_LEVEL);
//		logLevelCombo.select(ALL_LOG_LEVELS.indexOf(logLevel));

		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("规则执行级别");

		CCombo ruleLevelCombo = new CCombo(this, SWT.BORDER);
		ruleLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		List<String> allExecutePeriods = ExecutePeriod.getAllExecutePeriods();
		ruleLevelCombo.setItems(allExecutePeriods.toArray(new String[0]));
		
		Button btnCheckButton = new Button(this, SWT.CHECK);
		btnCheckButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnCheckButton.setText("支持V5X环境执行(业务组件规则可在模块级别执行)");
//		String ruleLevel = StringUtils.isBlank(preferenceStore.getString(RuleConstants.EXECUTE_LEVEL)) ? ExecutePeriod.DEPLOY
//				.getName() : preferenceStore.getString(RuleConstants.EXECUTE_LEVEL);
//		ruleLevelCombo.select(allExecutePeriods.indexOf(ruleLevel));
	}

}
