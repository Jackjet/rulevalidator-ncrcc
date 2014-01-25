package com.yonyou.nc.codevalidator.connection;

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
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import com.yonyou.nc.codevalidator.dialect.DialectAdapter;
import com.yonyou.nc.codevalidator.dialect.DialectPreparedStatement;
import com.yonyou.nc.codevalidator.dialect.DialectStatement;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class PoolableConnection implements java.sql.Connection {
	// 这些常量本来可以依赖DBConsts的，但是fw无法依赖jdbcframework，所以这里冗余了代码。
	public final static int DB2 = 0;
	public final static int ORACLE = 1;
	public final static int SQLSERVER = 2;
	public final static int SYBASE = 3;
	public final static int INFORMIX = 4;
	public final static int HSQL = 5;
	public final static int OSCAR = 6;
	public final static int POSTGRESQL = 7;
	public final static int GBASE = 8;
	public final static int UNKOWNDATABASE = -1;

	private Connection m_connection;

	private DBConnectionPool pool;

	private DialectAdapter adapter = null;
	/**
	 * 本连接是否被关闭
	 */
	private volatile boolean beClosed = false;

	private int dbType = UNKOWNDATABASE;

	private String dbName = null;

	/**
	 * 用于记录是谁获得了这个连接，如果连接沲满了抛错里会把这些信息打印出来。
	 */
	private StackTraceElement[] stackTraces = null;

	public StackTraceElement[] getStackTraces() {
		return stackTraces;
	}

	public void setStackTraces(StackTraceElement[] stackTraces) {
		this.stackTraces = stackTraces;
	}

	public PoolableConnection(Connection c) {
		m_connection = c;
		try {
			DatabaseMetaData meta = c.getMetaData();
			dbName = meta.getDatabaseProductName().toLowerCase();
			adapter = new DialectAdapter(dbName);
		} catch (SQLException e) {
			Logger.error(" physical connection can't get db product name!", e);
		}
	}

	public int getDbType() {
		if (dbType == UNKOWNDATABASE) {
			if (-1 != dbName.indexOf("sql server")) {
				dbType = SQLSERVER;
			} else if (-1 != dbName.indexOf("oracle")) {
				dbType = ORACLE;
			} else if (dbName.indexOf("db2") != -1) {
				dbType = DB2;
			}
		}
		return dbType;
	}

	public PreparedStatement prepareStatement(String sql)
			throws java.sql.SQLException {

		DialectPreparedStatement dps = new DialectPreparedStatement(
				m_connection.prepareStatement(sql));
		dps.setAdapter(adapter);
		return dps;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws java.sql.SQLException {

		DialectPreparedStatement dps = new DialectPreparedStatement(
				m_connection.prepareStatement(sql, resultSetType,
						resultSetConcurrency));
		dps.setAdapter(adapter);
		return dps;
	}

	/**
	 * Creates a <code>Statement</code> object for sending SQL statements to the
	 * database. SQL statements without parameters are normally executed using
	 * Statement objects. If the same SQL statement is executed many times, it
	 * is more efficient to use a PreparedStatement
	 * <p/>
	 * JDBC 2.0
	 * <p/>
	 * Result sets created using the returned Statement will have forward-only
	 * type, and read-only concurrency, by default.
	 * 
	 * @return a new Statement object
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public Statement createStatement() throws java.sql.SQLException {
		DialectStatement dstmt = new DialectStatement(
				m_connection.createStatement());
		dstmt.setAdapter(adapter);
		return dstmt;
	}

	/**
	 * JDBC 2.0
	 * <p/>
	 * Creates a <code>Statement</code> object that will generate
	 * <code>ResultSet</code> objects with the given type and concurrency. This
	 * method is the same as the <code>createStatement</code> method above, but
	 * it allows the default result set type and result set concurrency type to
	 * be overridden.
	 * 
	 * @param resultSetType
	 *            a result set type; see ResultSet.TYPE_XXX
	 * @param resultSetConcurrency
	 *            a concurrency type; see ResultSet.CONCUR_XXX
	 * @return a new Statement object
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws java.sql.SQLException {

		DialectStatement dstmt = new DialectStatement(
				m_connection.createStatement(resultSetType,
						resultSetConcurrency));
		dstmt.setAdapter(adapter);
		return dstmt;
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {

		DialectStatement dstmt = new DialectStatement(
				m_connection.createStatement(resultSetType,
						resultSetConcurrency, resultSetHoldability));
		dstmt.setAdapter(adapter);
		return dstmt;
	}

	public void clearWarnings() throws java.sql.SQLException {

		m_connection.clearWarnings();
	}

	public ConnectionPool getPool() {
		return pool;
	}

	public void setPool(DBConnectionPool pool) {
		this.pool = pool;
	}

	public void close() throws java.sql.SQLException {
		if (!m_connection.getAutoCommit()) {
			m_connection.commit();
		}
		pool.removeOrStay(this);
	}

	public void reallyClose() throws SQLException {
		m_connection.close();
	}

	/**
	 * Makes all changes made since the previous commit/rollback permanent and
	 * releases any database locks currently held by the Connection. This method
	 * should be used only when auto-commit mode has been disabled.
	 * 
	 * @throws SQLException
	 *             if a database access error occurs
	 * @see #setAutoCommit
	 */
	public void commit() throws java.sql.SQLException {
		Logger.debug("commit connection");
		m_connection.commit();
	}

	/**
	 * Gets the current auto-commit state.
	 * 
	 * @return the current state of auto-commit mode
	 * @throws SQLException
	 *             if a database access error occurs
	 * @see #setAutoCommit
	 */
	public boolean getAutoCommit() throws java.sql.SQLException {
		return m_connection.getAutoCommit();
	}

	/**
	 * Returns the Connection's current catalog name.
	 * 
	 * @return the current catalog name or null
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public String getCatalog() throws java.sql.SQLException {
		return m_connection.getCatalog();
	}

	/**
	 * Gets the metadata regarding this connection's database. A Connection's
	 * database is able to provide information describing its tables, its
	 * supported SQL grammar, its stored procedures, the capabilities of this
	 * connection, and so on. This information is made available through a
	 * DatabaseMetaData object.
	 * 
	 * @return a DatabaseMetaData object for this Connection
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public java.sql.DatabaseMetaData getMetaData() throws java.sql.SQLException {

		return m_connection.getMetaData();
	}

	/**
	 * Gets this Connection's current transaction isolation level.
	 * 
	 * @return the current TRANSACTION_* mode value
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public int getTransactionIsolation() throws java.sql.SQLException {
		return m_connection.getTransactionIsolation();
	}

	public java.util.Map<String, Class<?>> getTypeMap()
			throws java.sql.SQLException {
		return m_connection.getTypeMap();
	}

	/**
	 * Returns the first warning reported by calls on this Connection.
	 */
	public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
		return m_connection.getWarnings();
	}

	public boolean isClosed() {
		return beClosed;
	}

	public boolean isReallyClose() throws SQLException {
		return m_connection.isClosed();
	}

	/**
	 * Tests to see if the connection is in read-only mode.
	 * 
	 * @return true if connection is read-only and false otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public boolean isReadOnly() throws java.sql.SQLException {
		return m_connection.isReadOnly();
	}

	/**
	 * Converts the given SQL statement into the system's native SQL grammar. A
	 * driver may convert the JDBC sql grammar into its system's native SQL
	 * grammar prior to sending it; this method returns the native form of the
	 * statement that the driver would have sent.
	 * 
	 * @param sql
	 *            a SQL statement that may contain one or more '?' parameter
	 *            placeholders
	 * @return the native form of this statement
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public String nativeSQL(String sql) throws java.sql.SQLException {
		return m_connection.nativeSQL(sql);
	}

	public java.sql.CallableStatement prepareCall(String sql)
			throws java.sql.SQLException {

		return m_connection.prepareCall(sql);
	}

	public java.sql.CallableStatement prepareCall(String sql,
			int resultSetType, int resultSetConcurrency)
			throws java.sql.SQLException {

		return m_connection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}

	public void rollback() throws java.sql.SQLException {
		m_connection.rollback();
	}

	public void setAutoCommit(boolean autoCommit) throws java.sql.SQLException {
		m_connection.setAutoCommit(autoCommit);
	}

	public void setCatalog(String catalog) throws java.sql.SQLException {
		m_connection.setCatalog(catalog);
	}

	public void setClosed(boolean newbeClosed) {
		beClosed = newbeClosed;
	}

	public void setReadOnly(boolean readOnly) throws java.sql.SQLException {
		m_connection.setReadOnly(readOnly);
	}

	public void setTransactionIsolation(int level) throws java.sql.SQLException {
		m_connection.setTransactionIsolation(level);
	}

	public java.sql.Connection getPhysicalConnection() {
		return m_connection;
	}

	public String toString() {
		return "DelegatingConnection" + System.identityHashCode(this) + "{"
				+ "isClosed=" + beClosed + ", connection=" + m_connection
				+ "'}'";
	}

	public int getHoldability() throws SQLException {
		return m_connection.getHoldability();
	}

	public void setHoldability(int holdability) throws SQLException {
		m_connection.setHoldability(holdability);
	}

	public Savepoint setSavepoint() throws SQLException {
		return m_connection.setSavepoint();

	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		m_connection.releaseSavepoint(savepoint);

	}

	public void rollback(Savepoint savepoint) throws SQLException {
		m_connection.rollback(savepoint);

	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {

		return m_connection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {

		return new DialectPreparedStatement(m_connection.prepareStatement(sql,
				autoGeneratedKeys));
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {

		return new DialectPreparedStatement(m_connection.prepareStatement(sql,
				resultSetType, resultSetConcurrency, resultSetHoldability));
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {

		return new DialectPreparedStatement(m_connection.prepareStatement(sql,
				columnIndexes));
	}

	public Savepoint setSavepoint(String name) throws SQLException {

		return m_connection.setSavepoint(name);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {

		return new DialectPreparedStatement(m_connection.prepareStatement(sql,
				columnNames));
	}

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return m_connection.createArrayOf(typeName, elements);
	}

	public Blob createBlob() throws SQLException {
		return m_connection.createBlob();
	}

	public Clob createClob() throws SQLException {
		return m_connection.createClob();
	}

	public NClob createNClob() throws SQLException {
		return m_connection.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return m_connection.createSQLXML();
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return m_connection.createStruct(typeName, attributes);
	}

	public Properties getClientInfo() throws SQLException {
		return m_connection.getClientInfo();
	}

	public String getClientInfo(String name) throws SQLException {
		return m_connection.getClientInfo(name);
	}

	public boolean isValid(int timeout) throws SQLException {
		return m_connection.isValid(timeout);
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		m_connection.setClientInfo(properties);
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		m_connection.setClientInfo(name, value);
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		m_connection.setTypeMap(map);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return m_connection.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return m_connection.unwrap(iface);
	}
}