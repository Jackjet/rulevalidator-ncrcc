package com.web.rule.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.web.pub.fw.uiview.UIView;
import com.web.rule.persistence.entity.ExecuteUnitVO;
import com.web.rule.persistence.entity.RuleExecuteResultVO;

public class ExecuteRecordView extends UIView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-3 下午4:15:25
	 */
	private static final long serialVersionUID = 547187056385627569L;
	private String recordId;
	/**
	 * 执行单元
	 */
	private ExecuteUnitVO executeUnit;
	/**
	 * 执行开始时间
	 */
	private Date exeBeginTime;
	/**
	 * 执行结束时间
	 */
	private Date exeEndTime;

	private List<RuleExecuteResultVO> executeResults = new ArrayList<RuleExecuteResultVO>();

	public ExecuteUnitVO getExecuteUnit() {
		return executeUnit;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public void setExecuteUnit(ExecuteUnitVO executeUnit) {
		this.executeUnit = executeUnit;
	}

	public Date getExeBeginTime() {
		return exeBeginTime;
	}

	public void setExeBeginTime(Date exeBeginTime) {
		this.exeBeginTime = exeBeginTime;
	}

	public Date getExeEndTime() {
		return exeEndTime;
	}

	public void setExeEndTime(Date exeEndTime) {
		this.exeEndTime = exeEndTime;
	}

	public List<RuleExecuteResultVO> getExecuteResults() {
		return executeResults;
	}

	public void setExecuteResults(List<RuleExecuteResultVO> executeResults) {
		this.executeResults = executeResults;
	}

}
