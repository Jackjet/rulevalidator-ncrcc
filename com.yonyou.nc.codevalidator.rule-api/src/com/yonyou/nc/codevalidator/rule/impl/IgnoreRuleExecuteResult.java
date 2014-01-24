package com.yonyou.nc.codevalidator.rule.impl;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;

/**
 * ����ִ�л���ExecutePeriodԭ������µķ���ִ�еĹ�����
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
		return String.format("ִ�л���(%s)����ϵͣ������򻷾�(%s)�ϸߣ��ݲ�ִ��", currentExecutePeriod.getName(),
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
