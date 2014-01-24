package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "表pub_billtemplet中LAYER字段的值都应为0（0意思为水平，非0不处理升级）",
		relatedIssueId = "655", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangguang",
		executeLayer = ExecuteLayer.MODULE, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00655 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		BusinessComponent bussinessComponent = ruleExecContext.getBusinessComponent();
		String module = bussinessComponent.getModule();
		String sql = "SELECT BILL_TEMPLETCAPTION,PK_BILLTYPECODE" + " FROM pub_billtemplet " + " where LAYER<>0 ";
		String sqlECHO = sql + " and MODULECODE = '" + module + "'";
		DataSet ds = executeQuery(ruleExecContext, sqlECHO);
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(ds);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"单据模板中Layer字段不为0的记录有：单据模版编码为：%s 的单据模版！（单据模版名为：%s)", "PK_BILLTYPECODE", "BILL_TEMPLETCAPTION"));
		return result;
	}
}