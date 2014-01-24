package com.yonyou.nc.codevalidator.temptable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import com.yonyou.nc.codevalidator.crossdb.CrossDBConnection;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class PostgreSQLTempTableCreator implements TempTableCreator {

	private static Random rnd = new Random();

	public String createTempTable(Connection con, String TabName, String TabColumn, String... idx) throws SQLException {
		String sql;

		TabName = TabName + "_" + Math.abs((long) rnd.nextInt());
		Statement stmt = null;
		boolean sqlTrans_old = ((CrossDBConnection) con).isSQLTrans();
		boolean addTimeStamp_old = ((CrossDBConnection) con).isAddTimeStamp();
		try {
			((CrossDBConnection) con).setSqlTrans(false);
			((CrossDBConnection) con).setAddTimeStamp(false);
			stmt = con.createStatement();
			sql = "create GLOBAL TEMPORARY table " + TabName + "(" + TabColumn + ") ON COMMIT drop";
			stmt.executeUpdate(sql);
			if (idx != null && idx.length > 0) {
				for (int i = 0; i < idx.length; i++) {
					String IndColumn = idx[i];
					if (IndColumn != null && IndColumn.trim().length() != 0) {
						sql = "create index i_" + TabName + "_" + i + " on " + TabName + "(" + IndColumn + ")";
						stmt.executeUpdate(sql);
					}
				}
			}
			return TabName;
		} catch (Exception e) {
			if (isTableExist(stmt, TabName)) {
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
			((CrossDBConnection) con).setSqlTrans(sqlTrans_old);
			((CrossDBConnection) con).setAddTimeStamp(addTimeStamp_old);
			if (stmt != null)
				stmt.close();
		}
	}

	private boolean isTableExist(Statement stmt, String table) {
		try {
			stmt.execute("delete from " + table);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void setRowNum(int rowNum) {
	}
}
