package com.web.pub.src.dao;

import java.util.List;

import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.page.paravo.MenusQryParaVO;
import com.web.pub.page.paravo.ResourQryParaVO;
import com.web.rule.persistence.entity.MenuVO;
import com.web.rule.persistence.entity.ResourceVO;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-13 下午2:45:20
 */
public interface IWebSrcDao {

	/**
	 * 查询素有根节点
	 * 
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-13 下午2:49:48
	 */
	public List<MenuVO> qryRootMenuVO() throws PubBusinessException;

	/**
	 * 查询所有二级菜单（菜单只支持两级）
	 * 
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-13 下午2:49:59
	 */
	public List<MenuVO> qryLeafMenuVO() throws PubBusinessException;

	/**
	 * 根据主键查询菜单VO
	 * 
	 * @param menuID
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-12 上午9:37:46
	 */
	public MenuVO qryMenuVOByID(String menuID) throws PubBusinessException;

	/**
	 * 插入预置脚本
	 * 
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-21 下午3:20:01
	 */
	public void initMenuData() throws PubBusinessException;

	/**
	 * 新增资源
	 * 
	 * @param recourceVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:34:17
	 */
	public ResourceVO addResource(ResourceVO recourceVO)
			throws PubBusinessException;

	/**
	 * 删除资源
	 * 
	 * @param recourceVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:34:14
	 */
	public ResourceVO delResource(ResourceVO recourceVO)
			throws PubBusinessException;

	/**
	 * 修改资源
	 * 
	 * @param recourceVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:34:11
	 */
	public ResourceVO updateResource(ResourceVO recourceVO)
			throws PubBusinessException;

	/**
	 * 根据条件查询资源
	 * 
	 * @param resQryParaVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:34:08
	 */
	public List<ResourceVO> getResource(ResourQryParaVO resQryParaVO)
			throws PubBusinessException;

	/**
	 * 新增菜单
	 * 
	 * @param recourceVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:34:05
	 */
	public MenuVO addMenus(MenuVO menuVO) throws PubBusinessException;

	/**
	 * 删除菜单
	 * 
	 * @param recourceVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:34:02
	 */
	public MenuVO delMenus(MenuVO menuVO) throws PubBusinessException;

	/**
	 * 修改菜单
	 * 
	 * @param recourceVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:33:59
	 */
	public MenuVO updateMenus(MenuVO menuVO) throws PubBusinessException;

	/**
	 * 根据条件查询菜单
	 * 
	 * @param menuQryParaVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-11 下午3:33:56
	 */
	public List<MenuVO> getMenus(MenusQryParaVO menuQryParaVO)
			throws PubBusinessException;

	/**
	 * 根据主键查询菜单
	 * 
	 * @param sourceID
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-2-12 下午2:49:49
	 */
	public ResourceVO qrySourceVOByID(String sourceID)
			throws PubBusinessException;
}
