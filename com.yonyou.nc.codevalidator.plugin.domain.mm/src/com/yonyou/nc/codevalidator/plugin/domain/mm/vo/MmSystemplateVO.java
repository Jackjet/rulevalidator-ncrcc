package com.yonyou.nc.codevalidator.plugin.domain.mm.vo;

import java.io.Serializable;

/**
 * 系统模板VO，只是用来传递数据
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmSystemplateVO implements Serializable {

	/**
	 * ID
	 */
	private static final long serialVersionUID = 94377387271951609L;

	public static final String DEVORG = "devorg";
	public static final String FUNNODE = "funnode";
	public static final String LAYER = "layer";
	public static final String MODULEID = "moduleid";
	public static final String NODEKEY = "nodekey";
	public static final String PK_SYSTEMPLATE = "pk_systemplate";
	public static final String TEMPLATEID = "templateid";
	public static final String TEMPSTYLE = "tempstyle";

	private String funnode;

	private String nodekey;

	private String pk_corp;

	private String pk_org;

	private String templateflag;

	private String templateid;

	private Integer tempstyle;

	private String moduleid;

	private Integer layer;

	public String getFunnode() {
		return funnode;
	}

	public void setFunnode(String funnode) {
		this.funnode = funnode;
	}

	public String getNodekey() {
		return nodekey;
	}

	public void setNodekey(String nodekey) {
		this.nodekey = nodekey;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getTemplateflag() {
		return templateflag;
	}

	public void setTemplateflag(String templateflag) {
		this.templateflag = templateflag;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public Integer getTempstyle() {
		return tempstyle;
	}

	public void setTempstyle(Integer tempstyle) {
		this.tempstyle = tempstyle;
	}

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
