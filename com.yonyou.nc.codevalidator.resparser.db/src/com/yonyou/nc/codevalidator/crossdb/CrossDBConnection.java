package com.yonyou.nc.codevalidator.crossdb;

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
import java.util.Vector;

import com.yonyou.nc.codevalidator.adapter.Adapter;
import com.yonyou.nc.codevalidator.adapter.AdapterFactory;
import com.yonyou.nc.codevalidator.connection.SQLStruct;
import com.yonyou.nc.codevalidator.datasource.DataSourceCenter;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sqltrans.SqlTranslator;
import com.yonyou.nc.codevalidator.type.DBConsts;

/**
 * @nopublish 数据库连接 created by hey in 2005.5
 */
public class CrossDBConnection extends CrossDBObject implements Connection, DBConsts {

	//
	private String dataSource = null;

	private Adapter adapter = null;

	// 包装的connection
	private Connection realConnection = null;

	// Statement缓冲
	private Vector<Statement> statements = new Vector<Statement>();

	// 数据库类型
	private int dbType = -1;

	// 数据库代码
	// private String dbCode = null;

	// SQL转换器
	private SqlTranslator translator = null;

	// 是否关闭
	private boolean closed = false;

	// JdbcOdbcStatement是否支持数据库批处理*
	private boolean batchSupported = true;

	// 在本连接中是否启动SQL翻译器
	private boolean enableTranslator = true;

	// 是否需要对sql server2000下JdbcOdbcDriver的特殊处理
	private boolean jdbcOdbc = false;

	// 是否需要修正JDBCOdcb桥的双字节处理的Bug*
	private boolean jdbcOdbcBug = false;

	// 填加时间戳
	private boolean addTimeStamp = true;

	// databasemetadata类，提高metadata数据的读取
	private DatabaseMetaData dbmd = null;

	// Connection记数器
	// private static int counter;

	protected int id;

	// 翻译语句缓冲
	// private LRUCache cache = null;

	/**
	 * @param conn
	 * @throws SQLException
	 */
	public CrossDBConnection(Connection conn, String dataSource) throws SQLException {
		super();
		this.dataSource = dataSource;
		DataSourceCenter sourceCenter = DataSourceCenter.getInstance();
		realConnection = conn;
		batchSupported = sourceCenter.isSupportBatch(dataSource);
		jdbcOdbc = sourceCenter.isODBC(dataSource);
		try {
			dbType = sourceCenter.getDatabaseType(dataSource);
		} catch (Exception e) {
			Logger.debug("maybe offline");
		}
		if (dbType == UNKOWNDATABASE)
			dbType = sourceCenter.getDbType(conn.getMetaData());
		// 得到翻译器
		// cache = SQLCache.getInstance().getCache(dataSource);
		translator = new SqlTranslator(dbType);
		// 得到适配器
		adapter = AdapterFactory.getAdapter(dbType);
		adapter.setNativeConn(realConnection);

		translator.setConnection(this);
		// boolean stx =
		// (realConnection.getClass().getName().startsWith("nc.bs.framework.ds"));
		// NativeJdbcExtractor extractor = null;
		// if (!stx) {
		// try {
		// extractor = NativeJdbcExtractorFactory.createJdbcExtractor();
		// } catch (Throwable t) {
		// Logger.error("create jdbc extractor error, maybe offline");
		// }
		// }
		//
		// if (extractor == null) {
		// extractor = new SingleTxJdbcExtractor();
		// }
		//
		// id = System.identityHashCode(extractor.getNativeConnection(conn));
		// // TODO:NEED HGY AUDIT.if the connection has been used by the
		// thread,then do not map again.
		// //liujb+ 2012-05-07 add sqlserver suppport.
		// ThreadTracer.getInstance().openConnection(dataSource, id);
		// if (ThreadTracer.getInstance().isUserDebug() &&
		// !ThreadTracer.getInstance().isExistConn(id)) {
		// StringBuffer mapInfo = new
		// StringBuffer(System.getProperty("nc.server.name"));
		// mapInfo.append("#");
		// mapInfo.append(Thread.currentThread().getName());
		// mapSessionToDB(mapInfo.toString());
		// }
		// liujb+ remove this method.
		// initHistory(dataSource, conn);
	}

//	// TODO:NEED HGY AUDIT.2012-05-07 liujb+ add support sql server.
//	private void mapSessionToDB(String value) {
//		if (value == null)
//			return;
//		if (dbType != ORACLE && dbType != SQLSERVER && dbType != DB2) {
//			return;
//		}
//		PreparedStatement statment = null;
//		try {
//			// ThreadTracer.getInstance().updateEvent("map thread to db.");
//			if (closed)
//				return;
//			if (dbType == ORACLE) {
//				try {
//					statment = prepareCall("call DBMS_SESSION.SET_IDENTIFIER(?)");
//					statment.setString(1, value);
//					statment.execute();
//				} finally {
//					close(statment);
//				}
//			} else if (dbType == SQLSERVER) {
//				try {
//					statment = prepareStatement("SET CONTEXT_INFO " + byte2hex(value.getBytes()));
//					statment.execute();
//				} finally {
//					close(statment);
//				}
//			} else if (dbType == DB2) {
//				try {
//					// need jdbc4 driver.
//					realConnection.setClientInfo("ClientUser", value);
//				} catch (Throwable ex) {
//				}
//			}
//		} catch (SQLException e) {
//			Logger.error(e.getMessage());
//		} finally {
//			close(statment);
//			// ThreadTracer.getInstance().updateEvent("end map thread to db");
//		}
//	}
//
//	private void close(PreparedStatement statment) {
//		if (statment != null) {
//			try {
//				statment.close();
//				statment = null;
//			} catch (Exception ex) {
//				// ex.printStackTrace();
//			}
//		}
//	}
//
//	private String byte2hex(byte[] ba) {
//		if (ba == null || ba.length == 0)
//			return "0x0";
//		String hs = "0x";
//		String stmp = "";
//		for (int i = 0; i < ba.length; i++) {
//			stmp = (java.lang.Integer.toHexString(ba[i] & 0XFF));
//			if (stmp.length() == 1)
//				hs = hs + "0" + stmp;
//			else
//				hs = hs + stmp;
//		}
//		return hs;
//	}

	// liujb+ remove this.
	// private void initHistory(String name, Connection conn) {
	// SqlHistoryMap sqlHistoryMap = (SqlHistoryMap)
	// SqlHistoryMap.getCurrentSqlHistoryMap();
	// SqlHistory sqlHistory = sqlHistoryMap.getSqlHistory(id);
	// if (sqlHistory == null) {
	// sqlHistory = new SqlHistory();
	// sqlHistory.setDataSource(name);
	// sqlHistory.setConnId(id);
	// sqlHistory.setThreadName(Thread.currentThread().getName());
	// sqlHistory.setThrowable(new Throwable());
	// sqlHistory.setClientAddress((String) Logger.getMDC("remoteAddr"));
	// sqlHistory.setClientPort("" + Logger.getMDC("remotePort"));
	// sqlHistoryMap.addSqlHistory(sqlHistory);
	// } else {
	// sqlHistory.setThrowable(new Throwable());
	// }
	//
	// }

	/**
	 * @param conn
	 * @param moduleLang
	 *            默认为0，如果不是0就进行内码转换
	 * @throws SQLException
	 */
	public CrossDBConnection(Connection conn) throws SQLException {
		this(conn, DataSourceCenter.getInstance().getSourceName());
	}

	public void clearWarnings() throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.trace(getId());
			realConnection.clearWarnings();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void close() throws SQLException {
		try {
			// TODO:NEED HGY AUDIT.
			// if (!closed) {
			// ThreadTracer.getInstance().closeConnection(dataSource, id);
			// // if (ThreadTracer.getInstance().isUserDebug()) {
			// // mapSessionToDB(null);
			// // }
			// }
			// if (Logger.isInfoEnabled())
			// Trace.traceDetail("Close " + getId());
			closed = true;
			closeStatements();
			// if(dbType==DBConsts.DB2)
			// TempTabMgr.getInstance().dropTempTables(this);
			realConnection.close();

		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void commit() throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.trace(getId());
			// TODO:NEED HGY TO AUDIT.
			// ThreadTracer.getInstance().updateEvent("begin commit");
			realConnection.commit();
			// ThreadTracer.getInstance().updateEvent("end commit");
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public Statement createStatement() throws SQLException {
		try {
			CrossDBStatement st = null;
			st = new CrossDBStatement(realConnection.createStatement(), this, dbType, batchSupported, jdbcOdbcBug,
					dataSource);
			convertLang(st);
			registerStatement(st);
			// if (Trace.isEnabled())
			// Trace.traceResult("Create:" + st.getId());
			return st;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * 转换语言种类
	 * 
	 * @param st
	 */
	private void convertLang(CrossDBStatement st) {
		if (nModuleLang != 0) {
			st.nModuleLang = nModuleLang;
			st.setGU_CONVERT(true);
			st.setUG_CONVERT(true);
		}
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.trace(getId(), resultSetType + "," + resultSetConcurrency);
			CrossDBStatement stat = new CrossDBStatement(realConnection.createStatement(resultSetType,
					resultSetConcurrency), this, dbType, batchSupported, jdbcOdbcBug, dataSource);
			convertLang(stat);
			registerStatement(stat);
			return stat;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	public boolean getAutoCommit() throws SQLException {
		// if (Trace.isEnabled())
		// Trace.trace(getId());
		try {
			return realConnection.getAutoCommit();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public String getCatalog() throws SQLException {
		// if (Trace.isEnabled())
		// Trace.trace(getId());
		checkClosed();
		try {
			return realConnection.getCatalog();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * 获得metadata一个connection只获取一次
	 * 
	 * @return
	 * @throws SQLException
	 */
	public java.sql.DatabaseMetaData getMetaData() throws SQLException {
		// if (Trace.isEnabled())
		// Trace.trace(getId());
		checkClosed();
		try {
			if (dbmd == null) {
				dbmd = realConnection.getMetaData();
			}
			return dbmd;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public int getTransactionIsolation() throws SQLException {
		checkClosed();
		try {
			return realConnection.getTransactionIsolation();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		checkClosed();
		return realConnection.getTypeMap();
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		checkClosed();
		try {
			return realConnection.getWarnings();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public boolean isClosed() throws SQLException {
		checkClosed();
		return realConnection.isClosed();
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public boolean isReadOnly() throws SQLException {
		checkClosed();
		try {
			return realConnection.isReadOnly();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public String nativeSQL(String sql) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), sql);
		checkClosed();
		try {
			String sqlFixed = translate(sql);
			String val = realConnection.nativeSQL(sqlFixed);
			// if (Trace.isEnabled())
			// Trace.traceResultQuote(sqlFixed);
			return val;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), sql);
		checkClosed();
		try {
			String sqlFixed = translate(sql);
			CallableStatement s = realConnection.prepareCall(sqlFixed);
			registerStatement(s);
			return s;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), sql);
		checkClosed();
		try {

			String sqlFixed = translate(sql);
			// if (isJdbcOdbcBug())
			// sqlFixed = fixJdbcOdbcCharToByte(sqlFixed);
			CallableStatement s = realConnection.prepareCall(sqlFixed);
			registerStatement(s);
			return s;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		checkClosed();
		CrossDBPreparedStatement s = null;
		SQLStruct ss = translate(sql, true);
		s = new CrossDBPreparedStatement(realConnection.prepareStatement(ss.getSql()), this, ss.getSql(), dbType, true,
				false, dataSource, ss.isAddTs());
		convertLang(s);
		s.setDbType(dbType);
		registerStatement(s);
		return s;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		checkClosed();
		CrossDBPreparedStatement s = null;
		SQLStruct ss = translate(sql, true);
		s = new CrossDBPreparedStatement(realConnection.prepareStatement(ss.getSql(), resultSetType,
				resultSetConcurrency), this, ss.getSql(), dbType, true, false, dataSource, ss.isAddTs());
		convertLang(s);
		s.setDbType(dbType);
		registerStatement(s);
		// if (Trace.isEnabled())
		// Trace.traceResult("Create：" + s.getId());
		return s;

	}

	protected void registerStatement(Statement s) {
		statements.addElement(s);
		return;
	}

	public void rollback() throws SQLException {
		checkClosed();
		// if (Trace.isEnabled())
		// Trace.trace(getId());
		try {
			realConnection.rollback();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.trace(getId(), "" + autoCommit);
			realConnection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void setCatalog(String arg0) throws SQLException {
		try {
			realConnection.setCatalog(arg0);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public void setReadOnly(boolean arg0) throws SQLException {
		try {
			realConnection.setReadOnly(arg0);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * 本方法用于将SQL转换器设为无效状态，这样如果应用程序 不需要转换时可以在这里进行设置。 创建日期：(00-9-17 18:14:46)
	 * 
	 * @param b
	 *            boolean
	 */
	public void setSqlTrans(boolean b) {
		translator.setTransFlag(b);
	}

	public boolean isSQLTrans() {
		return translator.getTransFlag();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.trace(getId(), level);
			realConnection.setTransactionIsolation(level);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/**
	 * 
	 */
	public String toString() {
		return (realConnection == null) ? "connection is closed" : realConnection.toString() + ":" + id + ":"
				+ dataSource;
	}

	public void enableSQLTranslator(boolean newBEnableSQLTranslator) {
		enableTranslator = newBEnableSQLTranslator;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-13 18:42:38)
	 * 
	 * @return nc.bs.mw.sqltrans.SqlTranslator
	 */
	public SqlTranslator getSqlTranslator() {
		return translator;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-4 10:13:46)
	 * 
	 * @return boolean
	 */
	public boolean isAddTimeStamp() {
		return addTimeStamp;
	}

	public boolean isJdbcOdbcBug() {

		return jdbcOdbcBug;
	}

	/**
	 * 对sql server2000下对JdbcOdbcDriver的特殊处理。 创建日期：(2005-4-14 9:22:20)
	 * 
	 * @return boolean
	 */
	public boolean isJdbcOdbc() {
		return jdbcOdbc;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-1-30 13:22:20)
	 * 
	 * @return boolean
	 */
	public boolean isSQLTranslatorEnabled() {
		return enableTranslator;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-4 10:13:46)
	 * 
	 * @param newAddTimeStamp
	 *            boolean
	 */
	public void setAddTimeStamp(boolean newAddTimeStamp) {
		addTimeStamp = newAddTimeStamp;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 20:35:10)
	 * 
	 * @return boolean
	 */
	public boolean supportBatchOperate() {
		return batchSupported;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-6-26 14:22:30)
	 * 
	 * @param i
	 *            int
	 */
	public void setDatabaseType(int i) {
		dbType = i;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		checkClosed();
		try {
			return realConnection.getHoldability();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int holdability) throws SQLException {
		try {
			realConnection.setHoldability(holdability);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#setSavepoint()
	 */
	public Savepoint setSavepoint() throws SQLException {
		try {
			return realConnection.setSavepoint();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {

		try {
			realConnection.releaseSavepoint(savepoint);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}

	}

	/*
	 * 
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.traceQuote(getId(), savepoint + "");
			realConnection.rollback(savepoint);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	/*
	 * 
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		try {
			CrossDBStatement stat = new CrossDBStatement(realConnection.createStatement(resultSetType,
					resultSetConcurrency, resultSetHoldability), this, dbType, batchSupported, jdbcOdbcBug, dataSource);
			convertLang(stat);
			registerStatement(stat);
			return stat;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), sql);
		checkClosed();
		try {
			String sqlFixed = translate(sql);
			CallableStatement s = realConnection.prepareCall(sqlFixed);
			registerStatement(s);
			return s;
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.trace(getId(), sql + "," + autoGeneratedKeys);
		throw new UnsupportedOperationException();
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {

		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), sql);
		checkClosed();
		CrossDBPreparedStatement s = null;
		SQLStruct ss = translate(sql, true);
		s = new CrossDBPreparedStatement(realConnection.prepareStatement(ss.getSql(), resultSetType,
				resultSetConcurrency, resultSetHoldability), this, ss.getSql(), dbType, true, false, dataSource,
				ss.isAddTs());
		convertLang(s);
		s.setDbType(dbType);
		registerStatement(s);
		// if (Trace.isEnabled())
		// Trace.traceResult("create:" + s.getId());
		return s;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.trace(getId());
		// throw ExceptionFactory.getUnsupportedException();
		throw new UnsupportedOperationException();

	}

	public Savepoint setSavepoint(String name) throws SQLException {
		try {
			// if (Trace.isEnabled())
			// Trace.traceQuote(getId(), name);
			return realConnection.setSavepoint(name);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		// if (Trace.isEnabled())
		// Trace.trace(getId(), sql + "," + columnNames[0]);
		// throw ExceptionFactory.getUnsupportedException();
		throw new UnsupportedOperationException();
	}

	/**
	 * 得到数据库类型
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getDatabaseType() throws SQLException {
		return dbType;
	}

	/**
	 * @return
	 */
	public String getDbcode() {
		return null;
	}

	/**
	 * 翻译SQL以适配多种数据库
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected String translate(String sql) throws SQLException {
		return translate(sql, false).getSql();
	}

	/**
	 * 翻译SQL以适配多种数据库
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected SQLStruct translate(String sql, boolean ptmtTs) throws SQLException {
//		ThreadTracer.getInstance().updateEvent("begin translate:" + sql);
		try {
//			if (Trace.isEnabled()) {
//				Trace.traceSQL("Database Type 0d 1o 2s:" + dbType);
//				Trace.traceSQL("Original SQL :" + sql);
//			}
			if (sql == null)
				return null;
			if (!isSQLTranslatorEnabled()) {
				if (isAddTimeStamp())
					return stampSQL(sql, ptmtTs);
				return new SQLStruct(sql, false);
			}
			if (sql.length() > 8000) {
				String sqlFixed = translator.getSql(sql);
				if (isAddTimeStamp())
					return stampSQL(sqlFixed, ptmtTs);
				return new SQLStruct(sqlFixed, false);
			}
//			String key = sql;
//			String result = (String) cache.getPreparedSQL(key);
//			if (result != null) {
//				SQLStruct s = null;
//				if (isAddTimeStamp()) {
//					s = stampSQL(result, ptmtTs);
//				} else {
//					s = new SQLStruct(result, false);
//				}
//				if (Trace.isEnabled())
//					Trace.traceSQL("Translated Prepared SQL :" + s.getSql());
//				return s;
//			}

			// if (UG_CONVERT)
			// sql = unicodeFromNoUC(sql);
			sql = translator.getSql(sql);
			// if (isJdbcOdbcBug())
			// sql = fixJdbcOdbcCharToByte(sql);
//			cache.putPreparedSQL(key, sql);
			SQLStruct s = null;
			if (isAddTimeStamp()) {
				s = stampSQL(sql, ptmtTs);
			} else {
				s = new SQLStruct(sql, false);
			}
//			if (Trace.isEnabled())
//				Trace.traceSQL("Translated Prepared  SQL :" + s.getSql());
			return s;
		} finally {
//			ThreadTracer.getInstance().updateEvent("end translate");
		}
	}

	/**
	 * 关闭所有Statement
	 */
	private void closeStatements() {
		Object[] ss;
		synchronized (statements) {
			ss = new Object[statements.size()];
			statements.copyInto(ss);
		}
		for (int i = 0; i < ss.length; i++) {
			if (ss[i] == null || !(ss[i] instanceof CrossDBStatement)) {
			} else {
				try {
					((CrossDBStatement) ss[i]).close();
				} catch (SQLException e) {
				}
			}
		}
		statements = new Vector<Statement>();
	}

	/**
	 * 检查连接是否关闭
	 * 
	 * @throws SQLException
	 *             如果关闭抛出异常
	 */
	private void checkClosed() throws SQLException {
		if (closed)
			throw new SQLException("Connection has been closed!!");
	}

	/**
	 * @param s
	 */
	protected void deregisterStatement(CrossDBStatement s) {
		if (s == null) {
			return;
		}
		if (!statements.removeElement(s)) {
			return;
		}
	}

	/**
	 * @return
	 */

	public String getId() {
		return "Connection(" + id + ")";
	}

	protected Adapter getAdapter() {
		return adapter;
	}

	public Connection getPConnection() {
		return realConnection;
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		try {
			return realConnection.createArrayOf(typeName, elements);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public Blob createBlob() throws SQLException {
		try {
			return realConnection.createBlob();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public Clob createClob() throws SQLException {
		try {
			return realConnection.createClob();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public NClob createNClob() throws SQLException {
		try {
			return realConnection.createNClob();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		try {
			return realConnection.createSQLXML();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		try {
			return realConnection.createStruct(typeName, attributes);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		try {
			return realConnection.getClientInfo();
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		try {
			return realConnection.getClientInfo(name);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		try {
			return realConnection.isValid(timeout);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		realConnection.setClientInfo(properties);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {

		realConnection.setClientInfo(name, value);

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		try {
			realConnection.setTypeMap(map);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		try {
			return realConnection.isWrapperFor(iface);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		try {
			return realConnection.unwrap(iface);
		} catch (SQLException e) {
			throw translator.getSqlException(e);
		}
	}

	public String getDataSource() {
		return dataSource;
	}

}