package com.yonyou.nc.codevalidator.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.persistence.dao.IRuleDefinitionDao;
import com.yonyou.nc.codevalidator.persistence.dao.IRuleDefinitionInitializer;
import com.yonyou.nc.codevalidator.persistence.dao.RuleDaoException;
import com.yonyou.nc.codevalidator.persistence.entity.RuleDefinitionVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class RuleDefinitionInitializerImpl extends AbstractRuleCommonDaoImpl implements IRuleDefinitionInitializer {

	@Override
	public void initialize() throws RuleDaoException {
		Logger.info("开始更新规则定义...");
		List<RuleDefinitionAnnotationVO> allDefinitionVos = RuleDefinitionsReader.getInstance().getAllDefinitionVos();
		List<RuleDefinitionVO> ruleDefinitionVoList = new ArrayList<RuleDefinitionVO>();
		IRuleDefinitionDao ruleDefinitionDao = new RuleDefinitionDaoImpl();
		for (RuleDefinitionAnnotationVO ruleDefinitionAnnotationVO : allDefinitionVos) {
			String relatedIssueId = ruleDefinitionAnnotationVO.getRelatedIssueId();
			RuleDefinitionVO ruleDefinitionVo = ruleDefinitionDao.findRuleDefinitionByIssueId(relatedIssueId);
			if (ruleDefinitionVo == null) {
				ruleDefinitionVo = new RuleDefinitionVO();
				ruleDefinitionVo.setRelatedIssueId(ruleDefinitionAnnotationVO.getRelatedIssueId());
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
			ruleDefinitionVoList.add(ruleDefinitionVo);
		}
		ruleDefinitionDao.batchSave(ruleDefinitionVoList);
		Logger.info("规则定义更新完成...");
	}
	
}
