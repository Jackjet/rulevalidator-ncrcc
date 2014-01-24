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
 * 预制的单据模板字段参照和元数据上参照不一致的检查脚本
 * 
 * @author hanlj3
 * @reporter fengjb1
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "预制的单据模板字段参照和元数据上参照不一致的检查脚本", memo = "",
		solution = "", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, executePeriod = ExecutePeriod.DEPLOY,
		executeLayer = ExecuteLayer.GLOBAL, specialParamDefine = { "" }, coder = "hanlj3",
		checkRole = CheckRoleEnum.MUSTEXIST, relatedIssueId = "374")
public class TestCase00374 extends AbstractExistScriptRuleDefinition implements IGlobalRuleDefinition {

	public static final String SQL = "SELECT\r\n" + "        h.bill_templetcaption,metadata.refmodelname,\r\n"
			+ "        pub_bill.*\r\n" + "   FROM\r\n" + "        (\r\n" + "         SELECT\r\n"
			+ "                    b.metadataproperty,\r\n" + "                    b.metadatapath    ,\r\n"
			+ "                    b.itemkey         ,\r\n" + "                    b.pos             ,\r\n"
			+ "                    b.reftype         ,\r\n" + "                    b.pk_billtemplet  ,\r\n"
			+ "                    b.pk_billtemplet_b,\r\n" + "                    b.metadatarelation\r\n"
			+ "               FROM\r\n" + "                    pub_billtemplet_b b\r\n" + "            ) pub_bill,\r\n"
			+ "        (\r\n" + "         SELECT\r\n"
			+ "                    co.namespace ||'.'|| c.name ||'.'||p.name metadataproperty,\r\n"
			+ "                    co.namespace                                              ,\r\n"
			+ "                    c.name                                                    ,\r\n"
			+ "                    p.name                                                    ,\r\n"
			+ "                    c.displayname                                             ,\r\n"
			+ "                    p.refmodelname\r\n" + "               FROM\r\n"
			+ "                    md_ormap m   ,\r\n" + "                    md_class c   ,\r\n"
			+ "                    md_property p,\r\n" + "                    md_table t   ,\r\n"
			+ "                    md_column l  ,\r\n" + "                    md_component co\r\n"
			+ "              WHERE\r\n" + "                    m.classid = c.id\r\n"
			+ "                AND co.id=c.COMPONENTID\r\n" + "                AND m.attributeid = p.id\r\n"
			+ "                AND m.tableid = t.id\r\n" + "                AND m.columnid = l.id\r\n"
			+ "                AND p.datatypestyle=305 ) metadata,\r\n" + "        pub_billtemplet h\r\n"
			+ "  WHERE\r\n" + "        pub_bill.metadataproperty=metadata.metadataproperty\r\n"
			+ "    AND pub_bill.pk_billtemplet=h.pk_billtemplet and pub_bill.reftype not "
			+ "like metadata.refmodelname ||'%'\r\n" + "    AND h.pk_billtemplet LIKE '____Z8%'\r\n" + " ORDER BY\r\n"
			+ "        h.bill_templetcaption\r\n";

	@Override
	protected String getRuleDefinitionSql() {
		return SQL;
	}

	@Override
	protected IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("字段参照:%s和元数据参照:%s不一致\n", "bill_templetcaption",
				"refmodelname");
	}

}
