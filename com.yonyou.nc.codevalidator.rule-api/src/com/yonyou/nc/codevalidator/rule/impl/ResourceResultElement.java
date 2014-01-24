package com.yonyou.nc.codevalidator.rule.impl;

/**
 * 对于资源结果element的处理
 * 
 * @author mazhqa
 * @since V1.0
 */
public class ResourceResultElement {

	/**
	 * 资源路径
	 */
	private final String resourcePath;
	/**
	 * 错误详细信息
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
