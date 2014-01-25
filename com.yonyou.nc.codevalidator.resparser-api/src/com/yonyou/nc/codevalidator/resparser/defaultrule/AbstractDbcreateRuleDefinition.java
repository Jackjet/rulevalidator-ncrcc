package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.dbcreate.DbCreateTable;
import com.yonyou.nc.codevalidator.resparser.dbcreate.DbCreateTableField;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.CreatorConstants;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * 抽象的建库脚本规则定义，执行前需要依赖dbcreate临时表的初始化
 * <P>
 * 关于建库脚本临时表的名称和字段，可以参考DbCreateTable和DbCreateTableField类中的信息
 * @author mazhqa
 * @since V2.3
 * @see DbCreateTable
 * @see DbCreateTableField
 */
public abstract class AbstractDbcreateRuleDefinition extends AbstractRuleDefinition {
	
	@Override
	public String[] getDependentCreator() {
		return new String[] {CreatorConstants.DBCREATE};
	}
	
	protected DataSet executeDbcreateTableQuery(String sqlString, IRuleExecuteContext ruleExecuteContext) throws RuleBaseException {
		return SQLQueryExecuteUtils.executeTempTableQuery(sqlString, ruleExecuteContext.getRuntimeContext());
	}
	
}
