package com.yonyou.nc.codevalidator.plugin.domain.ncm.script;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.dbcreate.DbCreateTableField;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractDbcreateRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, description = "对于逻辑型的数据库字段（Y, N）， "
		+ "在数据库中有默认的配置项N ，建议勾选必输项 ，既然是逻辑型 那么不是N就是Y", solution = "修改建库sql文件，使该字段不能为空", relatedIssueId = "348",
		subCatalog = SubCatalogEnum.CS_CONTENTCHECK, coder = "luoxin3", executePeriod = ExecutePeriod.DEPLOY,
		executeLayer = ExecuteLayer.GLOBAL)
public class TestCase00348 extends AbstractDbcreateRuleDefinition implements IGlobalRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		DataSet dataSet = executeDbcreateTableQuery(getExecuteSql(), ruleExecContext);
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(dataSet);
		return result;
	}

	private String getExecuteSql() {
		return String.format("select FIELDTABLENAME as 表名,FIELDNAME as 字段名,FIELDTYPE as 字段类型,"
				+ "FIELDLENGTH as 字段长度,CANBENULL as 是否为空,DBCREATEFILEPATH as 文件路径"
				+ " from  %s where %s = 'char' and %s = 1 and %s = 1 ", SQLQueryExecuteUtils.getDbCreateDetailTableName(),
				DbCreateTableField.FIELD_TYPE_FIELD, DbCreateTableField.FIELD_LENGTH_FIELD,
				DbCreateTableField.CANBENULL_FIELD);
	}
}
