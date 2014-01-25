package com.yonyou.nc.codevalidator.sdk.code;

/**
 * Java资源访问权限, 默认为SRC，无任何访问权限，否则必须指定为PUBLIC/CLIENT/PRIVATE
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