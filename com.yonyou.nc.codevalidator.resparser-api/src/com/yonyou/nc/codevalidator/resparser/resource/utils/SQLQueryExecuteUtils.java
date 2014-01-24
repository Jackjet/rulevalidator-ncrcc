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
 * SQL查询执行的工具类
 * 
 * @author mazhqa
 * @since V2.3
 */
public final class SQLQueryExecuteUtils {

	private static final int SQL_IN_LIST_LIMIT = 200;

	private SQLQueryExecuteUtils() {

	}

	/**
	 * 在当前的runtimeContext上执行sql，推荐使用该方法进行查询，拿runtimeContext作为参数
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
	 * 执行多语查询（访问多语临时表）的时候，需要使用本方法进行查询
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
	 * 执行涉及到临时表的查询时，需要使用此方法
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
	 * 得到当前（线程）执行中的多语临时表名称
	 * 
	 * @return
	 */
	public static String getCurrentMultiLangTableName() {
		return TempTableExecContextOperator.getTempTableExecContext().getMultiLangTableName();
	}

	/**
	 * 得到当前（线程）执行中的建库脚本临时表名称
	 * 
	 * @return
	 */
	public static String getDbCreateTableName() {
		return TempTableExecContextOperator.getTempTableExecContext().getDbcreateTableName();
	}

	/**
	 * 得到当前（线程）执行中的建库脚本详细临时表名称
	 * 
	 * @return
	 */
	public static String getDbCreateDetailTableName() {
		return TempTableExecContextOperator.getTempTableExecContext().getDbcreateDetailTableName();
	}

	/**
	 * 构造In子句
	 */
	public static String buildSqlForIn(final String fieldname, final String[] fieldvalue) {
		StringBuilder sbSQL = new StringBuilder();
		sbSQL.append("(" + fieldname + " IN ( ");
		int len = fieldvalue.length;
		// 循环写入条件
		for (int i = 0; i < len; i++) {
			if (fieldvalue[i] != null && fieldvalue[i].trim().length() > 0) {
				sbSQL.append("'").append(fieldvalue[i].toString()).append("'");
				// 单独处理 每个取值后面的",", 对于最后一个取值后面不能添加"," 并且兼容 oracle 的 IN 254 限制。每
				// 200 个 数据 or 一次。时也不能添加","
				if (i != (fieldvalue.length - 1) && !(i > 0 && (i + 1) % SQL_IN_LIST_LIMIT == 0)) {
					sbSQL.append(",");
				}
			} else {
				return null;
			}

			// 兼容 oracle 的 IN 254 限制。每 200 个 数据 or 一次。
			if (i > 0 && (i + 1) % SQL_IN_LIST_LIMIT == 0 && i != (fieldvalue.length - 1)) {
				sbSQL.append(" ) OR ").append(fieldname).append(" IN ( ");
			}
		}
		sbSQL.append(" )) ");
		return sbSQL.toString();
	}

	/**
	 * 根据当前数据源得到相应数据库表的信息，包括表的字段，属性等等，并屏蔽数据库的差异
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
