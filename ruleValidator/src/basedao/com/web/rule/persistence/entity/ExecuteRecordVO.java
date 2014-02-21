package com.web.rule.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rv_execute_record")
public class ExecuteRecordVO implements Serializable, IEntityIdentifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7758351941505231998L;

	@Id
	@Column(name = "recordid", length = 50)
	private String recordId;
	/**
	 * 执行单元
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "executeunit_unitid")
	private ExecuteUnitVO executeUnit;
	/**
	 * 执行开始时间
	 */
	@Column(name = "exe_begin_time", length = 20)
	private Date exeBeginTime;
	/**
	 * 执行结束时间
	 */
	@Column(name = "exe_end_time", length = 20, nullable = true)
	private Date exeEndTime;

	// @OneToMany(mappedBy = "executeRecord", cascade = CascadeType.ALL, fetch =
	// FetchType.LAZY)
	// @OrderBy("resultId desc")
	// private List<RuleExecuteResultVO> executeResults = new
	// ArrayList<RuleExecuteResultVO>();

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public ExecuteUnitVO getExecuteUnit() {
		return executeUnit;
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

	// public List<RuleExecuteResultVO> getExecuteResults() {
	// return executeResults;
	// }
	//
	// public void setExecuteResults(List<RuleExecuteResultVO> executeResults) {
	// this.executeResults = executeResults;
	// }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((recordId == null) ? 0 : recordId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecuteRecordVO other = (ExecuteRecordVO) obj;
		if (recordId == null) {
			if (other.recordId != null)
				return false;
		} else if (!recordId.equals(other.recordId))
			return false;
		return true;
	}

	@Override
	public String getId() {
		return recordId;
	}

}
