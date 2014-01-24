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

@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, description = "ҵ����ע���¼�ԴIDУ��", relatedIssueId = "685",
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
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy("�¼�ԴID��%s �¼�Դ���ƣ�%s ������Դ�в�����",
				"sourceid", "sourcename"));
		return result;
	}

}
