package com.yonyou.nc.codevalidator.plugin.domain.am.i18n;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractLangRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON,
		description = "查询模板数据库resid与多语资源文件resid匹配校验", solution = "", executePeriod = ExecutePeriod.DEPLOY,
		executeLayer = ExecuteLayer.MODULE, coder = "zhangnane", relatedIssueId = "867")
public class TestCase00867 extends AbstractLangRuleDefinition {

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
		String funccode = moduleCode.toUpperCase();
		DataSet dataSet = SQLQueryExecuteUtils.executeMultiLangQuery(getCheckSql(funccode),
				ruleExecContext.getRuntimeContext());
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"查询模板:%s 模板pk:%s 项目编码:%s 默认名称:%s 的resid：%s 在多语文件中不存在请检查", "templetname", "pk_templet", "fieldcode",
				"fieldname", "resid"));
		return result;
	}

	private String getCheckSql(String funccode) {
		String tablename = SQLQueryExecuteUtils.getCurrentMultiLangTableName();
		StringBuilder checkSql = new StringBuilder();
		/** and "+tablename+".PATH is null **/
		checkSql.append("select pub_query_templet.model_name templetname,pub_query_templet.id pk_templet,pub_query_condition.field_code fieldcode,pub_query_condition.resid  resid ,pub_query_condition.field_name fieldname,"
				+ tablename + ".PATH from pub_query_templet ");
		checkSql.append("left join pub_query_condition ");
		checkSql.append("on pub_query_templet.id = pub_query_condition.pk_templet ");

		checkSql.append("left join " + tablename + " on pub_query_condition.resid=" + tablename + ".id ");
		checkSql.append("where model_code in (select funcode from sm_funcregister left join dap_dapsystem on own_module = moduleid where systypecode = '"
				+ funccode + "') and pub_query_condition.resid is not null and " + tablename + ".PATH is null");

		return checkSql.toString();
	}

}
