package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * 规则应用范围： ALL("所有领域"), EB("电子商务"), EP("电子采购"), SCM("供应链"), FI("财务"), AM("资产"),
 * TM("资金"), MM("流程制造"), PM("生产制造"), HR("HR"), IM("行业制造")
 * 
 * @author luoweid
 * 
 */
public enum ScopeEnum {

	ALL("所有领域"), EB("电子商务"), EP("电子采购"), SCM("供应链"), FI("财务"), AM("资产"), TM("资金"), MM("流程制造"), PM("生产制造"), HR("HR"), IM(
			"行业制造");

	private String name;

	private ScopeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
