package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.dbinit.DbInitTableFieldValue;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.CreatorConstants;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * 抽象的预置脚本脚本规则定义，执行前需要依赖dbinit临时表的初始化
 * <P>
 * 关于预置脚本临时表的名称和字段，可以参考DbInitTableFieldValue
 * 
 * @author mazhqa
 * @since V2.7
 * @see DbInitTableFieldValue
 */
public abstract class AbstractDbInitRuleDefinition extends AbstractRuleDefinition {

	protected DataSet executeDbcreateTableQuery(String sqlString, IRuleExecuteContext ruleExecuteContext)
			throws RuleBaseException {
		return SQLQueryExecuteUtils.executeTempTableQuery(sqlString, ruleExecuteContext.getRuntimeContext());
	}

	@Override
	public String[] getDependentCreator() {
		return new String[] { CreatorConstants.DBINIT };
	}

}
