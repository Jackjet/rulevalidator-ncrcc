package com.web.account.service;

import java.util.List;

import com.web.account.uiview.UserUIView;
import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.page.paravo.UserQryParaVO;

/**
 * 获取资源服务
 * 
 * @author:sunshine
 * @date:2014-1-13 下午2:43:29
 */
public interface IAccountService {

	/**
	 * 用户登录
	 * 
	 * @param userView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:15:08
	 */
	public UserUIView loginUserView(UserUIView userView)
			throws PubBusinessException;

	/**
	 * 查询所有用户信息
	 * 
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-13 下午2:46:42
	 */
	public List<UserUIView> qryUserView(UserQryParaVO userParaVO)
			throws PubBusinessException;

	/**
	 * 新增用户
	 * 
	 * @param userView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:14:25
	 */
	public UserUIView insertUserView(UserUIView userView)
			throws PubBusinessException;

	/**
	 * 修改用户
	 * 
	 * @param userView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:14:22
	 */
	public UserUIView updateUserView(UserUIView userView)
			throws PubBusinessException;

	/**
	 * 删除用户
	 * 
	 * @param userView
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:14:20
	 */
	public UserUIView delUserView(UserUIView userView)
			throws PubBusinessException;

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-24 下午4:58:19
	 */
	public Boolean updatePassword(String userId, String oldPassword,
			String newPassword) throws PubBusinessException;

}
