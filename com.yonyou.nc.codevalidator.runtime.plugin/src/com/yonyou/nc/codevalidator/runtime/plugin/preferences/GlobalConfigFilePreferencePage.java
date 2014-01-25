package com.yonyou.nc.codevalidator.runtime.plugin.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.yonyou.nc.codevalidator.config.IRuleConfig;
import com.yonyou.nc.codevalidator.rule.RuleConstants;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.vo.RuleCheckConfigurationImpl;
import com.yonyou.nc.codevalidator.runtime.plugin.Activator;
import com.yonyou.nc.codevalidator.runtime.plugin.editor.RuleConfigEditorComposite;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 全局配置文件首选项页面
 * 
 * @author mazhqa
 * @since V2.6
 */
public class GlobalConfigFilePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
	private RuleConfigEditorComposite ruleCaseSelectComposite;

	/**
	 * @wbp.parser.constructor
	 */
	public GlobalConfigFilePreferencePage() {
	}

	public GlobalConfigFilePreferencePage(String title) {
		super(title);
	}

	public GlobalConfigFilePreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
		
	}

	@Override
	protected Control createContents(Composite parent) {
		String globalConfigFilePath = preferenceStore.getString(RuleConstants.GLOBAL_CONFIG_FILEPATH);
		IRuleConfig ruleConfig = Activator.getRuleConfig();
		FileInputStream fis = null;
		try {
			File file = new File(globalConfigFilePath);
			fis = new FileInputStream(file);
			RuleCheckConfigurationImpl ruleCheckConfiguration = (RuleCheckConfigurationImpl)ruleConfig.loadConfiguration(fis);
			ruleCaseSelectComposite = new RuleConfigEditorComposite(parent, SWT.NONE, null);
			ruleCaseSelectComposite.loadRuleConfigData(ruleCheckConfiguration);
			return ruleCaseSelectComposite;
		} catch (RuleBaseException e) {
			Logger.error("读取配置文件时发生异常", e);
			MessageDialog.openError(parent.getShell(), "读取配置文件时发生异常", e.getMessage());
			return parent;
		} catch (FileNotFoundException e) {
			Logger.error(String.format("文件:%s 未找到!", globalConfigFilePath), e);
			MessageDialog.openError(parent.getShell(), "读取配置文件时发生异常", e.getMessage());
			return parent;
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
	
	@Override
	public boolean performOk() {
		String globalConfigFilePath = preferenceStore.getString(RuleConstants.GLOBAL_CONFIG_FILEPATH);
		IRuleConfig ruleConfig = Activator.getRuleConfig();
		RuleCheckConfigurationImpl ruleCheckConfiguration = ruleCaseSelectComposite.getRuleCheckConfiguration();
		FileOutputStream fos = null;
		try{
			File file = new File(globalConfigFilePath);
			fos = new FileOutputStream(file);
			ruleConfig.exportConfig(fos, ruleCheckConfiguration);
		} catch (IOException e) {
			Logger.error("回写配置文件时发生异常", e);
			MessageDialog.openError(this.getShell(), "读取配置文件时发生异常", e.getMessage());
			return false;
		} catch (RuleBaseException e) {
			Logger.error(String.format("文件:%s 未找到!", globalConfigFilePath), e);
			MessageDialog.openError(this.getShell(), "读取配置文件时发生异常", e.getMessage());
			return false;
		} finally {
			IOUtils.closeQuietly(fos);
		}
		return super.performOk();
	}
	
}
