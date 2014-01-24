package com.yonyou.nc.codevalidator.temptable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.yonyou.nc.codevalidator.crossdb.CrossDBConnection;

/**
 * @nopublish
 * @author hey
 * 
 */
public class SQLServerTempTableCreator implements TempTableCreator {

	public String createTempTable(Connection con, String TabName, String TabColumn, String IndColumn)
			throws SQLException {
		return createTempTable(con, TabName, TabColumn, new String[] { IndColumn });
	}

	public String createTempTable(Connection con, String TabName, String TabColumn, String... idx) throws SQLException {
		String sql;
		String m_tabname;
		boolean bExist = true;
		Statement stmt = null;
		boolean sqlTrans_old = ((CrossDBConnection) con).isSQLTrans();
		boolean addTimeStamp_old = ((CrossDBConnection) con).isAddTimeStamp();
		try {
			((CrossDBConnection) con).setSqlTrans(false);
			((CrossDBConnection) con).setAddTimeStamp(false);
			stmt = con.createStatement();
			// 判断临时表名是否存在
			try {
				stmt.executeQuery("select count(*) from #" + TabName);
			} catch (Exception e) {
				bExist = false;
			}
			// 建临时表的SQL
			if (bExist) {
				sql = "drop table #" + TabName;
				stmt.executeUpdate(sql);
			}
			TabColumn = transDataType(TabColumn);
			sql = "create table #" + TabName + "(" + TabColumn + ")";
			stmt.executeUpdate(sql);
			if (idx != null && idx.length > 0) {
				for (int i = 0; i < idx.length; i++) {
					String IndColumn = idx[i];
					if (IndColumn != null && IndColumn.trim().length() != 0) {
						sql = "create index i_" + TabName + "_" + i + " on #" + TabName + "(" + IndColumn + ")";
						stmt.executeUpdate(sql);
					}
				}
			}
			m_tabname = "#" + TabName;
			return m_tabname;
		} finally {
			((CrossDBConnection) con).setSqlTrans(sqlTrans_old);
			((CrossDBConnection) con).setAddTimeStamp(addTimeStamp_old);
			if (stmt != null)
				stmt.close();
		}
	}

	public String transDataType(String columns) {
		StringBuffer colStrs = new StringBuffer();
		for (String colStr : columns.split("(\\s)+")) {
			if (colStr.toUpperCase().startsWith("CHAR(")) {
				colStr = "N" + colStr.toUpperCase();
			} else if (colStr.toUpperCase().startsWith("VARCHAR(")) {
				colStr = "N" + colStr.toUpperCase();
			}
			colStrs.append(colStr + " ");
		}
		colStrs.setLength(colStrs.length() - 1);
		return colStrs.toString();
	}

	public void setRowNum(int rowNum) {
	}

}
