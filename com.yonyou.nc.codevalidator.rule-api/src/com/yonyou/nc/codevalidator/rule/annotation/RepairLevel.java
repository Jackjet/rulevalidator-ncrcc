package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * 规则检查完成后，错误的严重性等级： MUSTREPAIR("必须修改"),SUGGESTREPAIR("建议修改")
 * 
 * @author luoweid
 * 
 */
public enum RepairLevel {

	MUSTREPAIR("必须修改"), SUGGESTREPAIR("建议修改");

	private String name;

	private RepairLevel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
