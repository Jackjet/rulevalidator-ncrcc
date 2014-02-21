package com.web.pub.fw.uiview;

import java.io.Serializable;

/**
 * 资源视图模型
 * 
 * @author lixln
 * 
 */
public class ResourceView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2013-12-26 下午4:20:38
	 */
	private static final long serialVersionUID = -575804002543959808L;

	/**
	 * 资源id
	 */
	private String resourceId;

	/**
	 * 资源编码
	 */
	private String resourceCode;

	/**
	 * url
	 */
	private String url;

	/**
	 * 资源名称
	 */
	private String resourceName;

	/**
	 * 安全等级
	 */
	private Integer securityLevel;

	/**
	 * 模块id
	 */
	private String moduleId;

	/**
	 * 序号
	 */
	private Integer seq;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Integer getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(Integer securityLevel) {
		this.securityLevel = securityLevel;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

}
