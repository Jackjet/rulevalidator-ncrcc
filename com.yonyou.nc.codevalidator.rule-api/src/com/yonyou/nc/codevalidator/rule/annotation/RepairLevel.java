package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * ��������ɺ󣬴���������Եȼ��� MUSTREPAIR("�����޸�"),SUGGESTREPAIR("�����޸�")
 * 
 * @author luoweid
 * 
 */
public enum RepairLevel {

	MUSTREPAIR("�����޸�"), SUGGESTREPAIR("�����޸�");

	private String name;

	private RepairLevel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
