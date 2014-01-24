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
 * ���ڹ����DB���棬���ڲ�ȡ����ķ�ʽ����ÿ�α��浥��ִ�е�Ԫ�Ľ���������ÿ��ִ�п�ʼ�ͽ���ʱ���ᷢ��֪ͨ
 * <P>
 * ��������͸���ִ�м�¼
 * @author mazhqa
 * @since V2.9
 */
public class RuleDaoRecordPersistenceImpl implements IRecordExecutePersistence {
	
	private final SessionFactory sessionFactory = RuleSessionFactory.getSessionFactory();

	@Override
	public String insertRecord(RuleExportContext ruleExportContext) throws RuleBaseException {
		Logger.info(String.format("��ʼ��ִ�е�Ԫ: %s ��ִ�м�¼���д�������...", ruleExportContext.getBusinessComponent()
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
			Logger.info(String.format("ִ�е�Ԫ: %s ��ִ�м�¼�����ɹ�!", ruleExportContext.getBusinessComponent()
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
		Logger.info(String.format("��ʼ��ִ�е�Ԫ: %s ��ִ�м�¼���и��²���...", ruleExportContext.getBusinessComponent()
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
			Logger.info(String.format("ִ�е�Ԫ: %s ��ִ�м�¼���³ɹ�!", ruleExportContext.getBusinessComponent()
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
	 * ��ȡִ�е�ԪVO����������ڣ�����Ҫ����һ��
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
			throw new RuleDaoException(String.format("�����ظ���ҵ�����: %s ", businessComponent.getDisplayBusiCompName()));
		}

	}
}
