package com.yonyou.nc.codevalidator.persistence.dao.impl;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;

/**
 * 抽象的规则dao执行器，真正执行的抽象接口
 * @author mazhqa
 * @since V2.7
 * @param <T> - 约定的返回值类型
 */
public interface IRuleDaoExecutor<T> {
	
	/**
	 * 具体的执行方法，可根据传入的实体管理器进行查询，保存等操作
	 * @param entityManager - 获取的实体管理器，不需要对其进行任何清理操作
	 * @return
	 * @throws RuleDaoException - 抛出异常
	 */
	T execute(EntityManager entityManager) throws RuleDaoException;

}
