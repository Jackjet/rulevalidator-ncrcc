package com.web.pub.page.paravo;

import java.io.Serializable;

import com.web.pub.page.handler.PubQryPara;

public class MenusQryParaVO extends PubQryPara implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2014-1-24 ÉÏÎç11:27:04
	 */
	private static final long serialVersionUID = -4097473392688127651L;

	private String pkMenu;
	private String menuName;
	private String resCode;
	private String resName;
	private Boolean isRoot;

	public String getPkMenu() {
		return pkMenu;
	}

	public void setPkMenu(String pkMenu) {
		this.pkMenu = pkMenu;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

}
