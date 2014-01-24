package com.yonyou.nc.codevalidator.resparser.executeresult;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.impl.AbstractSimpleExecuteResult;

/**
 * 规则执行错误的结果，这些错误不能是异常结束的任务
 * <p>
 * 如果是异常执行的错误，请参考 com.yonyou.nc.codevalidator.resparser.executeresult.
 * ExceptionRuleExecuteResult
 * 
 * @author mazhqa
 * @since V1.0
 */
public class ErrorRuleExecuteResult extends AbstractSimpleExecuteResult {

	/**
	 * 
	 * @param identifier
	 *            规则标识符
	 * @param note
	 *            规则执行错误备注
	 */
	public ErrorRuleExecuteResult(String identifier, String note) {
		this.ruleDefinitionIdentifier = identifier;
		this.note = note;
	}

	@Override
	public RuleExecuteStatus getRuleExecuteStatus() {
		return RuleExecuteStatus.FAIL;
	}

}
