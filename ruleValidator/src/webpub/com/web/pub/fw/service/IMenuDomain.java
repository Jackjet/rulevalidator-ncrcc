package com.web.pub.fw.service;

import java.awt.Menu;
import java.util.List;

import com.web.pub.fw.exception.PubBaseException;

public interface IMenuDomain {

	/**
	 * 根节点ID
	 */
	public static final String ROOT_ID = "root";

	/**
	 * 时间戳格式
	 */
	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 新增
	 * 
	 * @param data
	 * @return
	 * @throws WebBaseException
	 */
	public Menu insert(Menu data) throws PubBaseException;

	/**
	 * 更新
	 * 
	 * @param data
	 * @return
	 * @throws WebBaseException
	 */
	public Menu update(Menu data) throws PubBaseException;

	/**
	 * 删除（批量）
	 * 
	 * @param pks
	 *            主键
	 * @throws WebBaseException
	 */
	public void delete(String[] pks) throws PubBaseException;

	/**
	 * 详单
	 * 
	 * @param id
	 * @return
	 * @throws WebBaseException
	 */
	public Menu detail(String pk) throws PubBaseException;

	/**
	 * 查询
	 * 
	 * @param filter
	 * @return
	 * @throws WebBaseException
	 */
	// public List<Menu> query(MenuQueryFilter filter) throws PubBaseException;

	/**
	 * 获取孩子节点
	 * 
	 * @param parent
	 *            父节点
	 * 
	 * @param 是否级联查询
	 * @return
	 * @throws WebBaseException
	 */
	public List<Menu> getChildren(String parentId, boolean isCascade)
			throws PubBaseException;

}
