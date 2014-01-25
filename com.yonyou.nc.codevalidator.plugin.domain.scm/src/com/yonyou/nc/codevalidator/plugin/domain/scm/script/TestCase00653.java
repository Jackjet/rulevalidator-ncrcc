package com.yonyou.nc.codevalidator.plugin.domain.scm.script;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractExistScriptRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, coder = "hanlj3", executeLayer = ExecuteLayer.GLOBAL,
		executePeriod = ExecutePeriod.DEPLOY, description = "功能节点默认模板设置", relatedIssueId = "653",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK)
public class TestCase00653 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected String getRuleDefinitionSql() {
		return "select (select note from sm_product_version where productcode=a.moduleid),a.* from"
				+ " pub_systemplate_base a " + "where (layer is null or layer<>0)";
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("表pub_systemplate_base中-LAYER字段的值不为0");
	}

}
