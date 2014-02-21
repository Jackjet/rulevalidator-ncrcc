package com.web.pub.src.service;

import java.util.List;

import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.fw.uiview.MenuView;
import com.web.pub.fw.uiview.ResourceView;
import com.web.pub.page.paravo.MenusQryParaVO;
import com.web.pub.page.paravo.ResourQryParaVO;

/**
 * 获取资源服务
 * 
 * @author:sunshine
 * @date:2014-1-13 下午2:43:29
 */
public interface IWebSrcService {

	/**
	 * 查询所有菜信息
	 * 
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-13 下午2:46:42
	 */
	public List<MenuView> qryMenuVO() throws PubBusinessException;

	/**
	 * 插入预置脚本
	 * 
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-21 下午3:18:55
	 */
	public void initMenuData() throws PubBusinessException;

	/**
	 * 新增资源
	 * 
	 * @param resourceView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:36
	 */
	public ResourceView addResource(ResourceView resourceView)
			throws PubBusinessException;

	/**
	 * 删除资源
	 * 
	 * @param resourceView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:38
	 */
	public ResourceView delResource(ResourceView resourceView)
			throws PubBusinessException;

	/**
	 * 修改资源
	 * 
	 * @param resourceView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:42
	 */
	public ResourceView updateResource(ResourceView resourceView)
			throws PubBusinessException;

	/**
	 * 根据条件查询资源
	 * 
	 * @param resQryParaVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:45
	 */
	public List<ResourceView> getResource(ResourQryParaVO resQryParaVO)
			throws PubBusinessException;

	/**
	 * 新增菜单
	 * 
	 * @param menuView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:48
	 */
	public MenuView addMenus(MenuView menuView) throws PubBusinessException;

	/**
	 * 删除菜单
	 * 
	 * @param menuView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:52
	 */
	public MenuView delMenus(MenuView menuView) throws PubBusinessException;

	/**
	 * 修改菜单
	 * 
	 * @param menuView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:55
	 */
	public MenuView updateMenus(MenuView menuView) throws PubBusinessException;

	/**
	 * 根据条件查询菜单
	 * 
	 * @param menuQryParaVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:20:58
	 */
	public List<MenuView> getMenus(MenusQryParaVO menuQryParaVO)
			throws PubBusinessException;

}
