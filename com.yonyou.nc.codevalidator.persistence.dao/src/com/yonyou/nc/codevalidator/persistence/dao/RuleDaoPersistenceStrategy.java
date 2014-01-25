package com.yonyou.nc.codevalidator.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.yonyou.nc.codevalidator.export.api.ITotalRuleExportStrategy;
import com.yonyou.nc.codevalidator.persistence.dao.impl.AbstractRuleCommonDaoImpl;
import com.yonyou.nc.codevalidator.persistence.dao.impl.EntityManagerUtils;
import com.yonyou.nc.codevalidator.persistence.dao.impl.IRuleDaoExecutor;
import com.yonyou.nc.codevalidator.persistence.entity.ExecuteRecordVO;
import com.yonyou.nc.codevalidator.persistence.entity.ExecuteUnitVO;
import com.yonyou.nc.codevalidator.persistence.entity.RuleDefinitionVO;
import com.yonyou.nc.codevalidator.persistence.entity.RuleExecuteResultVO;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 规则执行结果，存放在对应的数据源中
 * 
 * @author mazhqa
 * @since V2.7
 */
public class RuleDaoPersistenceStrategy extends AbstractRuleCommonDaoImpl implements ITotalRuleExportStrategy {

	@Override
	public String getIdentifier() {
		return this.getClass().getName();
	}

	@Override
	public void totalResultExport(final SessionRuleExecuteResult sessionRuleExecuteResult) throws RuleBaseException {
		final String recordId = executeUpdate(new IRuleDaoExecutor<String>() {

			@Override
			public String execute(EntityManager entityManager) throws RuleDaoException {
				Logger.info("开始进行数据库结果保存操作!!!");
				ExecuteRecordVO executeRecordVO = new ExecuteRecordVO();
				String recordId = String.format("%s-%s", System.currentTimeMillis(), UUID.randomUUID().toString());
				executeRecordVO.setRecordId(recordId);
				executeRecordVO.setExeBeginTime(sessionRuleExecuteResult.getStartTime());
				executeRecordVO.setExeEndTime(sessionRuleExecuteResult.getEndTime());
				BusinessComponent topExecuteUnit = sessionRuleExecuteResult.getBusinessComponent();
				executeRecordVO.setExecuteUnit(getExecuteUnitVOOrphan(entityManager, topExecuteUnit));
				entityManager.persist(executeRecordVO);
				Logger.info("执行记录ExecuteRecord保存完成!");
				return recordId;
			}

		});
		Map<ExecuteUnitVO, List<IRuleExecuteResult>> unitToResultMap = new HashMap<ExecuteUnitVO, List<IRuleExecuteResult>>();
		for (IRuleExecuteResult ruleExecuteResult : sessionRuleExecuteResult.getRuleExecuteResults()) {
			ExecuteUnitVO executeUnitVO = getExecuteUnitVO(ruleExecuteResult.getBusinessComponent());
			if (!unitToResultMap.containsKey(executeUnitVO)) {
				unitToResultMap.put(executeUnitVO, new ArrayList<IRuleExecuteResult>());
			}
			unitToResultMap.get(executeUnitVO).add(ruleExecuteResult);
		}

		for (Map.Entry<ExecuteUnitVO, List<IRuleExecuteResult>> unitToResultEntry : unitToResultMap.entrySet()) {
			final ExecuteUnitVO executeUnitVO = unitToResultEntry.getKey();
			final List<IRuleExecuteResult> executeResultList = unitToResultEntry.getValue();
			executeUpdate(new IRuleDaoExecutor<Void>() {

				@Override
				public Void execute(EntityManager entityManager) throws RuleDaoException {
					ExecuteRecordVO executeRecordVO = entityManager.find(ExecuteRecordVO.class, recordId);
					for (IRuleExecuteResult ruleExecuteResult : executeResultList) {
						RuleExecuteResultVO executeResultVo = new RuleExecuteResultVO();
						executeResultVo.setExecuteRecord(executeRecordVO);
						executeResultVo.setExecuteUnit(getExecuteUnitVOOrphan(entityManager,
								ruleExecuteResult.getBusinessComponent()));
						executeResultVo.setPassFlag(ruleExecuteResult.getRuleExecuteStatus().getMessage());
						executeResultVo.setResult(ruleExecuteResult.getResult());
						executeResultVo.setExecuteLevel(ruleExecuteResult.getRuleExecuteContext()
								.getRuleConfigContext().getRuleExecuteLevel().getDisplayName());
						executeResultVo.setResultDetail(ruleExecuteResult.getNote());
						executeResultVo.setRuleDefinition(getRuleDefinitionVOOrphan(entityManager,
								ruleExecuteResult.getRuleDefinitionIdentifier()));
						executeResultVo.setSpecParams(ruleExecuteResult.getRuleExecuteContext().getRuleConfigContext()
								.getSpecialParamString());
						EntityManagerUtils.saveEntity(entityManager, executeResultVo);
					}
					return null;
				}
			});
			Logger.info(String.format("执行单元:%s，所有记录(总数:%s)存储成功!",
					executeUnitVO.getModuleCode() + executeUnitVO.getCompCode(), executeResultList.size()));
		}
		Logger.info("结果保存操作结束!!!");
	}

	private ExecuteUnitVO getExecuteUnitVO(final BusinessComponent businessComponent) throws RuleDaoException {
		return executeUpdate(new IRuleDaoExecutor<ExecuteUnitVO>() {

			@Override
			public ExecuteUnitVO execute(EntityManager entityManager) throws RuleDaoException {
				@SuppressWarnings("unchecked")
				List<ExecuteUnitVO> resultList = entityManager
						.createQuery(
								String.format(
										"select e from ExecuteUnitVO e where e.prodCode='%s' and e.moduleCode='%s' and e.compCode='%s'",
										businessComponent.getProduct(), businessComponent.getModule(),
										businessComponent.getBusinessComp())).getResultList();
				if (resultList.size() == 1) {
					return resultList.get(0);
				} else if (resultList.isEmpty()) {
					ExecuteUnitVO executeUnitVo = new ExecuteUnitVO();
					executeUnitVo.setProdCode(businessComponent.getProduct());
					executeUnitVo.setModuleCode(businessComponent.getModule());
					executeUnitVo.setCompCode(businessComponent.getBusinessComp());
					entityManager.persist(executeUnitVo);
					return executeUnitVo;
				} else {
					throw new RuleDaoException(String.format("存在重复的业务组件: %s ",
							businessComponent.getDisplayBusiCompName()));
				}
			}

		});

	}

	// private RuleDefinitionVO getRuleDefinitionVO(final String identifier)
	// throws RuleDaoException {
	// return executeUpdate(new IRuleDaoExecutor<RuleDefinitionVO>() {
	//
	// @Override
	// public RuleDefinitionVO execute(EntityManager entityManager) throws
	// RuleDaoException {
	// @SuppressWarnings("unchecked")
	// List<RuleDefinitionVO> resultList = entityManager.createQuery(
	// String.format("select r from RuleDefinitionVO r where r.identify='%s'",
	// identifier))
	// .getResultList();
	// if (resultList.size() == 1) {
	// return resultList.get(0);
	// } else if (resultList.isEmpty()) {
	// return null;
	// } else {
	// throw new RuleDaoException(String.format("存在重复的标识符: %s ", identifier));
	// }
	// }
	// });
	//
	// }

	private ExecuteUnitVO getExecuteUnitVOOrphan(EntityManager entityManager, final BusinessComponent businessComponent)
			throws RuleDaoException {
		@SuppressWarnings("unchecked")
		List<ExecuteUnitVO> resultList = entityManager
				.createQuery(
						String.format(
								"select e from ExecuteUnitVO e where e.prodCode='%s' and e.moduleCode='%s' and e.compCode='%s'",
								businessComponent.getProduct(), businessComponent.getModule(), businessComponent.getBusinessComp()))
				.getResultList();
		if (resultList.size() == 1) {
			return resultList.get(0);
		} else if (resultList.isEmpty()) {
			ExecuteUnitVO executeUnitVo = new ExecuteUnitVO();
			executeUnitVo.setProdCode(businessComponent.getProduct());
			executeUnitVo.setModuleCode(businessComponent.getModule());
			executeUnitVo.setCompCode(businessComponent.getBusinessComp());
			EntityManagerUtils.saveEntity(entityManager, executeUnitVo);
			return executeUnitVo;
		} else {
			throw new RuleDaoException(String.format("存在重复的业务组件: %s ", businessComponent.getDisplayBusiCompName()));
		}

	}

	private RuleDefinitionVO getRuleDefinitionVOOrphan(EntityManager entityManager, final String identifier)
			throws RuleDaoException {
		@SuppressWarnings("unchecked")
		List<RuleDefinitionVO> resultList = entityManager.createQuery(
				String.format("select r from RuleDefinitionVO r where r.identify='%s'", identifier)).getResultList();
		if (resultList.size() == 1) {
			return resultList.get(0);
		} else if (resultList.isEmpty()) {
			return null;
		} else {
			throw new RuleDaoException(String.format("存在重复的标识符: %s ", identifier));
		}
	}

}
