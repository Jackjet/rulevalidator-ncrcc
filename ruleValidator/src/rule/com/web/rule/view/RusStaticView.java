package com.web.rule.view;

import java.io.Serializable;

import com.web.pub.fw.uiview.UIView;

public class RusStaticView extends UIView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2013-12-27 下午3:34:40
	 */
	private static final long serialVersionUID = 4385182630008343374L;

	private String prodCode;
	private String prodName;
	private String moduleCode;
	private String moduleName;
	private String compCode;
	private String compName;
	private String ruleCount;
	private String passRate;
	private String checkDate;

	// 记录主键
	private String recordID;
	// 规则执行时间
	private String recordDate;
	// 状态
	private String resultStatu;
	// 数量
	private String statusNum;

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getRuleCount() {
		return ruleCount;
	}

	public void setRuleCount(String ruleCount) {
		this.ruleCount = ruleCount;
	}

	public String getPassRate() {
		return passRate;
	}

	public void setPassRate(String passRate) {
		this.passRate = passRate;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getRecordID() {
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getResultStatu() {
		return resultStatu;
	}

	public void setResultStatu(String resultStatu) {
		this.resultStatu = resultStatu;
	}

	public String getStatusNum() {
		return statusNum;
	}

	public void setStatusNum(String statusNum) {
		this.statusNum = statusNum;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

}
