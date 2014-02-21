package com.web.pub.codename.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.web.pub.codename.iquery.IQueryCodeName;
import com.web.pub.fw.exception.PubBaseException;
import com.web.rule.codename.query.QueryOrderStatusCodeName;
import com.web.rule.codename.query.QueryProdCodeName;
import com.web.rule.codename.query.QueryRecordCodeName;
import com.web.rule.service.IRuleQryService;

public class CodeNameRegister {
	private static final Log logger = LogFactory.getLog(CodeNameRegister.class);

	private final Map<String, IQueryCodeName> codenameServiceMap = new ConcurrentHashMap<String, IQueryCodeName>();

	public CodeNameRegister(IRuleQryService ruleService) {
		super();
		// 订单状态
		codenameServiceMap.put("CODENAME_RULE_STATUS",
				new QueryOrderStatusCodeName());
		// 规则检查记录
		codenameServiceMap.put("CODENAME_RULE_RECORD", new QueryRecordCodeName(
				ruleService));
		// 产品
		codenameServiceMap.put("CODENAME_RULE_PROD", new QueryProdCodeName(
				ruleService));
	}

	public IQueryCodeName getQueryService(String key) throws PubBaseException {
		if (codenameServiceMap.containsKey(key)) {
			return codenameServiceMap.get(key);
		} else {
			logger.error("没有查询到" + key
					+ "对应的编码名称查询服务,请检查CodeNameRegister类中是否注册！");
			throw new PubBaseException("没有查询到" + key
					+ "对应的编码名称查询服务,请检查CodeNameRegister类中是否注册！");
		}
	}
}
