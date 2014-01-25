package com.yonyou.nc.codevalidator.persistence.dao.impl;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;

/**
 * 抽象的规则通用DAO定义，其中提供了获取EntityManage的方法，以及对其进行手动释放
 * 
 * @author mazhqa
 * @since V2.7
 */
public class AbstractRuleCommonDaoImpl {

	/**
	 * 执行查询的模板方法，无任何事务调用，在执行完成后，会自动将entityManager释放
	 * 
	 * @param ruleDaoExecutor
	 *            - 具体的执行逻辑
	 * @return ruleDaoExecutor返回的结果
	 * @throws RuleDaoException
	 *             - 当具体的executor执行出现错误时
	 */
	protected <T> T executeQuery(IRuleDaoExecutor<T> ruleDaoExecutor) throws RuleDaoException {
		EntityManager entityManager = null;
		try {
			entityManager = RuleOpenJPAEntityManager.getInstance().createEntityManager();
			return ruleDaoExecutor.execute(entityManager);
		} catch (Throwable e) {
			throw new RuleDaoException(e);
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}

	/**
	 * 执行具体操作的逻辑，会将调用该方法的更新作为一整个事务进行提交或回滚
	 * 
	 * @param ruleDaoExecutor
	 *            - 具体的执行逻辑
	 * @return ruleDaoExecutor返回的结果
	 * @throws RuleDaoException
	 *             - 当具体的executor执行出现错误时
	 */
	protected <T> T executeUpdate(IRuleDaoExecutor<T> ruleDaoExecutor) throws RuleDaoException {
		EntityManager entityManager = null;
		try {
			entityManager = RuleOpenJPAEntityManager.getInstance().createEntityManager();
			entityManager.getTransaction().begin();
			T result = ruleDaoExecutor.execute(entityManager);
			entityManager.getTransaction().commit();
			return result;
		} catch (Throwable e) {
			throw new RuleDaoException(e);
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				if (entityManager.getTransaction().isActive()) {
					entityManager.getTransaction().rollback();
				}
				entityManager.close();
			}
		}
	}

}
