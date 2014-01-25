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
		executePeriod = ExecutePeriod.DEPLOY, description = "检查查询模板参照字段引用的类型是否正确", relatedIssueId = "556",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK)
public class TestCase00556 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected String getRuleDefinitionSql() {
		return "SELECT  t.model_code,t.model_name,c.field_name,c.field_code FROM "
				+ " pub_query_templet t INNER JOIN pub_query_condition c ON t.id = c.pk_templet "
				+ " WHERE t.metaclass is null and t.layer = 0  and t.pk_corp = '@@@@' and nvl(c.if_notmdcondition,'N') = 'Y' and data_type = 5 and c.consult_code not like '<%' and not exists ( SELECT  1  FROM  BD_REFINFO info  WHERE c.CONSULT_CODE = info.name ) ";
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("查询模板%s参照引用类型，在参照类型里存在。", "model_name");
	}

}
