package com.yonyou.nc.codevalidator.persistence.dao;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.yonyou.nc.codevalidator.export.api.IPortionRulePersistence;
import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.persistence.entity.ExecuteRecordVO;
import com.yonyou.nc.codevalidator.persistence.entity.ExecuteUnitVO;
import com.yonyou.nc.codevalidator.persistence.entity.RuleDefinitionVO;
import com.yonyou.nc.codevalidator.persistence.entity.RuleExecuteResultVO;
import com.yonyou.nc.codevalidator.persistence.hibernate.RuleSessionFactory;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 规则部分导出至数据库的实现类
 * 
 * @author zhenglq
 * @since V2.9
 */
public class RulePortionDbExportImpl implements IPortionRulePersistence {

	private final SessionFactory sessionFactory = RuleSessionFactory.getSessionFactory();

	@Override
	public String getIdentifier() {
		return this.getClass().getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String batchExportResult(BusinessComponent businessComponent, List<IRuleExecuteResult> ruleResultList,
			RuleExportContext context) throws RuleBaseException {
		Logger.info(String.format("开始对执行单元: %s 的执行结果(总计:%s)进行保存操作...", businessComponent.getDisplayBusiCompName(),
				ruleResultList.size()));
		Session session = sessionFactory.openSession();
		ExecuteRecordVO executeRecord = null;
		ExecuteUnitVO executeUnit = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			// if (executeUnit == null) {
			executeUnit = getExecuteUnitVOOrphan(businessComponent);
			String recorID = context.getRecordID();
			List<ExecuteRecordVO> recordList = session.createQuery(" from ExecuteRecordVO where recordId=?")
					.setParameter(0, recorID).list();
			executeRecord = recordList.get(0);
			for (IRuleExecuteResult ruleExecuteResult : ruleResultList) {
				// 查询规则定义VO
				String ruleID = ruleExecuteResult.getRuleDefinitionIdentifier();
				List<RuleDefinitionVO> ruleList = session.createQuery(
						" from RuleDefinitionVO where identify='" + ruleID + "'").list();
				RuleDefinitionVO ruleDefinition = ruleList.get(0);
				// 获取执行单元VO
				RuleExecuteResultVO executeResultVo = new RuleExecuteResultVO();
				executeResultVo.setResultId(UUID.randomUUID().toString());
				executeResultVo.setExecuteRecord(executeRecord);
				executeResultVo.setExecuteUnit(executeUnit);
				executeResultVo.setPassFlag(ruleExecuteResult.getRuleExecuteStatus().getMessage());
				executeResultVo.setResult(ruleExecuteResult.getResult());
				executeResultVo.setExecuteLevel(ruleExecuteResult.getRuleExecuteContext().getRuleConfigContext()
						.getRuleExecuteLevel().getDisplayName());
				String resultNote = ruleExecuteResult.getNote();
				if (resultNote.length() > RuleExecuteResultVO.NOTE_MAX_LENGTH) {
					resultNote = resultNote.substring(0, RuleExecuteResultVO.NOTE_MAX_LENGTH);
				}
				executeResultVo.setResultDetail(resultNote);
				executeResultVo.setRuleDefinition(ruleDefinition);
				executeResultVo.setSpecParams(ruleExecuteResult.getRuleExecuteContext().getRuleConfigContext()
						.getSpecialParamString());
				session.save(executeResultVo);
			}
			tx.commit();
			Logger.info(String.format("执行单元: %s 的执行结果(总计:%s)保存完成！", businessComponent.getDisplayBusiCompName(),
					ruleResultList.size()));
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuleDaoException(e);
		} finally {
			session.close();
		}

		return null;
	}

	/**
	 * 获取执行单元VO，如果不存在，则需要插入一条
	 * 
	 * @param businessComponent
	 * @return
	 * @throws RuleDaoException
	 */
	@SuppressWarnings("unchecked")
	private ExecuteUnitVO getExecuteUnitVOOrphan(final BusinessComponent businessComponent) throws RuleDaoException {
		Session session = sessionFactory.openSession();
		Query qry = session.createQuery("from ExecuteUnitVO where prodCode=? and moduleCode=? and compCode=?");
		qry.setParameter(0, businessComponent.getProduct());
		qry.setParameter(1, businessComponent.getModule());
		qry.setParameter(2, businessComponent.getBusinessComp());
		List<ExecuteUnitVO> resultList = qry.list();
		if (resultList.size() == 1) {
			return resultList.get(0);
		} else if (resultList.isEmpty()) {
			ExecuteUnitVO executeUnitVo = new ExecuteUnitVO();
			executeUnitVo.setUnitId(UUID.randomUUID().toString());
			executeUnitVo.setProdCode(businessComponent.getProduct());
			executeUnitVo.setModuleCode(businessComponent.getModule());
			executeUnitVo.setCompCode(businessComponent.getBusinessComp());
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.save(executeUnitVo);
				tx.commit();
			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				throw new RuleDaoException(e);
			} finally {
				session.close();
			}
			return executeUnitVo;
		} else {
			throw new RuleDaoException(String.format("存在重复的业务组件: %s ", businessComponent.getDisplayBusiCompName()));
		}

	}

	@Override
	public void resultFolderInitialize(File resultFolder) throws RuleBaseException {

	}

	@Override
	public String getResultFolderName() {
		return null;
	}

	@Override
	public boolean needExportFolder() {
		return false;
	}

}
