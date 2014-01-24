package com.yonyou.nc.codevalidator.plugin.domain.scm.md;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractExistScriptRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

@RuleDefinition(catalog = CatalogEnum.METADATA, coder = "hanlj3", executeLayer = ExecuteLayer.GLOBAL,
		executePeriod = ExecutePeriod.DEPLOY, description = "����Ԫ���ݵĲ�ѯģ���ֶΣ��Ƿ���ڷ�Ԫ�����ֶ�", relatedIssueId = "554",
		subCatalog = SubCatalogEnum.MD_BASESETTING)
public class TestCase00554 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected String getRuleDefinitionSql() {
		// �����ֶΣ��Ƿ�Ԫ�����ֶΡ�����Ϊ�� ��
		return "SELECT t.model_code,t.model_name,c.field_code,c.field_name,c.if_notmdcondition FROM"
				+ " pub_query_templet t INNER JOIN  pub_query_condition c ON t.id = c.pk_templet "
				+ "WHERE  t.layer = 0  and t.pk_corp = '@@@@' and c.if_notmdcondition is null";
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("��ѯģ�����ƣ�%s��Ӧ�ֶα��룺%s �ġ�Ԫ�����ֶΡ�����Ϊ�գ�", "model_name", "field_code");
	}

}

