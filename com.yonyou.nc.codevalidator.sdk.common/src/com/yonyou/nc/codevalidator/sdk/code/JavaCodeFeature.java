package com.yonyou.nc.codevalidator.sdk.code;

/**
 * Java��������ԣ���������java�����.class�����ڵ�ģ�飬ҵ���������ԴȨ��
 * @author mazhqa
 * @since V2.0
 */
public final class JavaCodeFeature {
	
	/**
	 * �����������ڵ�Public/Private/Client���ԣ���������ΪSRC
	 */
	private final JavaResPrivilege resPrivilege;
	/**
	 * ���ڵ�ģ������
	 */
	private final String moduleName;
	/**
	 * ���ڵ�ҵ���������
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
