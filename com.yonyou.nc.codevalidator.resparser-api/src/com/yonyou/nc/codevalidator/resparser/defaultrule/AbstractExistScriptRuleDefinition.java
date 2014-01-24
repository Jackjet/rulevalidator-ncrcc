package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;

/**
 * �����ר�ýű������壬�����ڲ�ѯָ��sql��������Ϊ�գ���ִ�гɹ����������ǿգ�������о�Ϊ������Ϣ
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractExistScriptRuleDefinition extends AbstractSingleScriptQueryRuleDefinition {

	@Override
	protected final IRuleExecuteResult processScriptRules(ScriptDataSetResource scriptDataSetResource, IRuleExecuteContext ruleExecContext)
			throws ResourceParserException{
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		DataSet dataSet = scriptDataSetResource.getDataSet();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(getScriptExportStrategy());
		return result;
	}
	
	@Override
	protected ScriptResourceQuery getScriptResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException{
		return new ScriptResourceQuery(getRuleDefinitionSql());
	}

	/**
	 * ��ö�Ӧ�Ĳ�ѯsql������sql��ѯ�Ľ������Ϊ������Ϣչʾ
	 * @return
	 */
	protected abstract String getRuleDefinitionSql();
	
	/**
	 * �������ĸ�ʽ������Ĭ��ֱ�Ӵ�ӡ��DataSet��String�ַ���
	 * @return
	 */
	protected IScriptExportStrategy getScriptExportStrategy() {
		return IScriptExportStrategy.DEFAULT_EXPORT_STRATEGY;
	}

}
