package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, description = "����ѯģ���еĲ����Ƿ��ڲ��ձ��д���", relatedIssueId = "584",
		subCatalog = SubCatalogEnum.CS_CONTENTCHECK, coder = "zhangnane", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY, scope = ScopeEnum.AM)
// @PublicRuleDefinitionParam(params = { CommonRuleParams.OIDMARKPARAM })
public class TestCase00584 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		// TODO: ������Ҫ��չ��֧�ֲ�ͬ��OIDMark����
		// String parameter =
		// ruleExecContext.getParameter(CommonRuleParams.OIDMARKPARAM);
		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
		String funcCode = moduleCode.toUpperCase();
		StringBuilder sql = new StringBuilder(
				"select qc.ID,qc.CONSULT_CODE ,qt.MODEL_NAME,qt.MODEL_CODE,qc.field_code ");
		sql.append("from pub_query_condition qc ");
		sql.append("left join pub_query_templet qt on qc.PK_TEMPLET = qt.ID ");
		sql.append("where data_type='5' ");
		sql.append("and node_code in (select funcode from sm_funcregister left join dap_dapsystem on own_module = moduleid where systypecode = '"
				+ funcCode + "') ");
		sql.append("and consult_code not in (select name from bd_refinfo) ");
		sql.append("and PK_TEMPLET like '____Z9%' ");

		DataSet dataset = executeQuery(ruleExecContext, sql.toString());
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataset);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"��ѯģ�����ƣ�%s �ֶ����ƣ�%s ģ����룺%s �еĲ��գ�%s �ڲ��ձ��в�����", "MODEL_NAME", "field_code", "MODEL_CODE", "CONSULT_CODE"));
		return result;
	}
}
