package com.yonyou.nc.codevalidator.config;

import java.io.File;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.GlobalExecuteUnit;
import com.yonyou.nc.codevalidator.rule.RulePersistenceConstants;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public abstract class AbstractRuleConfig implements IRuleConfig {

	@Override
	public final String getRuleConfigRelativePath() {
		return String.format("/%s/%s", RulePersistenceConstants.RULECASE_FOLDER_NAME, getRuleConfigFileName());
	}
	
	public final String getRuleConfigFilePath(BusinessComponent businessComponent) {
		if (businessComponent instanceof GlobalExecuteUnit) {
			try {
				return ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext().getSystemRuntimeContext().getGlobalConfigFilePath();
			} catch (RuleBaseException e) {
				return null;
			}
		}
		return String.format("%s/%s", businessComponent.getBusinessComponentPath(), getRuleConfigRelativePath());
	}

	@Override
	public void configFolderInitialize(File configFolder) throws RuleBaseException {
		
	}
}
