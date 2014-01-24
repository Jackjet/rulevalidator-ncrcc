package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "参照的Item需要显示名称而不是编码", relatedIssueId = "477",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangnane", repairLevel = RepairLevel.SUGGESTREPAIR,
		executeLayer = ExecuteLayer.MODULE, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00477 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String modulecode = ruleExecContext.getBusinessComponent().getModule();
		// 保证module小写
		DataSet dataSet = executeQuery(ruleExecContext, getCheckSql(modulecode.toLowerCase()));
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"单据模板:%s 中页签:%s 的项目名称为:%s 编码为:%s 的参照建议显示名称", "BILL_TEMPLETCAPTION", "table_name", "itemkey",
				"propertyname"));
		return result;
	}

	private String getCheckSql(String modulecode) {

		StringBuilder checkSql = new StringBuilder();
		//
		checkSql.append("SELECT metadata.namespace,t1.BILL_TEMPLETCAPTION,b1.table_name,b1.itemkey,metadata.propertyname,b1.reftype ");

		checkSql.append("FROM pub_billtemplet_b b1,pub_billtemplet t1,");
		// 通过拼接的方式查出类型为参照的元数据路径元数据路径
		checkSql.append("(SELECT co.namespace || '.' || c.name || '.' || p.name metadataproperty,co.namespace,c.name,p.name,p.displayname propertyname,c.displayname,p.refmodelname ");

		checkSql.append("FROM md_ormap m,md_class c,md_property p,md_table t,md_column l,md_component co ");

		checkSql.append("WHERE m.classid = c.id AND co.id = c.componentid AND m.attributeid = p.id AND m.tableid = t.id AND m.columnid = l.id AND p.datatypestyle = 305) metadata ");

		checkSql.append("WHERE b1.metadataproperty = metadata.metadataproperty ");

		checkSql.append("AND t1.pk_billtemplet = b1.pk_billtemplet ");

		checkSql.append("AND (t1.pk_billtemplet LIKE '____Z9%') ");
		// 通过参照判断焦点离开后是否显示名称
		checkSql.append("AND(b1.pos = 1 AND (b1.reftype IS NULL OR b1.reftype NOT LIKE '%code=N%')) ");

		checkSql.append("AND b1.showflag = 1 AND t1.pk_corp = '@@@@' ");

		checkSql.append("AND NOT EXISTS ");
		// itemkey中带有“name”被认为是需要显示名称的字段
		checkSql.append("(SELECT 1 FROM pub_billtemplet_b b2 WHERE b2.pk_billtemplet = b1.pk_billtemplet AND b2.itemkey LIKE b1.itemkey || '%' AND b2.itemkey LIKE '%name')");
		// modulecode小写
		checkSql.append("and metadata.namespace like '"+modulecode+"' ORDER BY t1.nodecode ");

		return checkSql.toString();

	}
}
