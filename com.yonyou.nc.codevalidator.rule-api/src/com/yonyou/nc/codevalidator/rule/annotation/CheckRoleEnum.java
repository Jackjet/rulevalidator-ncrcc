package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * ȷ�Ͻ�ɫö��
 * 
 * @author luoweid
 * 
 */
public enum CheckRoleEnum {
	MUSTEXIST("�������(����)"), MAINDESIGNER("�����"), REQUIRE("����");

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
