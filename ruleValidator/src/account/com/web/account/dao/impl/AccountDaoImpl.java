package com.web.account.dao.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.web.account.dao.IAccountDao;
import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.fw.tools.DESCrypt;
import com.web.pub.fw.tools.DateUtils;
import com.web.pub.page.paravo.UserQryParaVO;
import com.web.rule.persistence.entity.UserVO;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-13 下午2:45:15
 */
@Repository
@SuppressWarnings("unchecked")
public class AccountDaoImpl extends HibernateDaoSupport implements IAccountDao {

	@Autowired
	public void setSessionFactory0(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 用户登录
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:26:00
	 * @see com.web.account.dao.IAccountDao#loginUser(com.web.rule.persistence.entity.UserVO)
	 */
	@Override
	public UserVO loginUser(UserVO userVO) throws PubBusinessException {
		List<UserVO> useList = getSession()
				.createQuery(" from UserVO where userCode=? and pwd=?")
				.setParameter(0, userVO.getUserCode())
				.setParameter(1, DESCrypt.encrypt(userVO.getPwd())).list();
		UserVO login = null;
		if (useList != null && useList.size() == 1) {
			login = useList.get(0);
		}
		return login;
	}

	/**
	 * 查询所有用户信息
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:25:57
	 * @see com.web.account.dao.IAccountDao#qryUser()
	 */
	@Override
	public List<UserVO> qryUser(UserQryParaVO userParaVO)
			throws PubBusinessException {
		List<UserVO> ruleVOList = null;
		try {
			StringBuffer sb = new StringBuffer("from UserVO where 1=1 ");
			if (!StringUtils.isEmpty(userParaVO.getPkUser())) {
				sb.append(" and pkUser=?");
			}
			if (!StringUtils.isEmpty(userParaVO.getUserCode())) {
				sb.append(" and userCode like ?");
			}
			if (!StringUtils.isEmpty(userParaVO.getUserName())) {
				sb.append(" and userName like ?");
			}
			sb.append(" order by userCode asc ");
			Query qry = getSession().createQuery(sb.toString());
			int i = 0;
			if (!StringUtils.isEmpty(userParaVO.getPkUser())) {
				qry.setParameter(i++, userParaVO.getPkUser());
			}
			if (!StringUtils.isEmpty(userParaVO.getUserCode())) {
				qry.setParameter(i++, "%" + userParaVO.getUserCode() + "%");
			}
			if (!StringUtils.isEmpty(userParaVO.getUserName())) {
				qry.setParameter(i++, "%" + userParaVO.getUserName() + "%");
			}
			ruleVOList = qry.list();
		} catch (HibernateException e) {
			logger.error(e);
			throw new PubBusinessException(e.getMessage());
		}
		return ruleVOList;
	}

	/**
	 * 新增用户
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:25:53
	 * @see com.web.account.dao.IAccountDao#insertUser(com.web.rule.persistence.entity.UserVO)
	 */
	@Override
	public UserVO insertUser(UserVO userVO) throws PubBusinessException {
		userVO.setPkUser(UUID.randomUUID().toString());
		userVO.setIsLock("N");
		userVO.setPwd(DESCrypt.encrypt(userVO.getUserCode()));
		userVO.setCreateTs(DateUtils.getServerDate(DateUtils.PATTERN_19));
		//
		Transaction tx = null;
		try {
			tx = getSession().beginTransaction();
			getSession().save(userVO);
			tx.commit();
			getSession().flush();
		} catch (Exception e) {
			logger.equals(e);
			throw new PubBusinessException(e.getMessage());
		} finally {
			getSession().close();
		}
		return userVO;
	}

	/**
	 * 修改用户
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:25:51
	 * @see com.web.account.dao.IAccountDao#updateUser(com.web.rule.persistence.entity.UserVO)
	 */
	@Override
	public void updateUser(UserVO userVO) throws PubBusinessException {
		Transaction tx = null;
		try {
			tx = getSession().beginTransaction();
			getSession()
					.createQuery(
							" update UserVO set userCode=?, userName=?,cellPhone=?, email=?, sex=?, memo=? where pkUser=?")
					.setParameter(0, userVO.getUserCode())
					.setParameter(1, userVO.getUserName())
					.setParameter(2, userVO.getCellPhone())
					.setParameter(3, userVO.getEmail())
					.setParameter(4, userVO.getSex())
					.setParameter(5, userVO.getMemo())
					.setParameter(6, userVO.getPkUser()).executeUpdate();
			tx.commit();
			getSession().flush();
		} catch (Exception e) {
			throw new PubBusinessException(e.getMessage());
		} finally {
			getSession().close();
		}
	}

	/**
	 * 删除用户
	 * 
	 * @author:sunshine
	 * @date:2014-1-23 下午4:25:48
	 * @see com.web.account.dao.IAccountDao#delUser(java.lang.String)
	 */
	@Override
	public void delUser(String pkUser) throws PubBusinessException {
		UserVO userVO = new UserVO();
		userVO.setPkUser(pkUser);
		Transaction tx = null;
		try {
			tx = getSession().beginTransaction();
			getSession().delete(userVO);
			tx.commit();
			getSession().flush();
		} catch (Exception e) {
			throw new PubBusinessException(e.getMessage());
		} finally {
			getSession().close();
		}
	}

	/**
	 * 修改密码
	 * 
	 * @author:sunshine
	 * @date:2014-1-24 下午5:05:01
	 * @see com.web.account.dao.IAccountDao#updatePassword(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Boolean updatePassword(String userId, String oldPassword,
			String newPassword) throws PubBusinessException {
		Transaction tx = null;
		try {
			tx = getSession().beginTransaction();
			int rows = getSession()
					.createQuery(
							" update UserVO set pwd=? where pkUser=? and pwd=?")
					.setParameter(0, DESCrypt.encrypt(newPassword))
					.setParameter(1, userId)
					.setParameter(2, DESCrypt.encrypt(oldPassword))
					.executeUpdate();
			if (rows == 1) {
				tx.commit();
				getSession().flush();
			} else {
				throw new PubBusinessException("旧密码错误。");
			}
		} catch (Exception e) {
			throw new PubBusinessException(e.getMessage());
		} finally {
			getSession().close();
		}
		return null;
	}
}
