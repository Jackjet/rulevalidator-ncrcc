package com.yonyou.nc.codevalidator.persistence.hibernate;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;
import com.yonyou.nc.codevalidator.persistence.entity.RuleDefinitionVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class RuleDefinitionInitializerImpl {

	private final SessionFactory sessionFactory = RuleSessionFactory.getSessionFactory();

	public void initialize() throws RuleDaoException {
		Session session = sessionFactory.openSession();
		Logger.info("开始更新规则定义...");
		List<RuleDefinitionAnnotationVO> allDefinitionVos = RuleDefinitionsReader.getInstance().getAllDefinitionVos();
		session.beginTransaction();
		try {
			for (RuleDefinitionAnnotationVO ruleDefinitionAnnotationVO : allDefinitionVos) {
				RuleDefinitionVO ruleDefinitionVo = null;
				@SuppressWarnings("rawtypes")
				List resultList = session.createQuery("from RuleDefinitionVO where relatedIssueId=?")
						.setParameter(0, ruleDefinitionAnnotationVO.getRelatedIssueId()).list();
				if (resultList.isEmpty()) {
					ruleDefinitionVo = new RuleDefinitionVO();
					ruleDefinitionVo.setRuleId(UUID.randomUUID().toString());
					ruleDefinitionVo.setRelatedIssueId(ruleDefinitionAnnotationVO.getRelatedIssueId());
				} else if (resultList.size() == 1) {
					ruleDefinitionVo = (RuleDefinitionVO) resultList.get(0);
				} else {
					throw new RuleDaoException(String.format("存在多个相同的关联issue: %s 的规则定义在数据库中!",
							ruleDefinitionAnnotationVO.getRelatedIssueId()));
				}
				ruleDefinitionVo.setIdentify(ruleDefinitionAnnotationVO.getRuleDefinitionIdentifier());
				ruleDefinitionVo.setDescDetails(ruleDefinitionAnnotationVO.getDescription());
				ruleDefinitionVo.setDeveloper(ruleDefinitionAnnotationVO.getCoder());
				ruleDefinitionVo.setExecuteLayer(ruleDefinitionAnnotationVO.getExecuteLayer().getName());
				ruleDefinitionVo.setExecutePeriod(ruleDefinitionAnnotationVO.getExecutePeriod().getName());
				ruleDefinitionVo.setLevel(ruleDefinitionAnnotationVO.getRepairLevel().getName());
				ruleDefinitionVo.setMemo(ruleDefinitionAnnotationVO.getMemo());
				ruleDefinitionVo.setRuleScope(ruleDefinitionAnnotationVO.getScope().getName());
				ruleDefinitionVo.setRuleType(ruleDefinitionAnnotationVO.getCatalog().getName());
				ruleDefinitionVo.setRuleSubType(ruleDefinitionAnnotationVO.getSubCatalog().getName());
				ruleDefinitionVo.setSolution(ruleDefinitionAnnotationVO.getSolution());
				ruleDefinitionVo.setSpecialParams(ruleDefinitionAnnotationVO.getSpecialParamStr());
				session.save(ruleDefinitionVo);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			throw new RuleDaoException(e);
		} finally {
			session.close();
		}
		// ruleDefinitionDao.batchSave(ruleDefinitionVoList);
		Logger.info("规则定义更新完成...");
	}
}
