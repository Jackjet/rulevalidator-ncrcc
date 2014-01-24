package com.yonyou.nc.codevalidator.resparser.executeresult;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.impl.AbstractSimpleExecuteResult;

/**
 * ����ִ�д���Ľ������Щ���������쳣����������
 * <p>
 * ������쳣ִ�еĴ�����ο� com.yonyou.nc.codevalidator.resparser.executeresult.
 * ExceptionRuleExecuteResult
 * 
 * @author mazhqa
 * @since V1.0
 */
public class ErrorRuleExecuteResult extends AbstractSimpleExecuteResult {

	/**
	 * 
	 * @param identifier
	 *            �����ʶ��
	 * @param note
	 *            ����ִ�д���ע
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
