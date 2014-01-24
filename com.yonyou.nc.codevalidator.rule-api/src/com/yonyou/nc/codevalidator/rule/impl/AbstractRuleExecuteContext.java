package com.yonyou.nc.codevalidator.rule.impl;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleConfigContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;

/**
 * 抽象的规则执行上下文定义
 * @author mazhqa
 * @since V2.6
 */
public abstract class AbstractRuleExecuteContext implements IRuleExecuteContext{
	
	protected IRuleConfigContext ruleConfigContext;
	protected RuntimeContext runtimeContext;

	public BusinessComponent getBusinessComponent() {
		return runtimeContext.getCurrentExecuteBusinessComponent();
	}

	@Override
	public String getParameter(String parameter) {
		String parameterValue = ruleConfigContext.getProperty(parameter);
		if (parameterValue == null || parameterValue.trim().length() == 0) {
			parameterValue = ruleConfigContext.getGlobalProperty(parameter);
		}
		return parameterValue;
	}

	@Override
	public String[] getParameterArray(String parameter) {
		String parameterValue = getParameter(parameter);
		if (parameterValue == null || parameterValue.trim().length() == 0) {
			return null;
		}
		if (parameterValue.contains(",")) {
			return parameterValue.split(",");
		}
		return new String[] { parameterValue };
	}


	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}

	@Override
	public IRuleConfigContext getRuleConfigContext() {
		return ruleConfigContext;
	}

	public void setRuleConfigContext(IRuleConfigContext ruleConfigContext) {
		this.ruleConfigContext = ruleConfigContext;
	}

}
