package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * 确认角色枚举
 * 
 * @author luoweid
 * 
 */
public enum CheckRoleEnum {
	MUSTEXIST("必须存在(所有)"), MAINDESIGNER("主设计"), REQUIRE("需求");

	private String name;

	private CheckRoleEnum(String name) {
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
