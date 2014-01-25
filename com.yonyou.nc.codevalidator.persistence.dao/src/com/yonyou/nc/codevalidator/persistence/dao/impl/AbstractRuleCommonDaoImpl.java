package com.yonyou.nc.codevalidator.persistence.dao.impl;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;

/**
 * ����Ĺ���ͨ��DAO���壬�����ṩ�˻�ȡEntityManage�ķ������Լ���������ֶ��ͷ�
 * 
 * @author mazhqa
 * @since V2.7
 */
public class AbstractRuleCommonDaoImpl {

	/**
	 * ִ�в�ѯ��ģ�巽�������κ�������ã���ִ����ɺ󣬻��Զ���entityManager�ͷ�
	 * 
	 * @param ruleDaoExecutor
	 *            - �����ִ���߼�
	 * @return ruleDaoExecutor���صĽ��
	 * @throws RuleDaoException
	 *             - �������executorִ�г��ִ���ʱ
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
	 * ִ�о���������߼����Ὣ���ø÷����ĸ�����Ϊһ������������ύ��ع�
	 * 
	 * @param ruleDaoExecutor
	 *            - �����ִ���߼�
	 * @return ruleDaoExecutor���صĽ��
	 * @throws RuleDaoException
	 *             - �������executorִ�г��ִ���ʱ
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
