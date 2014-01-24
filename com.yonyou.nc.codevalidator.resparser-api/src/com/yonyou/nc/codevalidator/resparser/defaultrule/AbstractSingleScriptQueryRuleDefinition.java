package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 当查询中只有一个sql语句时，从该查询类中继承
 * @author mazhqa
 * @since V2.1
 */
public abstract class AbstractSingleScriptQueryRuleDefinition extends AbstractScriptQueryRuleDefinition{
	
	@Override
	public final IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ScriptResourceQuery scriptResourceQuery = getScriptResourceQuery(ruleExecContext);
		scriptResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
		ScriptDataSetResource resourceAsDataSet = ResourceManagerFacade.getResourceAsDataSet(scriptResourceQuery);
		return processScriptRules(resourceAsDataSet, ruleExecContext);
	}

	/**
	 * 在查询出结果后，处理结果
	 * 
	 * @param scriptDataSetResource
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	protected abstract IRuleExecuteResult processScriptRules(ScriptDataSetResource scriptDataSetResource, IRuleExecuteContext ruleExecContext)
			throws ResourceParserException;

	/**
	 * 得到对应的脚本资源查询query
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	protected abstract ScriptResourceQuery getScriptResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException;


}
