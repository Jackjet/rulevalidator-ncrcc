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
		executePeriod = ExecutePeriod.DEPLOY, description = "��ͬ��׼��Ƚ�ͬһ����ťҵ����ע�����ͬ�İ�ť��¼�����Ƿ���ͬ",
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
		return new DefaultFormatStringScriptExportStrategy("%s��ͬһ����ťҵ����ע�����ͬ�İ�ť��¼��������ͬ�������������ťҵ��������ظ���ť.",
				"fun_name");
	}

}
