package com.yonyou.nc.codevalidator.rule.impl;

/**
 * ������Դ���element�Ĵ���
 * 
 * @author mazhqa
 * @since V1.0
 */
public class ResourceResultElement {

	/**
	 * ��Դ·��
	 */
	private final String resourcePath;
	/**
	 * ������ϸ��Ϣ
	 */
	private final String errorDetail;

	public ResourceResultElement(String resourcePath, String errorDetail) {
		this.resourcePath = resourcePath;
		this.errorDetail = errorDetail;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

}
