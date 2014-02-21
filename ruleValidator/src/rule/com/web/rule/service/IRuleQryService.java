package com.web.rule.service;

import java.util.List;

import com.web.pub.fw.exception.PubBaseException;
import com.web.rule.page.paravo.ReportDetailQryPara;
import com.web.rule.page.paravo.StaticQryPara;
import com.web.rule.persistence.entity.ExecuteUnitVO;
import com.web.rule.view.ExecuteRecordView;
import com.web.rule.view.RuleExecuteResultView;
import com.web.rule.view.RusStaticView;

public interface IRuleQryService {

	/**
	 * 查询规则统计信息
	 * 
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2013-12-30 下午1:45:21
	 */
	public List<RusStaticView> qryRuleStaticInfo(ReportDetailQryPara qryPara)
			throws PubBaseException;

	/**
	 * 查询规则校验记录
	 * 
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-2 上午11:07:05
	 */
	public List<ExecuteRecordView> qryExecuteRecord(StaticQryPara qryPara)
			throws PubBaseException;

	/**
	 * 查询规则校验明细
	 * 
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-2 上午11:07:52
	 */
	public List<RuleExecuteResultView> qryExecuteResult(
			ReportDetailQryPara qryPara) throws PubBaseException;

	/**
	 * 删除规则执行记录
	 * 
	 * @param recordID
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-7 下午3:30:07
	 */
	public void delExecRecord(String recordID) throws PubBaseException;

	/**
	 * 根据执行结果ID查询信息
	 * 
	 * @param resultId
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-8 下午4:56:52
	 */
	public RuleExecuteResultView queryResultDetailByID(String resultId)
			throws PubBaseException;

	/**
	 * 查询所有产品
	 * 
	 * @return
	 * @throws PubBaseException
	 * @author:sunshine
	 * @date:2014-1-16 下午3:53:13
	 */
	public List<ExecuteUnitVO> qryAllProd() throws PubBaseException;
}
