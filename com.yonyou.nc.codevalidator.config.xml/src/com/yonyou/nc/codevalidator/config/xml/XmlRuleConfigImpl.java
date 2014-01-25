package com.yonyou.nc.codevalidator.config.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.yonyou.nc.codevalidator.config.AbstractRuleConfig;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.vo.IRuleCheckConfiguration;
import com.yonyou.nc.codevalidator.rule.vo.RuleCheckConfigurationImpl;
import com.yonyou.nc.codevalidator.sdk.utils.RuleXmlUtils;
import com.yonyou.nc.codevalidator.sdk.utils.XmlPersistenceConstants;

/**
 * xml格式的规则配置文件实现，加载配置文件时，可以结合全局配置进行执行
 * 
 * @author mazhqa
 * @since V2.6
 */
public class XmlRuleConfigImpl extends AbstractRuleConfig {

	@Override
	public void exportConfig(OutputStream os, IRuleCheckConfiguration ruleCheckConfiguration) throws RuleBaseException {
		RuleXmlUtils.writeRuleCheckDefinition(os, ruleCheckConfiguration);
	}

	@Override
	public IRuleCheckConfiguration loadConfiguration(InputStream is) throws RuleBaseException {
		return RuleXmlUtils.loadRuleCheckConfiguration(is);
	}

	@Override
	public String getRuleConfigFileName() {
		return "ruleconfig.xml";
	}

	@Override
	public void configFolderInitialize(File configFolder) throws RuleBaseException {
		this.copyFileToFolder(configFolder, XmlPersistenceConstants.CONFIG_XSL_NAME);
	}

	private void copyFileToFolder(File folder, String fileName) throws RuleBaseException {
		File configStyleFile = new File(folder, fileName);
		if (!configStyleFile.exists()) {
			try {
				FileUtils.copyURLToFile(this.getClass().getResource(fileName), configStyleFile);
			} catch (IOException e) {
				throw new RuleBaseException(e);
			}
		}
	}

	@Override
	public IRuleCheckConfiguration parseRule(BusinessComponent businessComponent) throws RuleBaseException {
		FileInputStream fis = null;
		try {
			String ruleConfigFilePath = this.getRuleConfigFilePath(businessComponent);
			File ruleConfigFile = new File(ruleConfigFilePath);
			IRuleCheckConfiguration ruleCheckConfiguration = new RuleCheckConfigurationImpl();
			if (ruleConfigFile.exists()) {
				fis = new FileInputStream(ruleConfigFile);
				ruleCheckConfiguration = loadConfiguration(fis);
			}
			return ruleCheckConfiguration;
		} catch (FileNotFoundException e) {
			throw new RuleBaseException(e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}

	@Override
	public void configFileInitialize(File configFile) throws RuleBaseException {
		try {
			FileUtils.copyURLToFile(this.getClass().getResource("ruleconfig.xml"), configFile);
		} catch (IOException e) {
			throw new RuleBaseException(e);
		}
	}

}
