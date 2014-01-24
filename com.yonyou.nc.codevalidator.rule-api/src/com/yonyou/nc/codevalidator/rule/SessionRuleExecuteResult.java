package com.yonyou.nc.codevalidator.rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 在一次检查会话中的所有检查结果信息
 * 
 * @author mazhqa
 * @since V2.0
 */
public class SessionRuleExecuteResult {

	/**
	 * 最顶层的执行单元
	 */
	private BusinessComponent businessComponent;
	
	private List<IRuleExecuteResult> ruleExecuteResults = new ArrayList<IRuleExecuteResult>();

	/**
	 * 检查开始时间
	 */
	private Date startTime;

	/**
	 * 检查结束时间
	 */
	private Date endTime;

	public Date getStartTime() {
		return startTime;
	}

	public List<IRuleExecuteResult> getRuleExecuteResults() {
		return ruleExecuteResults;
	}

	public void setRuleExecuteResults(List<IRuleExecuteResult> ruleExecuteResults) {
		this.ruleExecuteResults = ruleExecuteResults;
	}

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

	public void setBusinessComponent(BusinessComponent businessComponent) {
		this.businessComponent = businessComponent;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
