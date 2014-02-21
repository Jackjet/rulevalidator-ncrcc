package com.web.account.dao;

import java.util.List;

import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.page.paravo.UserQryParaVO;
import com.web.rule.persistence.entity.UserVO;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-13 下午2:45:20
 */
public interface IAccountDao {

	/**
	 * 用户登录
	 * 
	 * @param userVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:24:30
	 */
	public UserVO loginUser(UserVO userVO) throws PubBusinessException;

	/**
	 * 查询所有用户信息
	 * 
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:24:34
	 */
	public List<UserVO> qryUser(UserQryParaVO userParaVO)
			throws PubBusinessException;

	/**
	 * 新增用户
	 * 
	 * @param userVO
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:24:38
	 */
	public UserVO insertUser(UserVO userVO) throws PubBusinessException;

	/**
	 * 修改用户
	 * 
	 * @param userVO
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:24:40
	 */
	public void updateUser(UserVO userVO) throws PubBusinessException;

	/**
	 * 删除用户
	 * 
	 * @param pkUser
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-23 下午4:24:44
	 */
	public void delUser(String pkUser) throws PubBusinessException;

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws PubBusinessException
	 * @author:sunshine
	 * @date:2014-1-24 下午5:04:38
	 */
	public Boolean updatePassword(String userId, String oldPassword,
			String newPassword) throws PubBusinessException;

}
