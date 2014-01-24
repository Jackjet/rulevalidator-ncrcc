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
 * ����Ľ���ű������壬ִ��ǰ��Ҫ����dbcreate��ʱ��ĳ�ʼ��
 * <P>
 * ���ڽ���ű���ʱ������ƺ��ֶΣ����Բο�DbCreateTable��DbCreateTableField���е���Ϣ
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
