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

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "���յ�Item��Ҫ��ʾ���ƶ����Ǳ���", relatedIssueId = "477",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangnane", repairLevel = RepairLevel.SUGGESTREPAIR,
		executeLayer = ExecuteLayer.MODULE, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00477 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String modulecode = ruleExecContext.getBusinessComponent().getModule();
		// ��֤moduleСд
		DataSet dataSet = executeQuery(ruleExecContext, getCheckSql(modulecode.toLowerCase()));
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"����ģ��:%s ��ҳǩ:%s ����Ŀ����Ϊ:%s ����Ϊ:%s �Ĳ��ս�����ʾ����", "BILL_TEMPLETCAPTION", "table_name", "itemkey",
				"propertyname"));
		return result;
	}

	private String getCheckSql(String modulecode) {

		StringBuilder checkSql = new StringBuilder();
		//
		checkSql.append("SELECT metadata.namespace,t1.BILL_TEMPLETCAPTION,b1.table_name,b1.itemkey,metadata.propertyname,b1.reftype ");

		checkSql.append("FROM pub_billtemplet_b b1,pub_billtemplet t1,");
		// ͨ��ƴ�ӵķ�ʽ�������Ϊ���յ�Ԫ����·��Ԫ����·��
		checkSql.append("(SELECT co.namespace || '.' || c.name || '.' || p.name metadataproperty,co.namespace,c.name,p.name,p.displayname propertyname,c.displayname,p.refmodelname ");

		checkSql.append("FROM md_ormap m,md_class c,md_property p,md_table t,md_column l,md_component co ");

		checkSql.append("WHERE m.classid = c.id AND co.id = c.componentid AND m.attributeid = p.id AND m.tableid = t.id AND m.columnid = l.id AND p.datatypestyle = 305) metadata ");

		checkSql.append("WHERE b1.metadataproperty = metadata.metadataproperty ");

		checkSql.append("AND t1.pk_billtemplet = b1.pk_billtemplet ");

		checkSql.append("AND (t1.pk_billtemplet LIKE '____Z9%') ");
		// ͨ�������жϽ����뿪���Ƿ���ʾ����
		checkSql.append("AND(b1.pos = 1 AND (b1.reftype IS NULL OR b1.reftype NOT LIKE '%code=N%')) ");

		checkSql.append("AND b1.showflag = 1 AND t1.pk_corp = '@@@@' ");

		checkSql.append("AND NOT EXISTS ");
		// itemkey�д��С�name������Ϊ����Ҫ��ʾ���Ƶ��ֶ�
		checkSql.append("(SELECT 1 FROM pub_billtemplet_b b2 WHERE b2.pk_billtemplet = b1.pk_billtemplet AND b2.itemkey LIKE b1.itemkey || '%' AND b2.itemkey LIKE '%name')");
		// modulecodeСд
		checkSql.append("and metadata.namespace like '"+modulecode+"' ORDER BY t1.nodecode ");

		return checkSql.toString();

	}
}
