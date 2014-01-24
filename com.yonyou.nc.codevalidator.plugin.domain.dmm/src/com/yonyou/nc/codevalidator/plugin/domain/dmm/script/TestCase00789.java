package com.yonyou.nc.codevalidator.plugin.domain.dmm.script;

import java.util.List;


import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, coder = "guojunf", description = "单据模板参照类型的字段，类型设置要勾选“焦点离开后参照显示名称”", relatedIssueId = "789", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00789 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		// 模块
		String ncModule = ruleExecContext.getBusinessComponent().getModule();

		String sql = " select b.reftype reftype,b.itemkey itemkey,h.nodecode nodecode,h.metadataclass mdclass from pub_billtemplet_b b inner join  pub_billtemplet h on h.pk_billtemplet = b.pk_billtemplet where lower(h.modulecode) = '"
				+ ncModule + "' and b.reftype like '%code=%'";

		DataSet executeQuery = SQLQueryExecuteUtils.executeQuery(sql,
				ruleExecContext.getRuntimeContext());

		List<DataRow> rows = executeQuery.getRows();
		for (DataRow dataRow : rows) {
			Object refType = dataRow.getValue("reftype");
			Object itemKey = dataRow.getValue("itemkey");
			Object nodeCode = dataRow.getValue("nodecode");
			Object metadataclass = dataRow.getValue("mdclass");
			if (refType.toString().contains("code=N")) {
				continue;
			}

			result.addResultElement("节点号:" + nodeCode, "元数据:" + metadataclass
					+ " 字段：" + itemKey + "单据模板参照类型的字段，请检查是否要勾选“焦点离开后参照显示名称”");
		}

		return result;
	}

}
