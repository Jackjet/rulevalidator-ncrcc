package com.yonyou.nc.codevalidator.rule.impl;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;

/**
 * 由于执行环境ExecutePeriod原因而导致的放弃执行的规则结果
 * 
 * @author mazhqa
 * @since V2.6
 */
public class IgnoreRuleExecuteResult extends AbstractSimpleExecuteResult {

	private ExecutePeriod currentExecutePeriod;
	private ExecutePeriod ruleExecutePeriod;

	/**
	 * 
	 * @param identifier
	 * @param ruleExecutePeriod
	 * @param currentExecutePeriod
	 */
	public IgnoreRuleExecuteResult(String identifier, ExecutePeriod ruleExecutePeriod,
			ExecutePeriod currentExecutePeriod) {
		this.ruleDefinitionIdentifier = identifier;
		this.currentExecutePeriod = currentExecutePeriod;
		this.ruleExecutePeriod = ruleExecutePeriod;
	}

	@Override
	public String getNote() {
		return String.format("执行环境(%s)级别较低，本规则环境(%s)较高，暂不执行", currentExecutePeriod.getName(),
				ruleExecutePeriod.getName());
	}

	@Override
	public String toString() {
		return getResult();
	}

	@Override
	public RuleExecuteStatus getRuleExecuteStatus() {
		return RuleExecuteStatus.IGNORED;
	}
}
