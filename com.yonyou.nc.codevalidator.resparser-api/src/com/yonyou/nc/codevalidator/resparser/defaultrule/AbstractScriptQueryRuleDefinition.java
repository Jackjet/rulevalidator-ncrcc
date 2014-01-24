package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * ����Ľű���ѯ������
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractScriptQueryRuleDefinition extends AbstractRuleDefinition {
	
	/**
	 * ִ��sql���õ���Ӧ��dataset
	 * @param ruleExecContext - ִ�й���������
	 * @param sql ��ѯ�ַ���
	 * @return
	 * @throws ResourceParserException
	 */
	protected DataSet executeQuery(IRuleExecuteContext ruleExecContext, String sql) throws ResourceParserException{
		ScriptResourceQuery scriptResourceQuery = new ScriptResourceQuery(sql);
		scriptResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
		return ResourceManagerFacade.getResourceAsDataSet(scriptResourceQuery).getDataSet();
	}
}
