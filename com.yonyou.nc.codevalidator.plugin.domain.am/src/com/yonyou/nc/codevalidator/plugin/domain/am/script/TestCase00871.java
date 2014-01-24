package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractExistScriptRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * ��ΪҪ������ݿ��е��������ݣ����ԾͲ���ģ���ѯ��ֱ�Ӳ���˵�ע���������������
 * 
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "������ݿ���Ĳ�����������֮�˵�ע��", relatedIssueId = "871",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch", executeLayer = ExecuteLayer.GLOBAL,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00871 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected String getRuleDefinitionSql() {
		return "select  pk_menuitem,menuitemname from sm_menuitemreg " + " where pk_menu not "
				+ "in (select pk_menu from sm_menuregister)";
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("�˵�ע���е���������(sm_menuitemreg)��%s : %s", "pk_menuitem",
				"menuitemname");
	}

}
