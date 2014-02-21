package com.web.rule.page.handler;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.web.pub.fw.exception.PubBaseException;
import com.web.pub.page.handler.IDBQuery;
import com.web.pub.page.handler.IDBQueryHandler;
import com.web.rule.page.paravo.ReportDetailQryPara;
import com.web.rule.service.IRuleQryService;
import com.web.rule.view.RuleExecuteResultView;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RuleReportDetailDBQryHandler implements IDBQueryHandler {

	private static final Log logger = LogFactory
			.getLog(RuleReportDetailDBQryHandler.class);

	private IRuleQryService ruleService;

	@Override
	public List query(IDBQuery query) {
		List<RuleExecuteResultView> list = null;
		try {
			list = ruleService.qryExecuteResult((ReportDetailQryPara) query);
			// if (chnlSendBillUIViews != null) {
			// list = Arrays.asList(chnlSendBillUIViews);
			// }
		} catch (PubBaseException e) {
			logger.error(e);
		}
		return list;
	}

	@Override
	public long queryTotalCount(IDBQuery query) {
		long length = 0;
		try {
			List<RuleExecuteResultView> chnlSendBillUIViews = ruleService
					.qryExecuteResult((ReportDetailQryPara) query);
			if (chnlSendBillUIViews != null) {
				length = chnlSendBillUIViews.size();
			}
		} catch (PubBaseException e) {
			logger.error(e);
		}
		return length;
	}

	public void setRuleService(IRuleQryService ruleService) {
		this.ruleService = ruleService;
	}
}
