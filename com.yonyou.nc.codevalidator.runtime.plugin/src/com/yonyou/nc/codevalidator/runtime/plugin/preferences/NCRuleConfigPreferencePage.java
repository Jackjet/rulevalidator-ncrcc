package com.yonyou.nc.codevalidator.runtime.plugin.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.yonyou.nc.codevalidator.rule.RuleConstants;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

public class NCRuleConfigPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private static final List<String> ALL_LOG_LEVELS = Arrays.asList(new String[] { "DEBUG", "INFO", "WARN", "ERROR" });

	private final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

	private FileFieldEditor globalConfigFilePathField;
	private DirectoryFieldEditor globalExportFilePathField;
	private DirectoryFieldEditor globalLogFilePathField;

	private CCombo ruleLevelCombo;

	private CCombo logLevelCombo;

	private Button runInNc5xBtn;

	public NCRuleConfigPreferencePage() {
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		createSettingContents(composite);

		return composite;
	}

	private void createSettingContents(Composite parent) {
		Composite group = new Composite(parent, SWT.NONE);
		group.setLayout(new GridLayout(3, true));
		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_group.widthHint = 318;
		group.setLayoutData(gd_group);
		// 根目录
		globalConfigFilePathField = new FileFieldEditor(RuleConstants.GLOBAL_CONFIG_FILEPATH, "全局配置文件", group);
		globalConfigFilePathField.setPreferenceStore(preferenceStore);
		globalConfigFilePathField.setStringValue(preferenceStore.getString(RuleConstants.GLOBAL_CONFIG_FILEPATH));
//		globalConfigFilePathField.setPropertyChangeListener(new IPropertyChangeListener() {
//			public void propertyChange(PropertyChangeEvent event) {
//				if (event.getProperty().equals(FileFieldEditor.VALUE)) {
//					preferenceStore.setValue(RuleConstants.GLOBAL_CONFIG_FILEPATH, (String) event.getNewValue());
//				}
//			}
//		});

		globalExportFilePathField = new DirectoryFieldEditor(RuleConstants.GLOBAL_EXPORT_FILEPATH, "全局导出目录", group);
		globalExportFilePathField.setPreferenceStore(preferenceStore);
		globalExportFilePathField.setStringValue(preferenceStore.getString(RuleConstants.GLOBAL_EXPORT_FILEPATH));
//		globalExportFilePathField.setPropertyChangeListener(new IPropertyChangeListener() {
//			;
//
//			public void propertyChange(PropertyChangeEvent event) {
//				if (event.getProperty().equals(DirectoryFieldEditor.VALUE)) {
//					preferenceStore.setValue(RuleConstants.GLOBAL_EXPORT_FILEPATH, (String) event.getNewValue());
//				}
//			}
//		});

		globalLogFilePathField = new DirectoryFieldEditor(RuleConstants.GLOBAL_LOG_FILEPATH, "日志导出目录", group);
		globalLogFilePathField.setPreferenceStore(preferenceStore);
		globalLogFilePathField.setStringValue(preferenceStore.getString(RuleConstants.GLOBAL_LOG_FILEPATH));
//		globalLogFilePathField.setPropertyChangeListener(new IPropertyChangeListener() {
//			;
//
//			public void propertyChange(PropertyChangeEvent event) {
//				if (event.getProperty().equals(DirectoryFieldEditor.VALUE)) {
//					preferenceStore.setValue(RuleConstants.GLOBAL_LOG_FILEPATH, (String) event.getNewValue());
//				}
//			}
//		});

		Composite subComp = new Composite(parent, SWT.NONE);
		subComp.setLayout(new GridLayout(2, false));
		subComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// String[][] entryNamesAndValues = { { "DEBUG", "DEBUG" }, { "INFO",
		// "INFO" }, { "WARN", "WARN" },
		// { "ERROR", "ERROR" } };
		// globalLogLevelField = new
		// StringFieldEditor(RuleConstants.GLOBAL_LOG_LEVEL, "日志级别", 50,
		// StringFieldEditor.VALIDATE_ON_FOCUS_LOST, subComp);

		Label label = new Label(subComp, SWT.NONE);
		label.setText("日志级别");

		logLevelCombo = new CCombo(subComp, SWT.BORDER);
		logLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		logLevelCombo.setItems(ALL_LOG_LEVELS.toArray(new String[ALL_LOG_LEVELS.size()]));
		String logLevel = StringUtils.isBlank(preferenceStore.getString(RuleConstants.GLOBAL_LOG_LEVEL)) ? "ERROR"
				: preferenceStore.getString(RuleConstants.GLOBAL_LOG_LEVEL);
		logLevelCombo.select(ALL_LOG_LEVELS.indexOf(logLevel));

		Label label_1 = new Label(subComp, SWT.NONE);
		label_1.setText("规则执行级别");

		ruleLevelCombo = new CCombo(subComp, SWT.BORDER);
		ruleLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		List<String> allExecutePeriods = ExecutePeriod.getAllExecutePeriods();
		ruleLevelCombo.setItems(allExecutePeriods.toArray(new String[0]));
		String ruleLevel = StringUtils.isBlank(preferenceStore.getString(RuleConstants.EXECUTE_LEVEL)) ? ExecutePeriod.DEPLOY
				.getName() : preferenceStore.getString(RuleConstants.EXECUTE_LEVEL);
		ruleLevelCombo.select(allExecutePeriods.indexOf(ruleLevel));
		
		runInNc5xBtn = new Button(subComp, SWT.CHECK);
		runInNc5xBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		runInNc5xBtn.setText("支持V5X环境执行(业务组件规则可在模块级别执行)");
		boolean runInNc5x = preferenceStore.getBoolean(RuleConstants.RUN_IN_NC_5X);
		runInNc5xBtn.setSelection(runInNc5x);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setDescription("NC规则系统运行配置");
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	public boolean performOk() {
		preferenceStore.setValue(RuleConstants.GLOBAL_CONFIG_FILEPATH, globalConfigFilePathField.getStringValue());
		preferenceStore.setValue(RuleConstants.GLOBAL_EXPORT_FILEPATH, globalExportFilePathField.getStringValue());
		preferenceStore.setValue(RuleConstants.GLOBAL_LOG_FILEPATH, globalLogFilePathField.getStringValue());
		preferenceStore.setValue(RuleConstants.GLOBAL_LOG_LEVEL, logLevelCombo.getText());
		preferenceStore.setValue(RuleConstants.EXECUTE_LEVEL, ruleLevelCombo.getText());
		preferenceStore.setValue(RuleConstants.RUN_IN_NC_5X, runInNc5xBtn.getSelection());
		return super.performOk();
	}
	
}
