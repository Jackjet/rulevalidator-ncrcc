package com.yonyou.nc.codevalidator.temptable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yonyou.nc.codevalidator.crossdb.CrossDBConnection;
import com.yonyou.nc.codevalidator.dao.DBUtil;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 
 * @author He Guan Yu
 * 
 * @date May 4, 2011
 * 
 */
public class DB2TempTableCreator implements TempTableCreator {

	public DB2TempTableCreator() {
		super();
	}

	public String createTempTable(Connection con, String table, String columns,
			String... idx) throws SQLException {
		String sql;
		Statement stmt = null;
		ResultSet rs = null;
		boolean isSQLTrans = false;
		boolean isAddTimeStamp = false;
		try {
			isSQLTrans = ((CrossDBConnection)con).isSQLTrans();
			isAddTimeStamp = ((CrossDBConnection)con).isAddTimeStamp();
			((CrossDBConnection)con).isAddTimeStamp();
			((CrossDBConnection)con).setSqlTrans(false);
			((CrossDBConnection)con).setAddTimeStamp(false);
			stmt = con.createStatement();
			columns = transDataType(columns);
			if(table!=null&&(table.startsWith("session.")||table.startsWith("SESSION.")))
				table=table.substring("session.".length());
			sql = "DECLARE GLOBAL TEMPORARY table " + table + "(" + columns
					+ ") NOT LOGGED ON COMMIT delete ROWS with replace ";
			Logger.debug("HH First: " + sql);
			stmt.executeUpdate(sql);
			if(idx != null && idx.length > 0) {
				for(int i = 0; i < idx.length; i++) {
					String idxColumn = idx[i];
					if (idxColumn != null && idxColumn.trim().length() != 0) {
						sql = "create index session.i_" + table+"_"+i + " on session." + table + "("
								+ idxColumn + ")";
						stmt.executeUpdate(sql);
					}
				}
			}

		} finally {
			((CrossDBConnection)con).setSqlTrans(isSQLTrans);
			((CrossDBConnection)con).setAddTimeStamp(isAddTimeStamp);
			DBUtil.closeRs(rs);
			DBUtil.closeStmt(stmt);
		}
		return "session." + table;
	}

	private String transDataType(String columns) {
		StringBuffer colStrs = new StringBuffer();
		for (String colStr : columns.split("(\\s)+")) {
			if (colStr.toUpperCase().startsWith("NUMBER(")) {
				colStr = colStr.toUpperCase().replace("NUMBER(", "DECIMAL(");
			}
//			if (colStr.toUpperCase().startsWith("CHAR(")) {
//				colStr = colStr.toUpperCase().replace("CHAR(", "VARCHAR(");
//			}
			colStrs.append(colStr).append(" ");
		}
		colStrs.setLength(colStrs.length() - 1);
		return colStrs.toString();
	}

	@Override
	public void setRowNum(int rowNum) {

	}

}
