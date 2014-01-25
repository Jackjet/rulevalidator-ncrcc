package com.yonyou.nc.codevalidator.persistence.dao;

import java.util.List;

import com.yonyou.nc.codevalidator.persistence.entity.RuleDefinitionVO;

/**
 * 规则定义数据访问对象接口
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleDefinitionDao {

	/**
	 * 新建或更新操作
	 * 
	 * @param ruleDefinitionVO
	 * @throws RuleDaoException
	 */
	void saveSingle(RuleDefinitionVO ruleDefinitionVO) throws RuleDaoException;
	
	/**
	 * 批量新建或更新操作
	 * 
	 * @param ruleDefinitionVO
	 * @throws RuleDaoException
	 */
	void batchSave(List<RuleDefinitionVO> ruleDefinitionVoList) throws RuleDaoException;

	/**
	 * 根据规则的唯一标识符查找规则定义，如果没找到，返回null
	 * 
	 * @param ruleIdentifier
	 *            - 对应规则收集系统的"NCRCC-ID"
	 * @return
	 * @throws RuleDaoException
	 *             - 当数据库中存在重复的规则定义时
	 */
	RuleDefinitionVO findRuleDefinitionByIssueId(String relatedIssueId) throws RuleDaoException;

	/**
	 * 根据规则的唯一标识符查找规则定义，如果没找到，返回null
	 * 
	 * @param identifier
	 *            - 标识符,全路径类名称
	 * @return
	 * @throws RuleDaoException
	 *             - 当数据库中存在重复的规则定义时
	 */
	RuleDefinitionVO findRuleDefinitionByIdentifier(String identifier) throws RuleDaoException;

}
