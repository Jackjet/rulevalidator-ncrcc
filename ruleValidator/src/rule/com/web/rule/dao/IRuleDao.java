package com.web.rule.dao;

import java.util.List;

import com.web.pub.fw.exception.PubBaseException;
import com.web.rule.page.paravo.ReportDetailQryPara;
import com.web.rule.persistence.entity.ExecuteRecordVO;
import com.web.rule.persistence.entity.ExecuteUnitVO;
import com.web.rule.persistence.entity.RuleExecuteResultVO;
import com.web.rule.view.RusStaticView;

public interface IRuleDao {

	public List<RusStaticView> queryRuleStatic(ReportDetailQryPara qryPara)
			throws PubBaseException;

	/**
	 * 查询规则校验记录
	 * 
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-2 上午11:07:05
	 */
	public List<ExecuteRecordVO> qryExecuteRecord() throws PubBaseException;

	/**
	 * 查询规则校验明细
	 * 
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-2 上午11:06:57
	 */
	public List<RuleExecuteResultVO> qryExecuteResult(
			ReportDetailQryPara qryPara) throws PubBaseException;

	/**
	 * 删除规则执行记录
	 * 
	 * @param recordID
	 * @author:sunshine
	 * @date:2014-1-7 下午3:31:19
	 */
	public void delExecRecord(String recordID) throws PubBaseException;

	/**
	 * 根据执行结果ID查询信息
	 * 
	 * @param resultId
	 * @return
	 * @author:sunshine
	 * @date:2014-1-8 下午4:58:26
	 */
	public RuleExecuteResultVO queryResultDetailByID(String resultId)
			throws PubBaseException;

	/**
	 * 查询所有产品
	 * 
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-16 下午3:55:53
	 */
	public List<ExecuteUnitVO> qryAllProd() throws PubBaseException;
}
