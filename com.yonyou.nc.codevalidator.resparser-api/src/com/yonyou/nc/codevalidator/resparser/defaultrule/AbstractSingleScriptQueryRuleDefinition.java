package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ѯ��ֻ��һ��sql���ʱ���Ӹò�ѯ���м̳�
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
	 * �ڲ�ѯ������󣬴�����
	 * 
	 * @param scriptDataSetResource
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	protected abstract IRuleExecuteResult processScriptRules(ScriptDataSetResource scriptDataSetResource, IRuleExecuteContext ruleExecContext)
			throws ResourceParserException;

	/**
	 * �õ���Ӧ�Ľű���Դ��ѯquery
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	protected abstract ScriptResourceQuery getScriptResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException;


}
