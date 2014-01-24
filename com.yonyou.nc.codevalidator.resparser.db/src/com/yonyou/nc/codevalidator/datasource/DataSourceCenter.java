/*
 * �������� 2005-8-19
 *
 * TODO 
 */
package com.yonyou.nc.codevalidator.datasource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.yonyou.nc.codevalidator.connection.DBMetaInfo;
import com.yonyou.nc.codevalidator.dao.DBUtil;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.type.DBConsts;

/**
 * @author hey ���ݿ���Ϣ�࣬�ṩ����Դ��Ӧ���ݿ�ľ�����Ϣ�������ݿ����ͣ����ݿ�汾�� �������Ǹ�����
 */
public class DataSourceCenter implements DBConsts {

	
	static boolean isRunningInPlugin = true;

	static DataSourceProvider provider = new SingleTxDataSourceProvider();

//	static {
//		try {
//			Class.forName("nc.uap.mde.lib.Activator");
//			isRunningInPlugin = true;
//		} catch (ClassNotFoundException e) {
//			isRunningInPlugin = false;
//		}
//
//		if (isRunningInPlugin) {
//			try {
//				provider = (DataSourceProvider) Class.forName(
//						"nc.uap.mde.lib.PluginDataSourceProvider")
//						.newInstance();
//			} catch (Exception e) {
//			}
//		}
//	}

	private Map<String, DBMetaInfo> metaCache = new ConcurrentHashMap<String, DBMetaInfo>();

	static private DataSourceCenter center = new DataSourceCenter();

	private Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<String, DataSource>();

	private final String DEFAULT_DS = "design";

	private static ThreadLocal<DataSourceProvider> tlProvider = new ThreadLocal<DataSourceProvider>();


	public DataSourceProvider getTLDataSourceProvider() {
		return tlProvider.get();
	}

	public void removeProvider() {
		tlProvider.set(null);
	}

	private DataSourceCenter() {
	}

	static public DataSourceCenter getInstance() {

		return center;
	}

	public void clearCache() {
		center.dataSourceCache.clear();
		provider.clean();
	}

	/**
	 * �õ�����Դ����
	 * 
	 * @return ����Դ����
	 */
	public String getSourceName() {
		return DEFAULT_DS;
	}

	/**
	 * �õ�Ĭ�����ݿ�����
	 * 
	 * @return ���ݿ�����
	 * @throws java.sql.SQLException
	 *             ��������׳�SQLException
	 */
	public Connection getConnection() throws SQLException {
		String sourceName = getSourceName();
		Connection con = getConnection(sourceName);
		return con;
	}

	/**
	 * ��������Դ���Ƶõ����ݿ�����
	 * 
	 * @return ���ݿ�����
	 * @throws SQLException
	 *             ��������׳�SQLException
	 */
	public Connection getConnection(String sourceName) throws SQLException {
		// �������Ա����Ϊ�գ���õ�Ĭ������Դ����
		String name = sourceName == null ? getSourceName() : sourceName;

		try {
			// �����Thread����Provider
			DataSourceProvider dsProvider = tlProvider.get();
			if (dsProvider != null) {
				DataSource dataSource = new SingleTxDataSourceProvider()
						.getDataSource(name);
				Connection conn = dataSource.getConnection();
				if (!metaCache.containsKey(name)) {
					initMetaCache(name, conn);
				}
				return conn;
			}

			// ����ǵ�һ��ȡ����
			DataSource ds = dataSourceCache.get(name);
			if (ds == null) {
				try {
					if (isRunningInPlugin && provider != null) {
						ds = provider.getDataSource(name);
					}
//					if (ds == null) {
//						ds = (DataSource) NCLocator.getInstance().lookup(name);
//					}
				} catch (Throwable e) {
					Logger.error("can not find datasource" + name, e);
					throw new RuntimeException("can not find datasource: "
							+ name);
				}
				Connection conn = null;
				synchronized (this) {
					conn = getDiffConnection(ds);
					dataSourceCache.put(name, ds);
					initMetaCache(sourceName, conn);
				}
				return conn;
			}
			// ������ǵ�һ������
			return getDiffConnection(ds);
		} catch (NullPointerException e) {
			Logger.error("get connector from database error: " + name, e);
			throw new SQLException("Can't get connection from database(" + name
					+ ")");
		}
	}

	private void initMetaCache(String dsName, Connection conn)
			throws SQLException {
		DatabaseMetaData DBMeta = conn.getMetaData();
		int DBType = getDbType(DBMeta);
		String DBName = getDbName(DBMeta);
		boolean isBatch = isSupportBatch(DBMeta);
		boolean isODBC = isODBC(DBMeta);
		int version = getDbVersion(conn);
		DBMetaInfo meta = new DBMetaInfo(DBType, version, DBName, isBatch,
				isODBC);
		metaCache.put(dsName, meta);
	}

	private Connection getDiffConnection(DataSource ds) throws SQLException {
		return ds.getConnection();
//		if (ds instanceof nc.bs.framework.ds.SingleTxwareDataSource
//				|| ds instanceof PhysicalDataSource) {
//
//
//		}
//		if (RuntimeEnv.isRunningInWebSphere()) {
//			try {
//				return WASDatasourceProcessor.getConnection(ds);
//			} catch (Exception e) {
//				Logger.warn("get connection from websphere datasource error", e);
//				return ds.getConnection();
//			}
//		} else
//			return ds.getConnection();
	}

	/**
	 * �õ����ݿ�����
	 * 
	 * @return
	 */
	public int getDatabaseType() {
		String sourceName = getSourceName();
		return getDatabaseType(sourceName);
	}

	/**
	 * ��������Դ���Ƶõ����ݿ�����
	 * 
	 * @param sourceName
	 *            ����Դ����
	 * @return ���ݿ�����
	 */
	public int getDatabaseType(String sourceName) {
		if (sourceName == null)
			sourceName = getSourceName();
		DBMetaInfo meta = metaCache.get(sourceName);
		if (meta == null) {
			Connection con = null;
			try {
				con = getConnection(sourceName);
				meta = metaCache.get(sourceName);
			} catch (SQLException e) {
				Logger.error("can not connecto to datassource: " + sourceName,
						e);
				throw new RuntimeException("connect to datasource error: "
						+ sourceName);
			} finally {
				DBUtil.closeConnection(con);
			}

		}
		return meta.getType();
	}

	/**
	 * �õ�Ĭ������Դ�����ݿ�����
	 * 
	 * @return
	 */
	public String getDatabaseName() {
		String sourceName = getSourceName();
		return getDatabaseName(sourceName);
	}

	/**
	 * ��������Դ���Ƶõ����ݿ�����
	 * 
	 * @param sourceName
	 * @return
	 */
	public String getDatabaseName(String sourceName) {
		if (sourceName == null)
			sourceName = getSourceName();
		DBMetaInfo meta = metaCache.get(sourceName);
		if (meta == null)
			return JDBC_SQLSERVER;
		return meta.getName();
	}

	/**
	 * �õ���Ĭ������Դ�����ݿ�汾
	 * 
	 * @return
	 */
	public int getDatabaseVersion() {
		String sourceName = getSourceName();

		return getDatabaseVersion(sourceName);
	}

	/**
	 * �Ƿ�֧����ִ��
	 * 
	 * @param sourceName
	 *            ����Դ����
	 * @return
	 */
	public boolean isSupportBatch(String sourceName) {
		if (sourceName == null)
			sourceName = getSourceName();
		DBMetaInfo meta = metaCache.get(sourceName);
		if (meta == null)
			return true;
		return meta.isSupportBatch();

	}

	/**
	 * �Ƿ�֧����ִ��
	 * 
	 * @return
	 */
	public boolean isSupportBatch() {

		String sourceName = getSourceName();
		return isSupportBatch(sourceName);
	}

	/**
	 * �Ƿ���ODBC
	 * 
	 * @return
	 */
	public boolean isODBC() {

		String sourceName = getSourceName();
		return isODBC(sourceName);
	}

	/**
	 * @param sourceName
	 * @return
	 */
	public boolean isODBC(String sourceName) {
		if (sourceName == null)
			sourceName = getSourceName();
		DBMetaInfo meta = metaCache.get(sourceName);
		if (meta == null)
			return true;
		return meta.isODBC();
	}

	/**
	 * �õ����ݿ�汾
	 * 
	 * @param sourceName
	 * @return
	 */
	public int getDatabaseVersion(String sourceName) {
		if (sourceName == null)
			sourceName = getSourceName();
		DBMetaInfo meta = metaCache.get(sourceName);
		if (meta == null)
			return 1;
		return meta.getVersion();
	}

	/**
	 * �õ����ݿ�����
	 * 
	 * @return
	 * @throws SQLException
	 *             ��������׳��쳣
	 */
	public int getDbType(DatabaseMetaData dmd) throws SQLException {
		String dpn = dmd.getDatabaseProductName();
		if (dpn.toUpperCase().indexOf("GBASE") != -1)
			return GBASE;
		if (dpn.toUpperCase().indexOf("POSTGRESQL") != -1)
			return POSTGRESQL;
		if (dpn.toUpperCase().indexOf("DB2") != -1)
			return DB2;
		if (dpn.toUpperCase().indexOf("ORACLE") != -1)
			return ORACLE;
		if (dpn.toUpperCase().indexOf("SQL") != -1)
			return SQLSERVER;
		if (dpn.toUpperCase().indexOf("SYBASE") != -1)
			return SYBASE;
		if (dpn.toUpperCase().indexOf("INFORMIX") != -1)
			return INFORMIX;
		throw new RuntimeException("unknow database");
	}

	/**
	 * @param dmd
	 * @return
	 * @throws SQLException
	 */
	private String getDbName(DatabaseMetaData dmd) throws SQLException {
		// ��ȡ�������ӵ����ݿ�����
		String m_dbname = null;
		String dpn = dmd.getDatabaseProductName();
		if (dpn.toUpperCase().indexOf("GBASE") != -1)
			m_dbname = "GBASE";
		if (dpn.toUpperCase().indexOf("POSTGRESQL") != -1)
			m_dbname = "POSTGRESQL";
		if (dpn.toUpperCase().indexOf("DB2") != -1)
			m_dbname = "DB2";
		if (dpn.toUpperCase().indexOf("ORACLE") != -1)
			m_dbname = "ORACLE";
		if (dpn.toUpperCase().indexOf("SQL") != -1)
			m_dbname = "SQL";
		if (dpn.toUpperCase().indexOf("INFORMIX") != -1)
			m_dbname = "INFORMIX";
		// if (dpn.toUpperCase().indexOf("OSCAR") != -1)
		// m_dbname = "OSCAR";
		return m_dbname;
	}

	/**
	 * �ж��Ƿ���jdbc odbc������
	 * 
	 * @return
	 * @throws SQLException
	 */
	private boolean isODBC(DatabaseMetaData dmd) throws SQLException {
		String url = dmd.getURL();
		return url.indexOf("odbc") >= 0;
	}

	/**
	 * @param dmd
	 * @return
	 * @throws SQLException
	 */
	private boolean isSupportBatch(DatabaseMetaData dmd) throws SQLException {
		return dmd.supportsBatchUpdates();

	}

	/**
	 * @throws SQLException
	 */
	private int getDbVersion(Connection m_con) throws SQLException {
		int m_DbVersion = -1;
		java.sql.Statement stmt = null;
		try {
			if (m_con == null)
				return m_DbVersion;
			DatabaseMetaData meta = m_con.getMetaData();
			String strDbVersion;
			String strVersion;
			int dbType = getDbType(meta);
			if (dbType != ORACLE) {
				return m_DbVersion;
			} else {
				stmt = m_con.createStatement();
				java.sql.ResultSet rs = stmt
						.executeQuery("select value from V$parameter where lower(name)='compatible'");
				if (rs.next()) {
					strDbVersion = rs.getString(1);
					StringTokenizer dbVersion = new StringTokenizer(
							strDbVersion, ".");
					if (dbVersion.hasMoreTokens()) {
						strVersion = dbVersion.nextToken();
						m_DbVersion = Integer.parseInt(strVersion);
					}
				}
			}
			return m_DbVersion;
		} catch (SQLException e) {
			return m_DbVersion;
		} finally {
			if (stmt != null)
				stmt.close();

		}
	}

}
