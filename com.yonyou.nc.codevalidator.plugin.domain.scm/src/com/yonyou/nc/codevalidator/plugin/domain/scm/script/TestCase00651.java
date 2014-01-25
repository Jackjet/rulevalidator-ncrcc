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

@RuleDefinition(catalog = CatalogEnum.LANG, coder = "hanlj3", executeLayer = ExecuteLayer.GLOBAL,
		executePeriod = ExecutePeriod.DEPLOY, description = "单据模板多语规则1", relatedIssueId = "651",
		subCatalog = SubCatalogEnum.LANG_BILLTEMP)
public class TestCase00651 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected String getRuleDefinitionSql() {
		return "select h.modulecode, h.nodecode, h.bill_templetcaption,b.DEFAULTSHOWNAME,b.metadataproperty,b.metadatapath,b.resid,b.RESID_TABNAME from "
				+ " pub_billtemplet_b b,pub_billtemplet h where "
				+ "b.pk_billtemplet = h.pk_billtemplet and b.defaultshowname is not null and b.resid is null  and h.layer=0";
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy(
				"%s 的节点 :%s 的resid为空且Defaultshowname有值，将始终显示Defaultshowname！", "modulecode", "nodecode");
	}

}
