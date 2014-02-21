package com.web.pub.src.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.fw.service.PubBaseService;
import com.web.pub.fw.uiview.MenuView;
import com.web.pub.fw.uiview.ResourceView;
import com.web.pub.page.paravo.MenusQryParaVO;
import com.web.pub.page.paravo.ResourQryParaVO;
import com.web.pub.src.dao.IWebSrcDao;
import com.web.pub.src.service.IWebSrcService;
import com.web.rule.persistence.entity.MenuVO;
import com.web.rule.persistence.entity.ResourceVO;

/**
 * 获取资源服务
 * 
 * @author:sunshine
 * @date:2014-1-13 下午2:43:16
 */
@Service
public class WebSrcServiceImpl extends PubBaseService implements IWebSrcService {

	@Resource
	private IWebSrcDao webSrcDao;

	@Override
	public List<MenuView> qryMenuVO() throws PubBusinessException {
		List<MenuView> meanVOList = null;
		List<MenuVO> rootMenuVO = webSrcDao.qryRootMenuVO();
		List<MenuVO> leafMenuVO = webSrcDao.qryLeafMenuVO();
		if (rootMenuVO != null) {
			HashMap<String, MenuVO> rootMap = new HashMap<String, MenuVO>();
			HashMap<String, List<MenuVO>> rootLeafMap = new HashMap<String, List<MenuVO>>();
			for (MenuVO rootMenu : rootMenuVO) {
				rootMap.put(rootMenu.getMenuId(), rootMenu);
				rootLeafMap.put(rootMenu.getMenuId(), null);
			}
			if (leafMenuVO != null) {
				for (MenuVO leafMenu : leafMenuVO) {
					List<MenuVO> leafMenuList = rootLeafMap.get(leafMenu
							.getParentId());
					if (leafMenuList == null) {
						leafMenuList = new ArrayList<MenuVO>();
					}
					leafMenuList.add(leafMenu);
					rootLeafMap.put(leafMenu.getParentId(), leafMenuList);
				}
			}
			meanVOList = new ArrayList<MenuView>();
			Iterator<Entry<String, MenuVO>> it = rootMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, MenuVO> entry = it.next();
				String rootKey = entry.getKey();
				MenuVO rootMenu = entry.getValue();
				List<MenuVO> menuVOList = rootLeafMap.get(rootKey);
				MenuView rootView = convert2RootView(rootMenu);
				if (!StringUtils.isEmpty(rootView.getParentId())) {
					rootView.setParentView(convert2RootView(webSrcDao
							.qryMenuVOByID(rootView.getParentId())));
				}
				List<MenuView> childrenView = convert2RootView(menuVOList);
				rootView.setChildrenView(childrenView);
				meanVOList.add(rootView);
			}
		}
		return meanVOList;
	}

	/**
	 * 菜单VO转换
	 * 
	 * @param rootMenuVO
	 * @param menuVOList
	 * @return
	 * @author:sunshine
	 * @date:2014-1-13 下午3:15:02
	 */
	private MenuView convert2RootView(MenuVO rootMenuVO) {
		MenuView rootMeanView = null;
		if (rootMenuVO != null) {
			rootMeanView = new MenuView();

			rootMeanView.setMenuId(rootMenuVO.getMenuId());
			rootMeanView.setMenuTitle(rootMenuVO.getMenuTitle());
			rootMeanView.setParentId(rootMenuVO.getParentId());
			if (!StringUtils.isEmpty(rootMenuVO.getParentId())) {
				rootMeanView.setParentView(convert2RootView(webSrcDao
						.qryMenuVOByID(rootMenuVO.getParentId())));
			}
			rootMeanView.setResourceId(rootMenuVO.getResourceVO()
					.getResourceId());
			rootMeanView.setSeq(rootMenuVO.getSeq());
			rootMeanView
					.setHide("1".equals(rootMenuVO.getHide()) ? Boolean.TRUE
							: Boolean.FALSE);
			rootMeanView
					.setLeaf("1".equals(rootMenuVO.getLeaf()) ? Boolean.TRUE
							: Boolean.FALSE);
			ResourceView resourceVO = convert2ResView(rootMenuVO
					.getResourceVO());
			rootMeanView.setResourceView(resourceVO);
		}
		return rootMeanView;
	}

	/**
	 * 菜单VO转换
	 * 
	 * @param menuVOList
	 * @return
	 * @author:sunshine
	 * @date:2014-1-13 下午3:14:59
	 */
	private List<MenuView> convert2RootView(List<MenuVO> menuVOList) {
		List<MenuView> menViewList = null;
		if (menuVOList != null) {
			menViewList = new ArrayList<MenuView>();
			for (MenuVO menuVO : menuVOList) {
				MenuView menuView = convert2RootView(menuVO);
				menViewList.add(menuView);
			}
		}
		return menViewList;
	}

	/**
	 * 资源VO转换
	 * 
	 * @param resourceVO
	 * @return
	 * @author:sunshine
	 * @date:2014-1-13 下午3:14:56
	 */
	private ResourceView convert2ResView(ResourceVO resourceVO) {
		ResourceView resView = null;
		if (resourceVO != null) {
			resView = new ResourceView();
			resView.setModuleId(resourceVO.getModuleId());
			resView.setResourceCode(resourceVO.getResourceCode());
			resView.setResourceId(resourceVO.getResourceId());
			resView.setResourceName(resourceVO.getResourceName());
			resView.setSecurityLevel(resourceVO.getSecurityLevel());
			resView.setSeq(resourceVO.getSeq());
			resView.setUrl(resourceVO.getUrl());
		}
		return resView;
	}

	private List<ResourceView> convert2ResView(List<ResourceVO> resourceVOList) {
		List<ResourceView> resList = null;
		if (resourceVOList != null) {
			resList = new ArrayList<ResourceView>();
			for (ResourceVO resourceVO : resourceVOList) {
				ResourceView resView = convert2ResView(resourceVO);
				resList.add(resView);
			}
		}
		return resList;
	}

	/**
	 * 插入预置脚本
	 * 
	 * @author:sunshine
	 * @date:2014-1-21 下午3:19:34
	 * @see com.web.pub.src.service.IWebSrcService#initMenuData()
	 */
	@Override
	public void initMenuData() throws PubBusinessException {
		webSrcDao.initMenuData();
	}

	/**
	 * 新增资源
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:22:44
	 * @see com.web.pub.src.service.IWebSrcService#addResource(com.web.pub.fw.uiview.ResourceView)
	 */
	@Override
	public ResourceView addResource(ResourceView resourceView)
			throws PubBusinessException {
		ResourceVO recourceVO = convertResVO(resourceView);
		recourceVO = webSrcDao.addResource(recourceVO);
		return resourceView;
	}

	/**
	 * 删除资源
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:22:47
	 * @see com.web.pub.src.service.IWebSrcService#delResource(com.web.pub.fw.uiview.ResourceView)
	 */
	@Override
	public ResourceView delResource(ResourceView resourceView)
			throws PubBusinessException {
		ResourceVO recourceVO = convertResVO(resourceView);
		recourceVO = webSrcDao.delResource(recourceVO);
		return resourceView;
	}

	/**
	 * 修改资源
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:22:50
	 * @see com.web.pub.src.service.IWebSrcService#updateResource(com.web.pub.fw.uiview.ResourceView)
	 */
	@Override
	public ResourceView updateResource(ResourceView resourceView)
			throws PubBusinessException {
		ResourceVO recourceVO = convertResVO(resourceView);
		recourceVO = webSrcDao.updateResource(recourceVO);
		return resourceView;
	}

	/**
	 * 根据条件查询资源
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:22:54
	 * @see com.web.pub.src.service.IWebSrcService#getResource(com.web.pub.page.paravo.ResourQryParaVO)
	 */
	@Override
	public List<ResourceView> getResource(ResourQryParaVO resQryParaVO)
			throws PubBusinessException {
		List<ResourceVO> resList = webSrcDao.getResource(resQryParaVO);
		return convert2ResView(resList);
	}

	/**
	 * 新增菜单
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:22:58
	 * @see com.web.pub.src.service.IWebSrcService#addMenus(com.web.pub.fw.uiview.MenuView)
	 */
	@Override
	public MenuView addMenus(MenuView menuView) throws PubBusinessException {
		MenuVO recourceVO = convertMenuVO(menuView);
		recourceVO = webSrcDao.addMenus(recourceVO);
		return menuView;
	}

	/**
	 * 删除菜单
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:23:01
	 * @see com.web.pub.src.service.IWebSrcService#delMenus(com.web.pub.fw.uiview.MenuView)
	 */
	@Override
	public MenuView delMenus(MenuView menuView) throws PubBusinessException {
		MenuVO recourceVO = convertMenuVO(menuView);
		recourceVO = webSrcDao.delMenus(recourceVO);
		return menuView;
	}

	/**
	 * 修改菜单
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:23:04
	 * @see com.web.pub.src.service.IWebSrcService#updateMenus(com.web.pub.fw.uiview.MenuView)
	 */
	@Override
	public MenuView updateMenus(MenuView menuView) throws PubBusinessException {
		MenuVO recourceVO = convertMenuVO(menuView);
		recourceVO = webSrcDao.updateMenus(recourceVO);
		return menuView;
	}

	/**
	 * 根据条件查询菜单
	 * 
	 * @author:sunshine
	 * @date:2014-2-11 下午3:23:08
	 * @see com.web.pub.src.service.IWebSrcService#getMenus(com.web.pub.page.paravo.MenusQryParaVO)
	 */
	@Override
	public List<MenuView> getMenus(MenusQryParaVO menuQryParaVO)
			throws PubBusinessException {
		List<MenuVO> resList = webSrcDao.getMenus(menuQryParaVO);
		return convert2RootView(resList);
	}

	/**
	 * 
	 * @param resourceView
	 * @return
	 * @author:sunshine
	 * @date:2014-2-11 下午3:35:39
	 */
	private ResourceVO convertResVO(ResourceView resourceView) {
		ResourceVO resourceVO = null;
		if (resourceView != null) {
			resourceVO = new ResourceVO();
			resourceVO.setModuleId(resourceView.getModuleId());
			resourceVO.setUrl(resourceView.getUrl());
			resourceVO.setResourceCode(resourceView.getUrl());
			resourceVO.setResourceId(resourceView.getResourceId());
			resourceVO.setResourceName(resourceView.getResourceName());
			resourceVO.setSecurityLevel(resourceView.getSecurityLevel());
			resourceVO.setSeq(resourceView.getSeq());
		}
		return resourceVO;
	}

	/**
	 * 
	 * @param menuView
	 * @return
	 * @author:sunshine
	 * @date:2014-2-11 下午3:35:43
	 */
	private MenuVO convertMenuVO(MenuView menuView) {
		MenuVO menuVO = null;
		if (menuView != null) {
			menuVO = new MenuVO();
			menuVO.setHide(menuView.getHide() ? "1" : "0");
			menuVO.setLeaf(menuView.getLeaf() ? "1" : "0");
			menuVO.setMenuId(menuView.getMenuId());
			menuVO.setMenuTitle(menuView.getMenuTitle());
			menuVO.setParentId(menuView.getParentId());
			menuVO.setResourceVO(webSrcDao.qrySourceVOByID(menuView
					.getResourceId()));
			menuVO.setSeq(menuView.getSeq());
		}
		return menuVO;
	}
}
