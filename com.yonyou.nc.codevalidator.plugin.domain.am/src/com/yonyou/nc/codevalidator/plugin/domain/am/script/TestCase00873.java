package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractExistScriptRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * 因为要检测数据库中的垃圾数据，所以就不按模块查询，直接检查功能注册--业务活动下的按钮注册是否重复
 * 
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "检查功能注册--业务活动下的按钮注册是否重复", relatedIssueId = "873",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch", executeLayer = ExecuteLayer.GLOBAL,
		executePeriod = ExecutePeriod.DEPLOY, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00873 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("功能注册--业务活动下的按钮注册重复：%s : %s, %s 业务活动: %s %s  数量：%s",
				"fun_name", "funcode", "name", "btncode", "btnname", "num");
	}

	@Override
	protected String getRuleDefinitionSql() {
		return " select funcode, fun_name, code, name, btncode,btnname,num "
				+ " from (select o.funcode,o.fun_name,y.PK_BUSIACTIVE,y.pk_butn,i.code,i.name,p.btncode,p.btnname,count(y.pk_butn) num"
				+ " from sm_busiactive_btn y"
				+ " inner join sm_busiactivereg i on y.pk_busiactive = i.pk_busiactive "
				+ " inner join sm_funcregister o on i.parent_id = o.cfunid inner join sm_butnregister p on y.pk_butn=p.pk_btn"
				+ " Group By O.Funcode, O.Fun_Name, Y.Pk_Busiactive, Y.Pk_Butn,I.Code,I.Name,P.Btncode,P.Btnname)"
				+ "  where num > 1";
	}

}
