package com.yonyou.nc.codevalidator.persistence.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.persistence.dao.IRuleDefinitionDao;
import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;
import com.yonyou.nc.codevalidator.persistence.entity.RuleDefinitionVO;

/**
 * 规则定义的数据访问接口实现
 * 
 * @author mazhqa
 * @since V2.7
 */
public class RuleDefinitionDaoImpl extends AbstractRuleCommonDaoImpl implements IRuleDefinitionDao {

	@Override
	public void saveSingle(final RuleDefinitionVO ruleDefinitionVO) throws RuleDaoException {
		executeUpdate(new IRuleDaoExecutor<Void>() {

			@Override
			public Void execute(EntityManager entityManager) throws RuleDaoException {
				EntityManagerUtils.saveEntity(entityManager, ruleDefinitionVO);
				return null;
			}
		});
	}

	@Override
	public RuleDefinitionVO findRuleDefinitionByIssueId(final String relatedIssueId) throws RuleDaoException {
		return executeQuery(new IRuleDaoExecutor<RuleDefinitionVO>() {

			@Override
			public RuleDefinitionVO execute(EntityManager entityManager) throws RuleDaoException {
				@SuppressWarnings("unchecked")
				List<RuleDefinitionVO> resultList = entityManager.createQuery(
						String.format("select r from RuleDefinitionVO r where r.relatedIssueId='%s'", relatedIssueId))
						.getResultList();
				if (resultList.size() == 1) {
					return resultList.get(0);
				} else if (resultList.isEmpty()) {
					return null;
				} else {
					throw new RuleDaoException(String.format("存在重复的issue id: %s ", relatedIssueId));
				}
			}
		});
	}

	@Override
	public RuleDefinitionVO findRuleDefinitionByIdentifier(final String identifier) throws RuleDaoException {
		return executeQuery(new IRuleDaoExecutor<RuleDefinitionVO>() {

			@Override
			public RuleDefinitionVO execute(EntityManager entityManager) throws RuleDaoException {
				@SuppressWarnings("unchecked")
				List<RuleDefinitionVO> resultList = entityManager.createQuery(
						String.format("select r from RuleDefinitionVO r where r.identify='%s'", identifier))
						.getResultList();
				if (resultList.size() == 1) {
					return resultList.get(0);
				} else if (resultList.isEmpty()) {
					return null;
				} else {
					throw new RuleDaoException(String.format("存在重复的标识符: %s ", identifier));
				}
			}
		});
	}

	@Override
	public void batchSave(final List<RuleDefinitionVO> ruleDefinitionVoList) throws RuleDaoException {
		executeUpdate(new IRuleDaoExecutor<Void>() {

			@Override
			public Void execute(EntityManager entityManager) throws RuleDaoException {
				if (!ruleDefinitionVoList.isEmpty()) {
					for (RuleDefinitionVO ruleDefinitionVO : ruleDefinitionVoList) {
						EntityManagerUtils.saveEntity(entityManager, ruleDefinitionVO);
					}
				}
				return null;
			}

		});
	}
}
