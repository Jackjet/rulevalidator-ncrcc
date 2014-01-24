package com.yonyou.nc.codevalidator.export.api;

import java.io.Serializable;
import java.util.Date;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class RuleExportContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7256912364958144859L;

	private String recordID;
	private Date startTime;
	private Date endTime;
	private BusinessComponent businessComponent;

	public String getRecordID() {
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public Date getStartTime() {
		return startTime;
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

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

	public void setBusinessComponent(BusinessComponent businessComponent) {
		this.businessComponent = businessComponent;
	}

}
