package com.web.pub.fw.uiview;

import java.io.Serializable;

public class MenuQueryFilterView extends UIView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2013-12-26 下午4:20:23
	 */
	private static final long serialVersionUID = -7554901906278324595L;

	/**
	 * 父节点id
	 */
	private String[] parentId;

	/**
	 * 菜单id
	 */
	private String[] menuId;

	/**
	 * 资源id
	 */
	private String[] resourceId;

	/**
	 * 菜单标题
	 */
	private String menuTitle;

	public String[] getParentId() {
		return parentId;
	}

	public void setParentId(String[] parentId) {
		this.parentId = parentId;
	}

	public String[] getMenuId() {
		return menuId;
	}

	public void setMenuId(String[] menuId) {
		this.menuId = menuId;
	}

	public String[] getResourceId() {
		return resourceId;
	}

	public void setResourceId(String[] resourceId) {
		this.resourceId = resourceId;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

}
