package com.yonyou.nc.codevalidator.temptable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.dao.DBUtil;
import com.yonyou.nc.codevalidator.datasource.PhysicalDataSource;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.IExecutorContextHelper;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * @nopublish
 * @author hey
 * 
 */
public class OracleTempTableCreator implements TempTableCreator {

	static List<String> existsTable = new ArrayList<String>();

	public OracleTempTableCreator() {
		super();

	}

	public String createTempTable(Connection con, String TabName, String TabColumn, String... idx) throws SQLException {
		String sql;
		Connection n_con = null;
		Statement stmt = null;
		Statement n_stmt = null;

		try {
			stmt = con.createStatement();
			if (existsTable.contains(TabName)) {
				if (isTableExist(stmt, TabName)) {
					return TabName;
				}
			}

			n_con = getConnect();
			// ����ʱ���SQL
			n_stmt = n_con.createStatement();
			TabColumn = transDataType(TabColumn);
			sql = "create GLOBAL TEMPORARY table " + TabName + "(" + TabColumn + ") ON COMMIT preserve ROWS ";
			Logger.debug("HH First: " + sql);
			if(isTableExist(n_stmt, TabName)) {
				n_stmt.executeUpdate("drop table " + TabName);
			}
			n_stmt.executeUpdate(sql);
			if (idx != null && idx.length > 0) {
				for (int i = 0; i < idx.length; i++) {
					String IndColumn = idx[i];
					if (IndColumn != null && IndColumn.trim().length() != 0) {
						sql = "create index i_" + TabName + "_" + i + " on " + TabName + "(" + IndColumn + ")";
						n_stmt.executeUpdate(sql);
					}
				}
			}

			existsTable.add(TabName);
		} catch (Exception e) {
			if (isTableExist(stmt, TabName)) {
				existsTable.add(TabName);
				return TabName;
			} else {
				Logger.error("HH First: create temporaty table error: " + TabName, e);
				if (e instanceof SQLException) {
					throw (SQLException) e;
				} else {
					throw new SQLException(e);
				}
			}
		} finally {
			DBUtil.closeStmt(stmt);
			DBUtil.closeStmt(n_stmt);
			DBUtil.closeConnection(n_con);
		}
		return TabName;
	}

	private String transDataType(String columns) {
		StringBuffer colStrs = new StringBuffer();
		for (String colStr : columns.split("(\\s)+")) {
			if (colStr.toUpperCase().startsWith("DECIMAL(")) {
				colStr = colStr.toUpperCase().replace("DECIMAL(", "NUMBER(");
			} else if (colStr.toUpperCase().startsWith("NUMERIC(")) {
				colStr = colStr.toUpperCase().replace("NUMERIC(", "NUMBER(");
			}
			colStrs.append(colStr).append(" ");
		}
		colStrs.setLength(colStrs.length() - 1);
		return colStrs.toString();
	}

	private boolean isTableExist(Statement stmt, String table) {
		try {
			stmt.execute("delete from " + table);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Connection getConnect() throws SQLException, RuleBaseException {
		IExecutorContextHelper executorContextHelper = ExecutorContextHelperFactory.getExecutorContextHelper();
		String dataSource = executorContextHelper.getCurrentRuntimeContext().getDataSource();
		return new PhysicalDataSource(dataSource).getConnection();
	}

	public void setRowNum(int rowNum) {

	}

}
