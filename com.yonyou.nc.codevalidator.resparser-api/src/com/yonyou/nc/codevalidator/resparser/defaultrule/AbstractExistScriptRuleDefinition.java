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
 * 抽象的专用脚本规则定义，仅用于查询指定sql，如果结果为空，则执行成功；如果结果非空，将结果列举为错误信息
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
	 * 获得对应的查询sql，本条sql查询的结果均作为错误信息展示
	 * @return
	 */
	protected abstract String getRuleDefinitionSql();
	
	/**
	 * 输出结果的格式化处理，默认直接打印出DataSet的String字符串
	 * @return
	 */
	protected IScriptExportStrategy getScriptExportStrategy() {
		return IScriptExportStrategy.DEFAULT_EXPORT_STRATEGY;
	}

}
