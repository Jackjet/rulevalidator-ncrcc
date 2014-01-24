package com.yonyou.nc.codevalidator.rule.vo;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleConfigContext;

/**
 * 抽象的规则配置结果，用于在实际执行时生成执行的规则配置
 * @author mazhqa
 * @since V2.6
 */
public interface IRuleCheckConfiguration extends Comparable<IRuleCheckConfiguration>{
	
	int GLOBAL_PRIORITY = 10;
	int MODULE_PRIORITY = 20;
	int BUSICOMP_PRIORITY = 30;

	CommonParamConfiguration getCommonParamConfiguration();

	List<RuleItemConfigVO> getRuleItemConfigVoList();
	
	/**
	 * 得到merge的优先级，用于合并规则时以高优先级的规则配置为准
	 * @return
	 */
	int getPriority();
	
	/**
	 * 设置规则执行的优先级
	 * @param priority
	 */
	void setPriority(int priority);
	
	/**
	 * 将规则检查配置的内容转换成对应配置上下文
	 * @return
	 */
	List<IRuleConfigContext> toRuleConfigContexts();
	
	/**
	 * 合并规则配置，如果相同则以本对象中的配置优先
	 * @param ruleCheckConfiguration
	 */
	void addRuleCheckConfiguration(IRuleCheckConfiguration ruleCheckConfiguration);
	
}
