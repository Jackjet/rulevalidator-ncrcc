package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.dbinit.DbInitTableFieldValue;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.CreatorConstants;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * �����Ԥ�ýű��ű������壬ִ��ǰ��Ҫ����dbinit��ʱ��ĳ�ʼ��
 * <P>
 * ����Ԥ�ýű���ʱ������ƺ��ֶΣ����Բο�DbInitTableFieldValue
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
