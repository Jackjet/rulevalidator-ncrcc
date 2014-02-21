package com.web.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.web.account.dao.IAccountDao;
import com.web.account.service.IAccountService;
import com.web.account.uiview.UserUIView;
import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.fw.service.PubBaseService;
import com.web.pub.page.paravo.UserQryParaVO;
import com.web.rule.persistence.entity.UserVO;

/**
 * 获取资源服务
 * 
 * @author:sunshine
 * @date:2014-1-13 下午2:43:16
 */
@Service
public class AccountServiceImpl extends PubBaseService implements
		IAccountService {

	@Resource
	private IAccountDao accountDao;

	/**
	 * 用户登录
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:15:36
	 * @see com.web.account.service.IAccountService#loginUserView(com.web.account.uiview.UserUIView)
	 */
	@Override
	public UserUIView loginUserView(UserUIView userView)
			throws PubBusinessException {
		UserVO userVO = convert2UserVO(userView);
		userVO = accountDao.loginUser(userVO);
		return convert2UserView(userVO);
	}

	/**
	 * 查询所有用户信息
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:15:44
	 * @see com.web.account.service.IAccountService#qryUserView()
	 */
	@Override
	public List<UserUIView> qryUserView(UserQryParaVO userParaVO)
			throws PubBusinessException {
		List<UserUIView> userViews = null;
		List<UserVO> userVO = accountDao.qryUser(userParaVO);
		userViews = convert2UserView(userVO);
		return userViews;
	}

	/**
	 * 
	 * @param userVO
	 * @return
	 * @author:sunshine
	 * @date:2014-1-23 下午4:21:26
	 */
	private List<UserUIView> convert2UserView(List<UserVO> userVO) {
		List<UserUIView> viewList = null;
		if (userVO != null) {
			viewList = new ArrayList<UserUIView>();
			for (UserVO userVO2 : userVO) {
				viewList.add(convert2UserView(userVO2));
			}
		}
		return viewList;
	}

	/**
	 * 
	 * @param userVO
	 * @return
	 * @author:sunshine
	 * @date:2014-1-23 下午4:21:23
	 */
	private UserUIView convert2UserView(UserVO userVO) {
		UserUIView userView = null;
		if (userVO != null) {
			userView = new UserUIView();
			userView.setUserCode(userVO.getUserCode());
			userView.setUserName(userVO.getUserName());
			userView.setEmail(userVO.getEmail());
			userView.setMemo(userVO.getMemo());
			userView.setSex(userVO.getSex());
			userView.setCellPhone(userVO.getCellPhone());
			userView.setPkUser(userVO.getPkUser());
		}
		return userView;
	}

	/**
	 * 新增用户
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:15:46
	 * @see com.web.account.service.IAccountService#insertUserView(com.web.account.uiview.UserUIView)
	 */
	@Override
	public UserUIView insertUserView(UserUIView userView)
			throws PubBusinessException {
		UserVO userVO = convert2UserVO(userView);
		userVO = accountDao.insertUser(userVO);
		userView.setPkUser(userVO.getPkUser());
		return userView;
	}

	private UserVO convert2UserVO(UserUIView userView) {
		UserVO userVO = null;
		if (userView != null) {
			userVO = new UserVO();
			userVO.setPkUser(userView.getPkUser());
			userVO.setUserCode(userView.getUserCode());
			userVO.setUserName(userView.getUserName());
			userVO.setPwd(userView.getPwd());
			userVO.setEmail(userView.getEmail());
			userVO.setMemo(userView.getMemo());
			userVO.setSex(userView.getSex());
			userVO.setCellPhone(userView.getCellPhone());
		}
		return userVO;
	}

	/**
	 * 修改用户
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:15:49
	 * @see com.web.account.service.IAccountService#updateUserView(com.web.account.uiview.UserUIView)
	 */
	@Override
	public UserUIView updateUserView(UserUIView userView)
			throws PubBusinessException {
		UserVO userVO = convert2UserVO(userView);
		accountDao.updateUser(userVO);
		return userView;
	}

	/**
	 * 删除用户
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:15:52
	 * @see com.web.account.service.IAccountService#delUserView(com.web.account.uiview.UserUIView)
	 */
	@Override
	public UserUIView delUserView(UserUIView userView)
			throws PubBusinessException {
		accountDao.delUser(userView.getPkUser());
		return userView;
	}

	/**
	 * 修改密码
	 * 
	 * @author:sunshine
	 * @date:2014-1-24 下午4:58:48
	 * @see com.web.account.service.IAccountService#updatePassword(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean updatePassword(String userId, String oldPassword,
			String newPassword) throws PubBusinessException {
		return accountDao.updatePassword(userId, oldPassword, newPassword);
	}
}
