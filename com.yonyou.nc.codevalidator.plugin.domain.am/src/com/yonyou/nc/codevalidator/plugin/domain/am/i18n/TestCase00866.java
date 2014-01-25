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
		description = "单据模板数据库resid与多语资源文件resid匹配校验", solution = "", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY, coder = "zhangnane", relatedIssueId = "866")
public class TestCase00866 extends AbstractLangRuleDefinition {

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String modulename = ruleExecContext.getBusinessComponent().getModule();
		// 模块大写
		DataSet dataset = SQLQueryExecuteUtils.executeMultiLangQuery(getSql(modulename.toUpperCase()),
				ruleExecContext.getRuntimeContext());
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataset);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"单据模板:%s 模板pk:%s 项目编码:%s 默认名称:%s 的resid在多语文件中不存在请检查", "bill_templetcaption", "pk_billtemplet",
				"itemkey", "defaultshowname"));
		return result;
	}

	private String getSql(String modulename) {
		String tablename = SQLQueryExecuteUtils.getCurrentMultiLangTableName();
		StringBuilder checkSql = new StringBuilder();
		checkSql.append("select pub_billtemplet.bill_templetcaption bill_templetcaption ,pub_billtemplet.pk_billtemplet pk_billtemplet,pub_billtemplet_b.itemkey itemkey,pub_billtemplet_b.resid  resid ,pub_billtemplet_b.defaultshowname defaultshowname,"
				+ tablename + ".PATH from pub_billtemplet ");
		checkSql.append("left join pub_billtemplet_b ");
		checkSql.append("on pub_billtemplet.pk_billtemplet = pub_billtemplet_b.pk_billtemplet ");

		checkSql.append("left join " + tablename + " on pub_billtemplet_b.resid=" + tablename + ".id ");
		checkSql.append("where modulecode like '" + modulename + "' and pub_billtemplet_b.resid is not null and "
				+ tablename + ".PATH is null");
		return checkSql.toString();
	}

}
