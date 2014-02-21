package com.web.rule.page.paravo;

import java.io.Serializable;

import com.web.pub.page.handler.PubQryPara;

public class ReportDetailQryPara extends PubQryPara implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-3 上午10:11:27
	 */
	private static final long serialVersionUID = -4223407850588285958L;

	private String recordID;
	private String ruleStatus;
	private String developer;
	private String product;
	// 检查开始时间
	private String ruleCheckDateStart;
	private String ruleCheckDateEnd;

	public String getRecordID() {
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getRuleCheckDateStart() {
		return ruleCheckDateStart;
	}

	public void setRuleCheckDateStart(String ruleCheckDateStart) {
		this.ruleCheckDateStart = ruleCheckDateStart;
	}

	public String getRuleCheckDateEnd() {
		return ruleCheckDateEnd;
	}

	public void setRuleCheckDateEnd(String ruleCheckDateEnd) {
		this.ruleCheckDateEnd = ruleCheckDateEnd;
	}

}
