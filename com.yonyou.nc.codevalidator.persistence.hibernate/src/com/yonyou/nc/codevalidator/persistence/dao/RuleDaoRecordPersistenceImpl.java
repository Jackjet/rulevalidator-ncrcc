package com.yonyou.nc.codevalidator.persistence.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.yonyou.nc.codevalidator.export.api.IRecordExecutePersistence;
import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.persistence.entity.ExecuteRecordVO;
import com.yonyou.nc.codevalidator.persistence.entity.ExecuteUnitVO;
import com.yonyou.nc.codevalidator.persistence.hibernate.RuleSessionFactory;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 对于规则的DB保存，现在采取另外的方式，即每次保存单个执行单元的结果，因此在每次执行开始和结束时都会发出通知
 * <P>
 * 用来插入和更新执行记录
 * @author mazhqa
 * @since V2.9
 */
public class RuleDaoRecordPersistenceImpl implements IRecordExecutePersistence {
	
	private final SessionFactory sessionFactory = RuleSessionFactory.getSessionFactory();

	@Override
	public String insertRecord(RuleExportContext ruleExportContext) throws RuleBaseException {
		Logger.info(String.format("开始对执行单元: %s 的执行记录进行创建操作...", ruleExportContext.getBusinessComponent()
				.getDisplayBusiCompName()));
		Session session = sessionFactory.openSession();
		ExecuteRecordVO executeRecord = new ExecuteRecordVO();
		executeRecord.setRecordId(UUID.randomUUID().toString());
		executeRecord.setExeBeginTime(ruleExportContext.getStartTime());
		executeRecord.setExecuteUnit(getExecuteUnitVOOrphan(ruleExportContext.getBusinessComponent()));
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(executeRecord);
			tx.commit();
			Logger.info(String.format("执行单元: %s 的执行记录创建成功!", ruleExportContext.getBusinessComponent()
					.getDisplayBusiCompName()));
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuleDaoException(e);
		} finally {
			session.close();
		}
		return executeRecord.getRecordId();
	}

	@Override
	public String updateRecord(RuleExportContext ruleExportContext) throws RuleBaseException {
		Logger.info(String.format("开始对执行单元: %s 的执行记录进行更新操作...", ruleExportContext.getBusinessComponent()
				.getDisplayBusiCompName()));
		Session session = sessionFactory.openSession();
		ruleExportContext.getRecordID();

		ExecuteRecordVO executeRecord = new ExecuteRecordVO();
		executeRecord.setRecordId(ruleExportContext.getRecordID());
		executeRecord.setExeEndTime(ruleExportContext.getEndTime());
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.createQuery(" update ExecuteRecordVO set exeEndTime=? where recordId=?")
					.setParameter(0, ruleExportContext.getEndTime()).setParameter(1, ruleExportContext.getRecordID())
					.executeUpdate();
			tx.commit();
			Logger.info(String.format("执行单元: %s 的执行记录更新成功!", ruleExportContext.getBusinessComponent()
					.getDisplayBusiCompName()));
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuleDaoException(e);
		} finally {
			session.close();
		}
		return executeRecord.getRecordId();
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
}
