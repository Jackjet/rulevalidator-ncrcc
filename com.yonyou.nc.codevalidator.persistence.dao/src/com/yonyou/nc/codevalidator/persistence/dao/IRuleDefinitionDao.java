package com.yonyou.nc.codevalidator.persistence.dao;

import java.util.List;

import com.yonyou.nc.codevalidator.persistence.entity.RuleDefinitionVO;

/**
 * ���������ݷ��ʶ���ӿ�
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleDefinitionDao {

	/**
	 * �½�����²���
	 * 
	 * @param ruleDefinitionVO
	 * @throws RuleDaoException
	 */
	void saveSingle(RuleDefinitionVO ruleDefinitionVO) throws RuleDaoException;
	
	/**
	 * �����½�����²���
	 * 
	 * @param ruleDefinitionVO
	 * @throws RuleDaoException
	 */
	void batchSave(List<RuleDefinitionVO> ruleDefinitionVoList) throws RuleDaoException;

	/**
	 * ���ݹ����Ψһ��ʶ�����ҹ����壬���û�ҵ�������null
	 * 
	 * @param ruleIdentifier
	 *            - ��Ӧ�����ռ�ϵͳ��"NCRCC-ID"
	 * @return
	 * @throws RuleDaoException
	 *             - �����ݿ��д����ظ��Ĺ�����ʱ
	 */
	RuleDefinitionVO findRuleDefinitionByIssueId(String relatedIssueId) throws RuleDaoException;

	/**
	 * ���ݹ����Ψһ��ʶ�����ҹ����壬���û�ҵ�������null
	 * 
	 * @param identifier
	 *            - ��ʶ��,ȫ·��������
	 * @return
	 * @throws RuleDaoException
	 *             - �����ݿ��д����ظ��Ĺ�����ʱ
	 */
	RuleDefinitionVO findRuleDefinitionByIdentifier(String identifier) throws RuleDaoException;

}
