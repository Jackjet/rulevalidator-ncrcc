package com.yonyou.nc.codevalidator.rule.annotation;

/**
 * ��Ŀ(��������)�� METADATA("Ԫ���ݹ���"), UIFACTORY("UI�������ù���"),
 * THIRDPARTYJAR("������jar������"), LANG("�������"), CREATESCRIPT("����ű�����"),
 * PRESCRIPT("Ԥ�ƽű�����"), OTHERCONFIGFILE("���������ļ�����"), WEBDEVELOP("web������ع���");
 * 
 * @author luoweid
 * 
 */
public enum CatalogEnum {

	JAVACODE("�������"), METADATA("Ԫ���ݹ���"), UIFACTORY("UI�������ù���"), THIRDPARTYJAR("������jar������"), LANG("�������"), CREATESCRIPT(
			"����ű�����"), PRESCRIPT("Ԥ�ƽű�����"), OTHERCONFIGFILE("���������ļ�����"), WEBDEVELOP("web������ع���");

	private String name;

	private CatalogEnum(String name) {
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
