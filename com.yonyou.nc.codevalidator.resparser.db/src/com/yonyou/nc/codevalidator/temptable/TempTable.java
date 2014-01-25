package com.yonyou.nc.codevalidator.temptable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yonyou.nc.codevalidator.dao.DBUtil;
import com.yonyou.nc.codevalidator.resparser.datasource.DataSourceMetaMgr;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * �˴���������˵���� �������ڣ�(2004-2-23 13:49:03)
 * 
 * @author����־ǿ modified by hey
 */
public class TempTable {
	private int m_rowsnum = 2000;

	private static String oracleVersion = "x.x.x.x.x";
	
	public String createTempTable(Connection con, String tableName,
			String columns, String... idx) throws SQLException {
		if (tableName == null || columns == null || con == null)
			return null;
		String dbname;
		String m_tabname = null;
		// ������ݿ�����
		dbname = getDbType(con);
		TempTableCreator tempTable = TempTableCreatorFactory
				.getDBTemptable(dbname);
		tempTable.setRowNum(m_rowsnum);
		m_tabname = tempTable.createTempTable(con, tableName, columns, idx);
		return m_tabname;
	}

	static private String getDbType(Connection con) throws SQLException {

		// ��ȡ�������ӵ����ݿ�����
		String m_dbname = null;
		DatabaseMetaData dmd = con.getMetaData();
		String dpn = dmd.getDatabaseProductName();
		if (dpn.toUpperCase().indexOf("POSTGRES") != -1)
			return "POSTGRESQL";
		if (dpn.toUpperCase().indexOf("DB2") != -1)
			m_dbname = "DB2";
		if (dpn.toUpperCase().indexOf("ORACLE") != -1) {
			m_dbname = "ORACLE";
			String productVer = dmd.getDatabaseProductVersion();
			int idx = productVer.indexOf("Release ") + "Release ".length();
			oracleVersion = productVer.substring(idx, idx
					+ "9.2.0.x.0".length());
		}

		if (dpn.toUpperCase().indexOf("SQL") != -1)
			m_dbname = "SQL";
		if (dpn.toUpperCase().indexOf("INFORMIX") != -1)
			m_dbname = "INFORMIX";
		if (dpn.toUpperCase().indexOf("OSCAR") != -1)
			m_dbname = "OSCAR";
		if (dpn.toUpperCase().indexOf("GBASE") != -1) {
			m_dbname = "GBASE";
		}
		return m_dbname;
	}

	public void dropTempTable(Connection con, String TabName)
			throws SQLException {
		if (con == null)
			return;
		String dbname = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// ������ݿ�����
			dbname = getDbType(con);
			stmt = con.createStatement();
			if (dbname.equalsIgnoreCase("ORACLE")) {
				// ȡ�����ݿ����ʱ��
				if (TabName == null || TabName.length() == 0) {
					rs = stmt
							.executeQuery("select TABLE_NAME from user_tables where TEMPORARY='Y'");
					while (rs.next()) {
						String sql = "drop table " + rs.getString(1);
						stmt.execute(sql);
					}
				} else {
					String sql = "drop table " + TabName.trim();
					stmt.execute(sql);
				}
			} else if (dbname.equalsIgnoreCase("DB2")) {
				if (TabName != null) {
					String sql = "drop table " + TabName.trim();
					stmt.execute(sql);
				}
			}
		} catch (Exception e) {
			Logger.error("ɾ����ʱ���쳣!", e);
		} finally {
			DBUtil.closeRs(rs);
			DBUtil.closeStmt(stmt);
		}

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-9-28 15:33:09)
	 */
	public void setRowsnum(int num) {
		m_rowsnum = num;
	}

	public static void gatherStats(Connection con, String TabName,
			int columnCounts, int rowCounts) {
		ResultSet rs = null;
		Statement stmt = null;
		CallableStatement cstmt = null;
		Connection myCon = null;
		String dbname = null;
		try {
			// ������ݿ�����
			dbname = getDbType(con);
			if (!(dbname.equalsIgnoreCase("ORACLE"))) {
				return;
			}
			/*
			 * cc+ ���� ORACLE 9.2.0.4.x ֮��İ汾��ͨ������ optimizer_dynamic_sampling = 4
			 * ���Զ��ռ���ʱ��ͳ����Ϣ
			 */
			if (verHigher()) {
				return;
			}
			if (-1 == rowCounts) {
				stmt = con.createStatement();
				rs = stmt
						.executeQuery("select count(*) as CNT from " + TabName);
				if (rs.next()) {
					rowCounts = rs.getInt("CNT");
				}
			}
			int blockCounts = 0;
			blockCounts = (int) (7 * (rowCounts * columnCounts) / 5000);
			if (blockCounts == 0)
				blockCounts = 2;
			myCon = getConnect();
			String username = myCon.getMetaData().getUserName();
			String sql1 = "begin dbms_stats.set_table_stats(ownname=>'"
					+ username.toUpperCase() + "',tabname=>'"
					+ TabName.toUpperCase() + "',numrows=>" + rowCounts
					+ ",numblks=>" + blockCounts + "); end ;";
			cstmt = myCon.prepareCall(sql1);
			cstmt.execute();
		} catch (Exception e) {
		} finally {
			DBUtil.closeRs(rs);
			DBUtil.closeStmt(stmt);
			DBUtil.closeStmt(cstmt);
			DBUtil.closeConnection(myCon);
		}
	}

	static private Connection getConnect() throws SQLException {
		DataSourceMetaMgr manager = DataSourceMetaMgr.getInstance();
		DataSourceMeta meta = manager.getCurrentDataSourceMeta();
		String strUrl = meta.getDatabaseUrl();
		String user = meta.getUser();
		String pwd = meta.getPassword();
		Connection newCon = DriverManager.getConnection(strUrl, user, pwd);
		return newCon;
	}

	// cc+
	private static boolean verHigher() {
		if (oracleVersion.equals("x.x.x.x.x"))
			throw new IllegalArgumentException(
					"Cannot Parse Oracle Version in Temptable ...");
		if (oracleVersion.charAt(0) == '8')
			return false;
		if (oracleVersion.charAt(0) == '9') {
			if (oracleVersion.charAt(2) < '2')
				return false;
			if (oracleVersion.charAt(2) > '2')
				return true;
			if (oracleVersion.charAt(2) == '2') {
				if (oracleVersion.charAt(4) > '0')
					return true;
				if (oracleVersion.charAt(6) >= '4')
					return true;
			}
		}
		return true;
	}

}