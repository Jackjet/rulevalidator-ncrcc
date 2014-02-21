package com.web.rule.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rv_resource")
public class ResourceVO implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-13 下午1:21:47
	 */
	private static final long serialVersionUID = -6663549505428642296L;

	/**
	 * 资源id
	 */
	@Id
	@Column(name = "resourceid")
	private String resourceId;

	/**
	 * 资源编码
	 */
	@Column(name = "resourcecode")
	private String resourceCode;

	/**
	 * url
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 资源名称
	 */
	@Column(name = "resourcename")
	private String resourceName;

	/**
	 * 安全等级
	 */
	@Column(name = "securitylevel")
	private Integer securityLevel;

	/**
	 * 模块id
	 */
	@Column(name = "moduleid")
	private String moduleId;

	/**
	 * 序号
	 */
	@Column(name = "seq")
	private Integer seq;

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
