package com.yonyou.nc.codevalidator.resparser.executeresult;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.impl.AbstractSimpleExecuteResult;

/**
 * 出现异常的规则执行结果
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
		return String.format("异常详细堆栈信息: %s", sw.toString());
	}

}
