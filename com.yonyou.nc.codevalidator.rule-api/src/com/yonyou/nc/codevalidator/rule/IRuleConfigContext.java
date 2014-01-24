package com.yonyou.nc.codevalidator.rule;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;

/**
 * 用于任务配置的上下文 
 * @author mazhqa
 * @since V1.0
 */
public interface IRuleConfigContext {

	/**
	 * 得到规则定义的唯一标识符
	 * 
	 * @return
	 */
	String getRuleDefinitionIdentifier();
	
	/**
	 * 规则执行的全局配置参数，这些配置参数是所有规则共享的（仅存在同一份）
	 * 
	 * @param property - 配置参数的名称
	 * @return
	 */
	String getGlobalProperty(String property);

	/**
	 * 得到全局配置参数的属性名称列表
	 * @return
	 */
	List<String> getGlobalPropertyNames();
	
	/**
	 * 单条规则的配置项
	 * @param property
	 * @return
	 */
	String getProperty(String property);

	/**
	 * 得到单条规则键值列表
	 * @return
	 */
	List<String> getParameterNames();
	
	/**
	 * 得到特殊参数的String字符串表示
	 * @return
	 */
	String getSpecialParamString();
	
	RuleExecuteLevel getRuleExecuteLevel();
	
	void setRuleExecuteLevel(RuleExecuteLevel ruleExecuteLevel);

}
