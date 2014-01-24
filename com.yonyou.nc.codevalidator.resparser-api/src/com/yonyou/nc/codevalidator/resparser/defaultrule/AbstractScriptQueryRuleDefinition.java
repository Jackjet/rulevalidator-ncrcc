package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * 抽象的脚本查询规则定义
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractScriptQueryRuleDefinition extends AbstractRuleDefinition {
	
	/**
	 * 执行sql，得到对应的dataset
	 * @param ruleExecContext - 执行规则上下文
	 * @param sql 查询字符串
	 * @return
	 * @throws ResourceParserException
	 */
	protected DataSet executeQuery(IRuleExecuteContext ruleExecContext, String sql) throws ResourceParserException{
		ScriptResourceQuery scriptResourceQuery = new ScriptResourceQuery(sql);
		scriptResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
		return ResourceManagerFacade.getResourceAsDataSet(scriptResourceQuery).getDataSet();
	}
}
