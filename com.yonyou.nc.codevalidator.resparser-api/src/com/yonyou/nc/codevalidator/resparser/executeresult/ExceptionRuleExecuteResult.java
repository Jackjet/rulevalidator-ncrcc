package com.yonyou.nc.codevalidator.resparser.executeresult;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.impl.AbstractSimpleExecuteResult;

/**
 * �����쳣�Ĺ���ִ�н��
 * 
 * @author mazhqa
 * @since V2.7
 */
public class ExceptionRuleExecuteResult extends AbstractSimpleExecuteResult {

	private Throwable t;

	public ExceptionRuleExecuteResult(String identifier, Throwable t) {
		this.ruleDefinitionIdentifier = identifier;
		this.t = t;
	}

	@Override
	public RuleExecuteStatus getRuleExecuteStatus() {
		return RuleExecuteStatus.EXCEPTION;
	}

	@Override
	public String getNote() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return String.format("�쳣��ϸ��ջ��Ϣ: %s", sw.toString());
	}

}
