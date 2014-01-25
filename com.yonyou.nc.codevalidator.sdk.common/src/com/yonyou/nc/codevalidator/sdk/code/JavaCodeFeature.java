package com.yonyou.nc.codevalidator.sdk.code;

/**
 * Java代码的特性，用来描述java代码和.class类所在的模块，业务组件和资源权限
 * @author mazhqa
 * @since V2.0
 */
public final class JavaCodeFeature {
	
	/**
	 * 该类型所属于的Public/Private/Client特性，其他设置为SRC
	 */
	private final JavaResPrivilege resPrivilege;
	/**
	 * 所在的模块名称
	 */
	private final String moduleName;
	/**
	 * 所在的业务组件名称
	 */
	private final String busiCompName;
	
	public JavaCodeFeature(JavaResPrivilege resPrivilege, String moduleName, String busiCompName) {
		this.resPrivilege = resPrivilege;
		this.moduleName = moduleName;
		this.busiCompName = busiCompName;
	}

	public JavaResPrivilege getResPrivilege() {
		return resPrivilege;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getBusiCompName() {
		return busiCompName;
	}
	
}
