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
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, description = "业务插件注册事件源ID校验", relatedIssueId = "685",
		subCatalog = SubCatalogEnum.CS_CONTENTCHECK, coder = "zhangnane", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00685 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
		String funcCode = moduleCode.toUpperCase();
		StringBuilder sql = new StringBuilder("SELECT distinct sourceid,sourcename ");
		sql.append("FROM pub_eventtype ");
		sql.append("left join dap_dapsystem ");
		sql.append("on  owner = moduleid ");
		sql.append("where systypecode = '" + funcCode + "' ");
		sql.append("and sourceid not in ");
		sql.append("(SELECT id FROM md_class)");
		DataSet dataset = executeQuery(ruleExecContext, sql.toString());

		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataset);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy("事件源ID：%s 事件源名称：%s 在数据源中不存在",
				"sourceid", "sourcename"));
		return result;
	}

}
