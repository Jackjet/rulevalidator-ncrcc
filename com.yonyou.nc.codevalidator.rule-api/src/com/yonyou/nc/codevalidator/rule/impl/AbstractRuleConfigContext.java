package com.yonyou.nc.codevalidator.rule.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.rule.IRuleConfigContext;
import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;

public abstract class AbstractRuleConfigContext implements IRuleConfigContext {

	private String ruleDefinitionId;
	protected Map<String, String> properties = new HashMap<String, String>();
	protected Map<String, String> globalProperties = new HashMap<String, String>();
	
	protected RuleExecuteLevel ruleExecuteLevel;

	public void setRuleDefinitionIdentifier(String ruleDefinitionIdentifier) {
		this.ruleDefinitionId = ruleDefinitionIdentifier;
	}

	@Override
	public RuleExecuteLevel getRuleExecuteLevel() {
		return ruleExecuteLevel;
	}

	@Override
	public void setRuleExecuteLevel(RuleExecuteLevel ruleExecuteLevel) {
		this.ruleExecuteLevel = ruleExecuteLevel;
	}

	@Override
	public String getRuleDefinitionIdentifier() {
		return ruleDefinitionId;
	}

	@Override
	public String getGlobalProperty(String property) {
		return globalProperties.get(property);
	}

	/**
	 * 设置规则执行上下文的全局配置属性
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setGlobalProperty(String propertyName, String propertyValue) {
		globalProperties.put(propertyName, propertyValue);
	}

	@Override
	public List<String> getGlobalPropertyNames() {
		return Collections.unmodifiableList(new ArrayList<String>(globalProperties.keySet()));
	}

	@Override
	public String getProperty(String property) {
		return properties.get(property);
	}

	/**
	 * 设置规则配置上下文的私有属性
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void setProperty(String propertyName, String propertyValue) {
		properties.put(propertyName, propertyValue);
	}

	@Override
	public List<String> getParameterNames() {
		return Collections.unmodifiableList(new ArrayList<String>(properties.keySet()));
	}

	@Override
	public String getSpecialParamString() {
		StringBuilder specialParameterBuilder = new StringBuilder();
		List<String> specialParameterNames = getParameterNames();
		for (String parameterName : specialParameterNames) {
			specialParameterBuilder.append(parameterName).append("=").append(getProperty(parameterName)).append(",");
		}
		return specialParameterBuilder.toString();
	}

}
