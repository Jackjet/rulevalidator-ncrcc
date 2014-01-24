package com.yonyou.nc.codevalidator.sdk.code;

/**
 * Java��Դ����Ȩ��, Ĭ��ΪSRC�����κη���Ȩ�ޣ��������ָ��ΪPUBLIC/CLIENT/PRIVATE
 * @author mazhqa
 * @since V1.0
 */
public enum JavaResPrivilege {
	
	SRC("src"), PUBLIC("public"), CLIENT("client"), PRIVATE("private");

	JavaResPrivilege(String resFolder) {
		JavaResPrivilege.this.resFolder = resFolder;
	}

	private String resFolder;

	public String resFolder() {
		return resFolder;
	}
}