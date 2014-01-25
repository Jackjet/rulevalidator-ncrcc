package com.yonyou.nc.codevalidator.rule.impl;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;

/**
 * 抽象规则执行结果，建议所有的规则执行结果类型都从该类中派生
 * 
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractRuleExecuteResult implements IRuleExecuteResult {

	protected static final String RESULT_SUCCESS = "规则执行成功";
	protected static final String RESULT_FAIL = "执行失败";

	protected String ruleDefinitionIdentifier;
	protected IRuleExecuteContext ruleExecuteContext;

	protected boolean success;
	protected String result;
	protected String note;

	protected BusinessComponent businessComponent;

	// @Override
	// public boolean isSuccess() {
	// return success;
	// }

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRuleDefinitionIdentifier() {
		return ruleDefinitionIdentifier;
	}

	public void setRuleDefinitionIdentifier(String ruleDefinitionIdentifier) {
		this.ruleDefinitionIdentifier = ruleDefinitionIdentifier;
	}

	@Override
	public String getNote() {
		return note;
	}

	public IRuleExecuteContext getRuleExecuteContext() {
		return ruleExecuteContext;
	}

	public void setRuleExecuteContext(IRuleExecuteContext ruleExecuteContext) {
		this.ruleExecuteContext = ruleExecuteContext;
	}

	@Override
	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

	@Override
	public void setBusinessComponent(BusinessComponent businessComponent) {
		this.businessComponent = businessComponent;
	}

	@Override
	public final String getResult() {
		return getRuleExecuteStatus().getMessage();
	}

	@Override
	public String toString() {
		return String.format("%s %s \n %s",
				ruleDefinitionIdentifier == null || ruleDefinitionIdentifier.trim().equals("") ? "" : "插件id："
						+ ruleDefinitionIdentifier + "\n", getResult(), getNote() == null
						|| getNote().trim().equals("") ? "" : "具体信息如下: " + getNote());
	}
}
