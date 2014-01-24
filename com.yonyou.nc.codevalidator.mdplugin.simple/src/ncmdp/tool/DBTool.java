package ncmdp.tool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import ncmdp.pdmxml.Field;
import ncmdp.pdmxml.Table;
/**
 * 数据库连接的工具
 * @author wangxmn
 *
 */
public class DBTool {
	// 数据库类型常量
	public static final int DBTYPE_UNKNOWN = -1;

	public static final int DBTYPE_SQLSEVER = 0; // SQLSERVER数据库

	public static final int DBTYPE_ORACLE = 1; // ORACLE数据库

	public static final int DBTYPE_DB2 = 2; // DB2数据库

	public static final int DBTYPE_INFORMIX = 3;// informix数据库

	/**
	 * 获得连接
	 * @param driver
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(Driver driver, String url, String user, String password) throws Exception {
		Connection con = null;
		try {
			DriverManager.registerDriver(driver);
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw e;
		}
		return con;
	}

	/**
	 * 获得该数据库连接的所有表
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public static Table[] getAllTables(Connection con) throws Exception{
		ArrayList<Table> al = new ArrayList<Table>();
		try {
			String[] tableNames = getTableNames(con);
			int count = tableNames == null ? 0 : tableNames.length;
			for (int i = 0; i < count; i++) {
				Table table = new Table();
				table.setDisplayName(tableNames[i]);
				table.setName(tableNames[i]);
				al.add(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
		return al.toArray(new Table[0]);
	}

	/**
	 * 获得数据库连接的类型，sqlserver、oracle等
	 * @param con
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static int getDBType(Connection con) throws java.sql.SQLException {
		java.sql.DatabaseMetaData dmd = con.getMetaData();
		String dbname = dmd.getDatabaseProductName();
		int dbType = DBTYPE_UNKNOWN;
		dbname = dbname.toLowerCase();
		if (-1 != dbname.indexOf("sql server")) {
			dbType = DBTYPE_SQLSEVER;
		} else if (-1 != dbname.indexOf("oracle")) {
			dbType = DBTYPE_ORACLE;
		} else if (dbname.indexOf("db2") != -1) {
			dbType = DBTYPE_DB2;
		} else if (dbname.indexOf("informix") != -1) {
			dbType = DBTYPE_INFORMIX;
		}
		return dbType;
	}

	/**
	 * 获得该链接下所有数据的表名称
	 * @param con
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static String[] getTableNames(Connection con) throws java.sql.SQLException {
		java.sql.Statement stmt = null;
		java.sql.ResultSet dbmdrs = null;
		java.util.Vector<String> v = new java.util.Vector<String>();
		try {
			int dbType = getDBType(con);
			stmt = con.createStatement();
			java.sql.DatabaseMetaData dbmd = con.getMetaData();
			if (dbType == DBTYPE_SQLSEVER)
				dbmdrs = dbmd.getTables(null, null, null, new String[] { "TABLE" });
			else if (dbType == DBTYPE_ORACLE || dbType == DBTYPE_DB2 || dbType == DBTYPE_INFORMIX)
				dbmdrs = dbmd.getTables(null, dbmd.getUserName().toUpperCase(), null, new String[] { "TABLE" });
			while (dbmdrs.next()) {
				// 获得表名
				String tableName = dbmdrs.getString("TABLE_NAME");
				if ((dbType == DBTYPE_SQLSEVER && tableName.equalsIgnoreCase("dtproperties")) || (dbType == DBTYPE_ORACLE && tableName.equalsIgnoreCase("plan_table")))
					continue;
				if (!v.contains(tableName))
					v.add(tableName);
			}
		} finally {
			try {
				if (dbmdrs != null)
					dbmdrs.close();
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
		}
		String[] retrs = new String[v.size()];
		if (v.size() > 0)
			v.copyInto(retrs);
		return retrs;

	}

	/**
	 * 获得该链接下该数据库的各列信息
	 * @param con
	 * @param tableName
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static Field[] getTableColumns(java.sql.Connection con, String tableName) throws java.sql.SQLException {
		java.sql.ResultSet rs = null;
		List<String> v1 = new ArrayList<String>();
		List<Field> v = new ArrayList<Field>();
		List<String> pks = null;
		try {
			int dbType = getDBType(con);
			pks = getTablePKColNames(con, tableName);
			java.sql.DatabaseMetaData dbmd = con.getMetaData();
			if (dbType == DBTYPE_SQLSEVER)
				rs = dbmd.getColumns(null, null, tableName.toUpperCase(), "%");
			else
				rs = dbmd.getColumns(null, dbmd.getUserName().toUpperCase(), tableName.toUpperCase(), "%");
			while (rs.next()) {
				String colName = rs.getString("COLUMN_NAME");
				if (!v1.contains(colName)) {
					v1.add(colName);
				} else
					continue;
				String strColType = rs.getString("TYPE_NAME");
				int size = rs.getInt("COLUMN_SIZE");
				int digits = rs.getInt("DECIMAL_DIGITS");
				Field field = new Field();
				field.setKey(pks.contains(colName));
				field.setName(colName);
				field.setDisplayName(colName);
				field.setType(strColType);
				field.setLength(size + "");
				field.setPrecision(digits + "");
				v.add(field);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return v.toArray(new Field[0]);

	}

	/**
	 * 获得该数据表中主键的信息
	 * @param con
	 * @param tableName
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static ArrayList<String> getTablePKColNames(Connection con, String tableName) throws java.sql.SQLException {
		java.sql.ResultSet rs = null;
		ArrayList<String> v = new ArrayList<String>();
		try {
			int dbType = getDBType(con);
			java.sql.DatabaseMetaData dbmd = con.getMetaData();
			if (dbType == DBTYPE_SQLSEVER)
				rs = dbmd.getPrimaryKeys(null, null, tableName.toUpperCase());
			else if (dbType == DBTYPE_ORACLE || dbType == DBTYPE_DB2 || dbType == DBTYPE_INFORMIX)
				rs = dbmd.getPrimaryKeys(null, dbmd.getUserName().toUpperCase(), tableName.toUpperCase());
			while (rs.next()) {
				String pkStr = rs.getString("COLUMN_NAME");
				if (!v.contains(pkStr))
					v.add(pkStr);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {

			}
		}
		return v;
	}
}
