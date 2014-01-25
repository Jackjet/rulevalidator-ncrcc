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
		executePeriod = ExecutePeriod.DEPLOY, description = "不同基准库比较同一个按钮业务活动里注册的相同的按钮记录主键是否相同",
		relatedIssueId = "553", subCatalog = SubCatalogEnum.PS_CONTENTCHECK)
public class TestCase00553 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected String getRuleDefinitionSql() {
		return " SELECT  fnc.cfunid, fnc.fun_name, btn.parent_id, btn.btncode FROM "
				+ " sm_butnregister btn INNER JOIN sm_funcregister fnc ON btn.parent_id = fnc.cfunid "
				+ " GROUP BY fnc.cfunid, fnc.fun_name, btn.parent_id, btn.btncode HAVING count(1) > 1 ";
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("%s的同一个按钮业务活动里注册的相同的按钮记录主键不相同，会造成升级后按钮业务活动里存在重复按钮.",
				"fun_name");
	}

}
