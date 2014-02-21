package com.web.rule.page.handler;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.web.pub.fw.exception.PubBaseException;
import com.web.pub.page.handler.IDBQuery;
import com.web.pub.page.handler.IDBQueryHandler;
import com.web.rule.page.paravo.StaticQryPara;
import com.web.rule.service.IRuleQryService;
import com.web.rule.view.ExecuteRecordView;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RuleStaticQryHandler implements IDBQueryHandler {

	private static final Log logger = LogFactory
			.getLog(RuleStaticQryHandler.class);

	private IRuleQryService ruleService;

	@Override
	public List query(IDBQuery query) {
		List<ExecuteRecordView> list = null;
		try {
			list = ruleService.qryExecuteRecord((StaticQryPara) query);
		} catch (PubBaseException e) {
			logger.error(e);
		}
		return list;
	}

	@Override
	public long queryTotalCount(IDBQuery query) {

		long length = 0;
		try {
			List<ExecuteRecordView> chnlSendBillUIViews = ruleService
					.qryExecuteRecord((StaticQryPara) query);
			if (chnlSendBillUIViews != null) {
				length = chnlSendBillUIViews.size();
			}
		} catch (PubBaseException e) {
			logger.error(e);
		}
		return length;

	}

	public IRuleQryService getRuleService() {
		return ruleService;
	}

	public void setRuleService(IRuleQryService ruleService) {
		this.ruleService = ruleService;
	}

}
