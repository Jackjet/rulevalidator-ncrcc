package com.yonyou.nc.codevalidator.resparser.executeresult;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.impl.AbstractSimpleExecuteResult;

/**
 * 规则执行正确结果
 * 
 * @author mazhqa
 * @since V1.0
 */
public class SuccessRuleExecuteResult extends AbstractSimpleExecuteResult {

	public SuccessRuleExecuteResult(String identifier) {
		this.ruleDefinitionIdentifier = identifier;
	}

	@Override
	public String getNote() {
		return RESULT_SUCCESS;
	}

	@Override
	public RuleExecuteStatus getRuleExecuteStatus() {
		return RuleExecuteStatus.SUCCESS;
	}
}
