package com.yonyou.nc.codevalidator.rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ��һ�μ��Ự�е����м������Ϣ
 * 
 * @author mazhqa
 * @since V2.0
 */
public class SessionRuleExecuteResult {

	/**
	 * ����ִ�е�Ԫ
	 */
	private BusinessComponent businessComponent;
	
	private List<IRuleExecuteResult> ruleExecuteResults = new ArrayList<IRuleExecuteResult>();

	/**
	 * ��鿪ʼʱ��
	 */
	private Date startTime;

	/**
	 * ������ʱ��
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
