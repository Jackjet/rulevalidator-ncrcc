package com.yonyou.nc.codevalidator.persistence.dao;

/**
 * �����ڹ���ϵͳ����ʱ��ʼ�����ù���Ľӿ�
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleDefinitionInitializer {
	
	/**
	 * ϵͳ��ʼ���������Ȼ�Ե�ǰ���е����ݽ�������Ȼ�����·������µĹ���
	 * @throws RuleDaoException
	 */
	void initialize() throws RuleDaoException;

}
