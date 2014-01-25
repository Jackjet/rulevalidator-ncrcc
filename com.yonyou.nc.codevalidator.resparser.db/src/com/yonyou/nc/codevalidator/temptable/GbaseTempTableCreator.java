package com.yonyou.nc.codevalidator.temptable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import com.yonyou.nc.codevalidator.crossdb.CrossDBConnection;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class GbaseTempTableCreator implements TempTableCreator {

	private static Random rnd = new Random();

	public String createTempTable(Connection con, String TabName,
			String TabColumn, String... idx) throws SQLException {
		String sql;

		TabName = TabName + "_" + Math.abs((long) rnd.nextInt());
		Statement stmt = null;
		try {
			((CrossDBConnection) con).setSqlTrans(false);
			((CrossDBConnection) con).setAddTimeStamp(false);
			stmt = con.createStatement();
			TabColumn = TabColumn.toUpperCase();
			TabColumn = TabColumn.replaceAll(
					"DECIMAL\\s*\\(\\s*28\\s*\\,\\s*8\\s*\\)", "DECIMAL(18,4)");
			sql = "create TEMPORARY table " + TabName + "(" + TabColumn + ")";
			stmt.executeUpdate(sql);
			return TabName;
		} catch (Exception e) {
			if (isTableExist(stmt, TabName)) {
				return TabName;
			} else {
				Logger
						.error("HH First: create temporaty table error: "
								+ TabName, e);
				if (e instanceof SQLException) {
					throw (SQLException) e;
				} else {
					throw new SQLException(e);
				}
			}

		} finally {
			((CrossDBConnection) con).setSqlTrans(true);
			((CrossDBConnection) con).setAddTimeStamp(true);
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
