package com.yonyou.nc.codevalidator.plugin.domain.scm.script;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractExistScriptRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.CheckRoleEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * V61到V63VO对照升级检查VO升级规则存在非强制分单条件未设置升级类检查脚本
 * 
 * @author hanlj3
 * @reporter fengjb1
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "V61到V63VO对照升级检查VO升级规则存在非强制分单条件未设置升级类检查脚本", memo = "",
		solution = "", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, executeLayer = ExecuteLayer.GLOBAL,
		executePeriod = ExecutePeriod.DEPLOY, specialParamDefine = { "" }, coder = "fengjb1",
		checkRole = CheckRoleEnum.MUSTEXIST, relatedIssueId = "375")
public class TestCase00375 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	public static final String SQL = "SELECT \r\n" + "    vochg.pk_vochange, \r\n" + "    vochg.src_billtype, \r\n"
			+ "    srcbt.BILLTYPENAME, \r\n" + "    vochg.dest_billtype, \r\n" + "    destbt.BILLTYPENAME, \r\n"
			+ "    vochg.upgradeexclass \r\n" + "FROM \r\n" + "    pub_vochange vochg \r\n" + "INNER JOIN \r\n"
			+ "    bd_billtype srcbt \r\n" + "    ON \r\n" + "    vochg.SRC_BILLTYPE = srcbt.PK_BILLTYPEID \r\n"
			+ "INNER JOIN \r\n" + "    bd_billtype destbt \r\n" + "    ON \r\n"
			+ "    vochg.DEST_BILLTYPE = destbt.PK_BILLTYPEID \r\n" + "WHERE \r\n"
			+ "    vochg.upgradeexclass is null \r\n" + "    and exists \r\n" + "    ( \r\n" + "    SELECT \r\n"
			+ "        1 \r\n" + "    FROM \r\n" + "        pub_vochange_s vochg_s \r\n" + "    INNER JOIN \r\n"
			+ "        PUB_VOSPLITITEM sp \r\n" + "        ON \r\n"
			+ "        vochg_s.PK_VOSPLITITEM = sp.PK_VOSPLITITEM \r\n" + "    WHERE \r\n"
			+ "        vochg_s.pk_vochange like '____Z8%' \r\n" + "        and sp.ISMUST           = 'N' \r\n"
			+ "        and vochg_s.pk_vochange = vochg.pk_vochange \r\n"
			+ "        and vochg.pk_group      = 'global00000000000000' \r\n" + "    ) \r\n"
			+ "    and vochg.pk_vochange like '____Z8%' \r\n" + "    and vochg.pk_group = 'global00000000000000'";

	@Override
	protected String getRuleDefinitionSql() {
		return SQL;
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("%s升级规则存在非强制分单条件未设置升级类\n", "pk_vochange");
	}

}
