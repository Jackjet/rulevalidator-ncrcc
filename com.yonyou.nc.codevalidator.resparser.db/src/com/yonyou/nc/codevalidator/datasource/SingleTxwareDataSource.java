package com.yonyou.nc.codevalidator.datasource;

import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.yonyou.nc.codevalidator.connection.PhysicalDbPool;
import com.yonyou.nc.codevalidator.resparser.datasource.DataSourceMetaMgr;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;

public class SingleTxwareDataSource implements DataSource {

	private static Map<String, PhysicalDbPool> pools = new HashMap<String, PhysicalDbPool>();

	/**
	 * NC原有缓存对数据源的连接，用于在线程完毕之后关闭线程上下文的Connection.
	 */
//	private static ThreadLocal<Map<String, SingleTxConnection>> connRef = new ThreadLocal<Map<String, SingleTxConnection>>();

	private DataSourceMeta meta;

	public SingleTxwareDataSource(String dsName) {
		if (dsName == null) {
			meta = DataSourceMetaMgr.getInstance().getDefaultDataSourceMeta();
		} else {
			meta = DataSourceMetaMgr.getInstance().getDataSourceMeta(dsName);
		}
		init(meta);
	}

	public SingleTxwareDataSource() {
		meta = DataSourceMetaMgr.getInstance().getDefaultDataSourceMeta();
		init(meta);
	}

	private void init(DataSourceMeta meta) {
		if (pools.get(meta.getDataSourceName()) == null) {
			PhysicalDbPool newPool = new PhysicalDbPool(meta);
			pools.put(meta.getDataSourceName(), newPool);
		}
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public void setLoginTimeout(int seconds) throws SQLException {

	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	public Connection getConnection() throws SQLException {
//		Map<String, SingleTxwareDataSource.SingleTxConnection> cs = connRef.get();
//		if (cs == null) {
//			cs = new HashMap<String, SingleTxwareDataSource.SingleTxConnection>();
//		}
//		SingleTxConnection c = (SingleTxConnection) cs.get(this.meta.getDataSourceName());
//		if (c != null) {
//			return c;
//		} else {
			PhysicalDbPool pool = pools.get(meta.getDataSourceName());
			SingleTxConnection connection = new SingleTxConnection(pool.getConnection());
			connection.setAutoCommit(false);
//			cs.put(meta.getDataSourceName(), c);
//			connRef.set(cs);
			return connection;
//		}
	}

	public Connection getConnection(String user, String pwd) throws SQLException {
		throw new SQLException("not support this mode to access db");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("not supported");
	}

	public static class SingleTxConnection implements Connection {

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return conn.unwrap(iface);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return conn.isWrapperFor(iface);
		}

		public Statement createStatement() throws SQLException {
			return conn.createStatement();
		}

		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return conn.prepareStatement(sql);
		}

		public CallableStatement prepareCall(String sql) throws SQLException {
			return conn.prepareCall(sql);
		}

		public String nativeSQL(String sql) throws SQLException {
			return conn.nativeSQL(sql);
		}

		public void setAutoCommit(boolean autoCommit) throws SQLException {
			conn.setAutoCommit(autoCommit);
		}

		public boolean getAutoCommit() throws SQLException {
			return conn.getAutoCommit();
		}

		public void commit() throws SQLException {
			conn.commit();
		}

		public void rollback() throws SQLException {
			conn.rollback();
		}

		public void close() throws SQLException {
			 conn.close();
		}

		public boolean isClosed() throws SQLException {
			return conn.isClosed();
		}

		public DatabaseMetaData getMetaData() throws SQLException {
			return conn.getMetaData();
		}

		public void setReadOnly(boolean readOnly) throws SQLException {
			conn.setReadOnly(readOnly);
		}

		public boolean isReadOnly() throws SQLException {
			return conn.isReadOnly();
		}

		public void setCatalog(String catalog) throws SQLException {
			conn.setCatalog(catalog);
		}

		public String getCatalog() throws SQLException {
			return conn.getCatalog();
		}

		public void setTransactionIsolation(int level) throws SQLException {
			conn.setTransactionIsolation(level);
		}

		public int getTransactionIsolation() throws SQLException {
			return conn.getTransactionIsolation();
		}

		public SQLWarning getWarnings() throws SQLException {
			return conn.getWarnings();
		}

		public void clearWarnings() throws SQLException {
			conn.clearWarnings();
		}

		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return conn.createStatement(resultSetType, resultSetConcurrency);
		}

		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return conn.getTypeMap();
		}

		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			conn.setTypeMap(map);
		}

		public void setHoldability(int holdability) throws SQLException {
			conn.setHoldability(holdability);
		}

		public int getHoldability() throws SQLException {
			return conn.getHoldability();
		}

		public Savepoint setSavepoint() throws SQLException {
			return conn.setSavepoint();
		}

		public Savepoint setSavepoint(String name) throws SQLException {
			return conn.setSavepoint(name);
		}

		public void rollback(Savepoint savepoint) throws SQLException {
			conn.rollback(savepoint);
		}

		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			conn.releaseSavepoint(savepoint);
		}

		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return conn.prepareStatement(sql, autoGeneratedKeys);
		}

		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return conn.prepareStatement(sql, columnIndexes);
		}

		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return conn.prepareStatement(sql, columnNames);
		}

		public Clob createClob() throws SQLException {
			return conn.createClob();
		}

		public Blob createBlob() throws SQLException {
			return conn.createBlob();
		}

		public NClob createNClob() throws SQLException {
			return conn.createNClob();
		}

		public SQLXML createSQLXML() throws SQLException {
			return conn.createSQLXML();
		}

		public boolean isValid(int timeout) throws SQLException {
			return conn.isValid(timeout);
		}

		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			conn.setClientInfo(name, value);
		}

		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			conn.setClientInfo(properties);
		}

		public String getClientInfo(String name) throws SQLException {
			return conn.getClientInfo(name);
		}

		public Properties getClientInfo() throws SQLException {
			return conn.getClientInfo();
		}

		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return conn.createArrayOf(typeName, elements);
		}

		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return conn.createStruct(typeName, attributes);
		}

		// private void doClose() throws SQLException {
		// conn.close();
		// }

		private Connection conn;

		SingleTxConnection(Connection conn) {
			this.conn = conn;
		}

		public Connection getOriginalConn() {
			return conn;
		}
	}
	
	/**
	 * TODO: mazhqa 这步操作是非常有疑惑的，不确定是否能够清理数据库连接池
	 * 在客户端由于需要保证更改design数据源更改后，对应的数据库连接也更改；
	 * 在服务端可能数据源的修改频率不会很高
	 */
	public synchronized static void clearPool() {
		pools.clear();
	}

}
