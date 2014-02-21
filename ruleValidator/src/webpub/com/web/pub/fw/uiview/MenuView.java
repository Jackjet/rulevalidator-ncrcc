package com.web.pub.fw.uiview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuView extends UIView implements Serializable {

	/**
	 * @author:sunshine
	 * @date:2013-12-26 下午4:20:13
	 */
	private static final long serialVersionUID = -6493631311462034564L;

	/**
	 * 菜单id
	 */
	private String menuId;

	/**
	 * 菜单标题
	 */
	private String menuTitle;

	/**
	 * 资源id
	 */
	private String resourceId;

	/**
	 * 父节点id
	 */
	private String parentId;

	private MenuView parentView;

	/**
	 * 排序
	 */
	private Integer seq;

	/**
	 * 是否隐藏
	 */
	private Boolean hide;

	/**
	 * 资源
	 */
	private ResourceView resourceView;

	/**
	 * 孩子节点
	 */
	private List<MenuView> childrenView;

	/**
	 * 是否为叶子节点
	 */
	private Boolean leaf;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MenuView() {
		super();
		childrenView = new ArrayList<MenuView>();
	}

	public List<MenuView> getChildrenView() {
		return childrenView;
	}

	public void setChildrenView(List<MenuView> childrenView) {
		this.childrenView = childrenView;
	}

	public ResourceView getResourceView() {
		return resourceView;
	}

	public void setResourceView(ResourceView resourceView) {
		this.resourceView = resourceView;
	}

	public Boolean getHide() {
		return hide;
	}

	public void setHide(Boolean hide) {
		this.hide = hide;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public MenuView getParentView() {
		return parentView;
	}

	public void setParentView(MenuView parentView) {
		this.parentView = parentView;
	}

}
