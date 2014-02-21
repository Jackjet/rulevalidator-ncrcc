package com.web.pub.fw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.fw.exception.PubBaseException;
import com.web.pub.fw.service.IMenuDomain;
import com.web.pub.fw.uiview.GridModel;
import com.web.pub.fw.uiview.MenuView;
import com.web.pub.fw.uiview.ResourceView;
import com.web.pub.page.paravo.MenusQryParaVO;
import com.web.pub.page.paravo.ResourQryParaVO;
import com.web.pub.src.service.IWebSrcService;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-2-11 下午2:50:48
 */
@Controller
@RequestMapping("src/pub")
public class MenuController extends BaseController {

	/**
	 * 日志
	 */
	private static final Log logger = LogFactory.getLog(MenuController.class);
	@Resource
	private IWebSrcService webSrcService;

	/**
	 * 新增
	 * 
	 * @param view
	 * @return
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "addMenus.json", method = RequestMethod.POST)
	@ResponseBody
	public MenuView addMenus(@ModelAttribute MenuView menuView)
			throws PubBaseException {
		try {
			menuView = webSrcService.addMenus(menuView);
		} catch (PubBusinessException e) {
			menuView.setMessage("excep");
			logger.error(e);
			throw e;
		}
		return menuView;
	}

	/**
	 * 删除
	 * 
	 * @param pks
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "delMenus.json", method = RequestMethod.POST)
	@ResponseBody
	public MenuView delMenus(@ModelAttribute MenuView menuView)
			throws PubBaseException {
		return webSrcService.delMenus(menuView);
	}

	/**
	 * 更新
	 * 
	 * @param view
	 * @return
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "updateMenus.json", method = RequestMethod.POST)
	@ResponseBody
	public MenuView updateMenus(@ModelAttribute MenuView menuView)
			throws PubBaseException {
		try {
			menuView = webSrcService.updateMenus(menuView);
		} catch (PubBusinessException e) {
			menuView.setMessage("excep");
			logger.error(e);
			throw e;
		}
		return menuView;
	}

	/**
	 * 查询
	 * 
	 * @param param
	 * @return
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "getMenus.json", method = RequestMethod.POST)
	@ResponseBody
	public GridModel getMenus(@ModelAttribute MenusQryParaVO menuQryParaVO)
			throws PubBaseException {
		List<MenuView> menuList = webSrcService.getMenus(menuQryParaVO);
		int size = 0;
		if (menuList != null) {
			size = menuList.size();
		}
		GridModel model = new GridModel(size, menuList);
		return model;
	}

	/**
	 * 新增
	 * 
	 * @param view
	 * @return
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "addResource.json", method = RequestMethod.POST)
	@ResponseBody
	public ResourceView addResource(@ModelAttribute ResourceView resourceView)
			throws PubBaseException {
		try {
			resourceView = webSrcService.addResource(resourceView);
		} catch (PubBusinessException e) {
			resourceView.setMessage("excep");
			logger.error(e);
			throw e;
		}
		return resourceView;
	}

	/**
	 * 删除
	 * 
	 * @param pks
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "delResource.json", method = RequestMethod.POST)
	@ResponseBody
	public ResourceView delResource(@ModelAttribute ResourceView resourceView)
			throws PubBaseException {
		return webSrcService.delResource(resourceView);
	}

	/**
	 * 更新
	 * 
	 * @param view
	 * @return
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "updateResource.json", method = RequestMethod.POST)
	@ResponseBody
	public ResourceView updateResource(@ModelAttribute ResourceView resourceView)
			throws PubBaseException {
		try {
			resourceView = webSrcService.updateResource(resourceView);
		} catch (PubBusinessException e) {
			resourceView.setMessage("excep");
			logger.error(e);
			throw e;
		}
		return resourceView;
	}

	/**
	 * 查询
	 * 
	 * @param param
	 * @return
	 * @throws WebBaseException
	 */
	@RequestMapping(value = "getResource.json", method = RequestMethod.POST)
	@ResponseBody
	public GridModel getResource(@ModelAttribute ResourQryParaVO resQryParaVO,
			HttpServletRequest request) throws PubBaseException {
		List<ResourceView> menuList = webSrcService.getResource(resQryParaVO);
		int size = 0;
		if (menuList != null) {
			size = menuList.size();
		}
		GridModel model = new GridModel(size, menuList);
		return model;
	}

	/**
	 * 获取孩子节点
	 * 
	 * @param parent
	 *            父节点
	 * @return 孩子节点
	 * @throws WebBaseException
	 */
	@RequestMapping("children.json")
	@ResponseBody
	public List<MenuView> getChildren(
			@RequestParam(required = false, defaultValue = IMenuDomain.ROOT_ID) String parentId,
			@RequestParam(required = false, defaultValue = "false") boolean isCascade)
			throws PubBaseException {

		List<MenuView> meunViewList = new ArrayList<MenuView>();
		meunViewList = webSrcService.qryMenuVO();
		return meunViewList;
	}
}
