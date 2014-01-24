package com.yonyou.nc.codevalidator.resparser.resource.utils;

import java.sql.Connection;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.IScriptResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.resparser.temp.TempTableExecContextOperator;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * SQL��ѯִ�еĹ�����
 * 
 * @author mazhqa
 * @since V2.3
 */
public final class SQLQueryExecuteUtils {

	private static final int SQL_IN_LIST_LIMIT = 200;

	private SQLQueryExecuteUtils() {

	}

	/**
	 * �ڵ�ǰ��runtimeContext��ִ��sql���Ƽ�ʹ�ø÷������в�ѯ����runtimeContext��Ϊ����
	 * 
	 * @param sql
	 * @param runtimeContext
	 * @return
	 * @throws RuleBaseException
	 */
	public static DataSet executeQuery(String sql, RuntimeContext runtimeContext) throws RuleBaseException {
		ScriptResourceQuery scriptResourceQuery = new ScriptResourceQuery(sql);
		scriptResourceQuery.setRuntimeContext(runtimeContext);
		ScriptDataSetResource resourceAsDataSet = ResourceManagerFacade.getResourceAsDataSet(scriptResourceQuery);
		return resourceAsDataSet.getDataSet();
	}

	/**
	 * ִ�ж����ѯ�����ʶ�����ʱ����ʱ����Ҫʹ�ñ��������в�ѯ
	 * 
	 * @param sql
	 * @param runtimeContext
	 * @return
	 * @throws RuleBaseException
	 */
	public static DataSet executeMultiLangQuery(String sql, RuntimeContext runtimeContext) throws RuleBaseException {
		return executeTempTableQuery(sql, runtimeContext);
	}

	/**
	 * ִ���漰����ʱ��Ĳ�ѯʱ����Ҫʹ�ô˷���
	 * 
	 * @param sql
	 * @param runtimeContext
	 * @return
	 * @throws RuleBaseException
	 */
	public static DataSet executeTempTableQuery(String sql, RuntimeContext runtimeContext) throws RuleBaseException {
		ScriptResourceQuery scriptResourceQuery = new ScriptResourceQuery(sql);
		scriptResourceQuery.setRuntimeContext(runtimeContext);
		Connection connection = TempTableExecContextOperator.getCurrentConnection();
		ScriptDataSetResource resourceAsDataSet = ResourceManagerFacade.getResourceAsDataSet(scriptResourceQuery,
				connection);
		return resourceAsDataSet.getDataSet();
	}

	/**
	 * �õ���ǰ���̣߳�ִ���еĶ�����ʱ������
	 * 
	 * @return
	 */
	public static String getCurrentMultiLangTableName() {
		return TempTableExecContextOperator.getTempTableExecContext().getMultiLangTableName();
	}

	/**
	 * �õ���ǰ���̣߳�ִ���еĽ���ű���ʱ������
	 * 
	 * @return
	 */
	public static String getDbCreateTableName() {
		return TempTableExecContextOperator.getTempTableExecContext().getDbcreateTableName();
	}

	/**
	 * �õ���ǰ���̣߳�ִ���еĽ���ű���ϸ��ʱ������
	 * 
	 * @return
	 */
	public static String getDbCreateDetailTableName() {
		return TempTableExecContextOperator.getTempTableExecContext().getDbcreateDetailTableName();
	}

	/**
	 * ����In�Ӿ�
	 */
	public static String buildSqlForIn(final String fieldname, final String[] fieldvalue) {
		StringBuilder sbSQL = new StringBuilder();
		sbSQL.append("(" + fieldname + " IN ( ");
		int len = fieldvalue.length;
		// ѭ��д������
		for (int i = 0; i < len; i++) {
			if (fieldvalue[i] != null && fieldvalue[i].trim().length() > 0) {
				sbSQL.append("'").append(fieldvalue[i].toString()).append("'");
				// �������� ÿ��ȡֵ�����",", �������һ��ȡֵ���治�����"," ���Ҽ��� oracle �� IN 254 ���ơ�ÿ
				// 200 �� ���� or һ�Ρ�ʱҲ�������","
				if (i != (fieldvalue.length - 1) && !(i > 0 && (i + 1) % SQL_IN_LIST_LIMIT == 0)) {
					sbSQL.append(",");
				}
			} else {
				return null;
			}

			// ���� oracle �� IN 254 ���ơ�ÿ 200 �� ���� or һ�Ρ�
			if (i > 0 && (i + 1) % SQL_IN_LIST_LIMIT == 0 && i != (fieldvalue.length - 1)) {
				sbSQL.append(" ) OR ").append(fieldname).append(" IN ( ");
			}
		}
		sbSQL.append(" )) ");
		return sbSQL.toString();
	}

	/**
	 * ���ݵ�ǰ����Դ�õ���Ӧ���ݿ�����Ϣ����������ֶΣ����Եȵȣ����������ݿ�Ĳ���
	 * @param tableName
	 * @param runtimeContext
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<TableColumn> getTableColumnInfo(String tableName, RuntimeContext runtimeContext)
			throws RuleBaseException {
		IScriptResourceQueryFactory scriptResourceQueryFactory = ResourceManagerFacade.getScriptResourceQueryFactory();
		return scriptResourceQueryFactory.getTableColumns(tableName, runtimeContext.getDataSource());
	}

}
