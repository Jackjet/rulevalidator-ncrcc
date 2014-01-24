package com.yonyou.nc.codevalidator.persistence.dao.impl;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;

/**
 * ����Ĺ���daoִ����������ִ�еĳ���ӿ�
 * @author mazhqa
 * @since V2.7
 * @param <T> - Լ���ķ���ֵ����
 */
public interface IRuleDaoExecutor<T> {
	
	/**
	 * �����ִ�з������ɸ��ݴ����ʵ����������в�ѯ������Ȳ���
	 * @param entityManager - ��ȡ��ʵ�������������Ҫ��������κ��������
	 * @return
	 * @throws RuleDaoException - �׳��쳣
	 */
	T execute(EntityManager entityManager) throws RuleDaoException;

}
