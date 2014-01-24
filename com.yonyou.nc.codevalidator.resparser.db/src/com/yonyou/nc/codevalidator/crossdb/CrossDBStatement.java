package com.yonyou.nc.codevalidator.crossdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.yonyou.nc.codevalidator.adapter.Adapter;
import com.yonyou.nc.codevalidator.sqltrans.SqlTranslator;
import com.yonyou.nc.codevalidator.type.DBConsts;

/**
 * @nopublish CrossDBStatement 创建日期：(2000-6-1 14:07:43) created by hey in 2005.5
 */

public class CrossDBStatement extends CrossDBObject implements Statement,
		DBConsts {
	// 记数器
	static int counter;

	int id = counter++;

	protected Statement dummy;

	protected CrossDBConnection con;

	protected Vector<CrossDBResultSet> resultsets = new Vector<CrossDBResultSet>();

	SqlTranslator trans = null;

	// JdbcOdbcStatement是否支持数据库批处理*
	private boolean batchSupported = true;

	/* 是否启动SQL翻译器 */
	private boolean sqlTranslator = true;

	// 是否需要对sql server2000下JdbcOdbcDriver的特殊处理
	private boolean jdbcOdbc = false;

	// 是否需要修正JDBCOdcb桥的双字节处理的Bug*
	private boolean jdbcOdbcBug = false;

	// 数据库类型
	int dbType = -1;

	//
	protected Adapter adapter = null;

	//
//	private LRUCache cache;

	private String dataSource = null;

	private StringBuffer batchSql = new StringBuffer();

	int maxRows;

	/**
	 * UFStatement1 构造子注解。
	 */
	public CrossDBStatement() {
		super();
	}

	/**
	 * 
	 * @param dummy
	 * @param con
	 * @param dbType
	 * @param batchSupport
	 * @param odbcBug
	 * @param cache
	 */
	public CrossDBStatement(java.sql.Statement dummy, CrossDBConnection con,
			int dbType, boolean batchSupport, boolean odbcBug, 
			String dataSource) {
		this();
		this.dataSource = dataSource;
		this.con = con;
//		this.cache = cache;
		this.adapter = con.getAdapter();
		this.dummy = dummy;
		this.dbType = dbType;
		this.trans = con.getSqlTranslator();
		this.batchSupported = batchSupport;
		this.jdbcOdbc = odbcBug;
		sqlTranslator = con.isSQLTranslatorEnabled();
	}

	public void supportHugeData() throws SQLException {
		adapter.supportHugeData(this);
	}

	/**
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void addBatch(String sql) throws SQLException {
//		if (Trace.isEnabled())
//			Trace.traceQuote(getId(), sql);
		// if (con.isAddTimeStamp())
		// sql = this.stampSQL(sql);
		try {
			String sqlFixed = translate(sql, true);
			dummy.addBatch(sqlFixed);
			batchSql.append(sqlFixed).append("; ");
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void cancel() throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			// 适配cancel
			adapter.cancel(dummy);
			// rs.cancel();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void clearBatch() throws SQLException {

		try {
			dummy.clearBatch();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void clearWarnings() throws SQLException {
		try {
			dummy.clearWarnings();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	synchronized public void close() throws SQLException {
		try {
			closeResultSets();
			dummy.close();
			con.deregisterStatement(this);
//			if (Trace.isEnabled())
//				Trace.traceResult("Close:" + getId());
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void closeResultSets() {
		Object[] rs;
		synchronized (resultsets) {
			rs = new Object[resultsets.size()];
			resultsets.copyInto(rs);
		}
		for (int i = 0; i < rs.length; i++) {
			if (rs[i] == null || !(rs[i] instanceof CrossDBResultSet)) {
			} else {
				try {
					((CrossDBResultSet) rs[i]).close();
				} catch (SQLException e) {
				}
			}
		}
		resultsets = new Vector<CrossDBResultSet>();
	}

	protected void deregisterResultSet(CrossDBResultSet r) {
		if (r == null) {
			return;
		}
		if (!resultsets.removeElement(r)) {
			return;
		}
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(String sql) throws SQLException {
		long beforeTime = System.currentTimeMillis();
		// TODO:NEED HGY AUDIT.
		String fixedSQL = translate(sql, true);
//		ThreadTracer.getInstance().addNewSql(fixedSQL, con.id, dataSource);
//		getCurrentSqlHistory().setLastSql(fixedSQL);
		try {
			return dummy.execute(fixedSQL);
		} catch (SQLException e) {
//			Trace.traceException(fixedSQL, e);
			throw trans.getSqlException(e);
		} finally {
//			if (Trace.isEnabled())
//				Trace.traceQuote(getId(), fixedSQL, beforeTime);
			afterExecuteSql(fixedSQL, beforeTime);
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public int[] executeBatch() throws SQLException {
		int[] result = null;
		long beforeTime = System.currentTimeMillis();
		// TODO:NEED HGY AUDIT.
//		ThreadTracer.getInstance().addNewSql(batchSql.toString(), con.id,
//				dataSource);
//		getCurrentSqlHistory().setLastSql(batchSql.toString());

		try {
			result = dummy.executeBatch();
			return result;
		} catch (SQLException e) {
//			Trace.traceException(batchSql.toString(), e);
			throw trans.getSqlException(e);
		} finally {
//			if (Trace.isEnabled()) {
//				Trace.trace(getId(), beforeTime);
//				Trace.traceSQL("Batch result " + Trace.quote(result));
//			}
			afterExecuteSql(batchSql.toString(), beforeTime);
			this.batchSql = new StringBuffer();
		}
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		long beforeTime = System.currentTimeMillis();
		String sqlFixed = translate(sql, true);
		// TODO:NEED HGY AUDIT.
//		ThreadTracer.getInstance().addNewSql(sqlFixed, con.id, dataSource);
//		getCurrentSqlHistory().setLastSql(sqlFixed);

		try {
			CrossDBResultSet r = new CrossDBResultSet(
					dummy.executeQuery(sqlFixed), this);
			r.setMaxRows(maxRows);
			registerResultSet(r);
//			if (Trace.isEnabled())
//				Trace.traceSQL("create " + r.getId());
			return r;
		} catch (SQLException e) {
//			Trace.traceException(sqlFixed, e);
			throw trans.getSqlException(e);
		} finally {
//			if (Trace.isEnabled())
//				Trace.traceQuote(getId(), sqlFixed, beforeTime);

			afterExecuteSql(sqlFixed, beforeTime);

		}
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException {
		long beforeTime = System.currentTimeMillis();
		String sqlFixed = translate(sql, true);
		try {
//			getCurrentSqlHistory().setLastSql(sqlFixed);
			// TODO:NEED HGY AUDIT.
//			ThreadTracer.getInstance().addNewSql(sqlFixed, con.id, dataSource);

			int result = dummy.executeUpdate(sqlFixed);
//			if (Trace.isEnabled())
//				Trace.traceQuote(getId(), sqlFixed, beforeTime);
//			if (Trace.isEnabled())
//				Trace.traceSQL("Update rows= " + result);
			return result;
		} catch (SQLException e) {
//			Trace.traceException(sqlFixed, e);
			throw trans.getSqlException(e);
		} finally {
			afterExecuteSql(sqlFixed, beforeTime);
		}
	}

	public java.sql.Connection getConnection() throws SQLException {
		return con;
	}

	public int getDbType() {
		return dbType;
	}

	public int getFetchDirection() throws SQLException {
		try {
			return dummy.getFetchDirection();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getFetchSize() throws SQLException {
		try {
			return dummy.getFetchSize();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getMaxFieldSize() throws SQLException {
		try {
			return dummy.getMaxFieldSize();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getMaxRows() throws SQLException {
		try {
			return dummy.getMaxRows();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean getMoreResults() throws SQLException {
		try {
			return dummy.getMoreResults();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getQueryTimeout() throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			return dummy.getQueryTimeout();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public ResultSet getResultSet() throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			CrossDBResultSet rs = new CrossDBResultSet(dummy.getResultSet(),
					this);
			rs.setMaxRows(maxRows);
//			if (Trace.isEnabled())
//				Trace.traceResult(rs.getId());
			return rs;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/**
	 * JDBC 2.0
	 * <p/>
	 * Retrieves the result set concurrency.
	 */
	public int getResultSetConcurrency() throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			return dummy.getResultSetConcurrency();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/**
	 * JDBC 2.0
	 * <p/>
	 * Determine the result set type.
	 */
	public int getResultSetType() throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			return dummy.getResultSetType();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public SqlTranslator getSqlTranslator() {
		return trans;
	}

	public int getUpdateCount() throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			return dummy.getUpdateCount();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			return dummy.getWarnings();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
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
	 * 此处插入方法说明。 创建日期：(2002-1-30 13:27:44)
	 * 
	 * @return boolean
	 */
	protected boolean isSQLTranslatorEnabled() {
		return sqlTranslator;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-10-20 17:05:57)
	 * 
	 * @param r
	 */
	protected void registerResultSet(CrossDBResultSet r) {
		resultsets.addElement(r);
	}

	public void setCursorName(String name) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setDbType(int newDbType) {
		dbType = newDbType;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-8-29 20:35:10)
	 * 
	 * @return boolean
	 */
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			dummy.setEscapeProcessing(arg0);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setFetchDirection(int direction) throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId());
			dummy.setFetchDirection(direction);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setFetchSize(int rows) throws SQLException {
		try {
			// TODO:NEED HGY TO AUDIT.
//			if (Trace.isEnabled())
//				Trace.trace(getId(), "setfetchsize=" + rows);
			dummy.setFetchSize(rows);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setMaxFieldSize(int size) throws SQLException {
		try {
			// TODO:NEED HGY TO AUDIT
//			if (Trace.isEnabled())
//				Trace.trace(getId(), "setmaxfieldsize=" + size);
			dummy.setMaxFieldSize(size);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setMaxRows(int rows) throws SQLException {
		try {
			if (rows < 0) {
				rows = 0;
			}
//			// TODO:NEED HGY TO AUDIT.
//			if (Trace.isEnabled())
//				Trace.trace(getId(), "setmaxrows=" + rows);
			dummy.setMaxRows(rows);
			maxRows = rows;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setQueryTimeout(int time) throws SQLException {
		try {
			// TODO:NEED HGY TO AUDIT.
//			if (Trace.isEnabled())
//				Trace.trace(getId(), "set querytimeout" + time);
			dummy.setQueryTimeout(time);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean supportBatchOperate() {
		return batchSupported;
	}

	/**
	 * 此处插入方法描述。 创建日期：(2002-7-23 8:38:50)
	 * 
	 * @param sql
	 *            java.lang.String
	 * @return java.lang.String
	 */

	protected String translate(String sql, boolean addTimeStamp)
			throws SQLException {
//		ThreadTracer.getInstance().updateEvent("begin translate:" + sql);
		try {
//			if (Trace.isEnabled()) {
//				Trace.traceSQL("Database Type 0d 1o 2s:" + dbType);
//				Trace.traceSQL("Original SQL :" + sql);
//			}

			if (!isSQLTranslatorEnabled()) {
				if (con.isAddTimeStamp() && addTimeStamp)
					return this.stampSQL(sql);
				return sql;
			}
			if (sql.length() > 5000) {
				String sqlFixed = trans.getSql(sql);
				if (con.isAddTimeStamp() && addTimeStamp)
					sqlFixed = this.stampSQL(sqlFixed);
				return sqlFixed;
			}
//			String key = sql;
//			String result;
//			result = (String) cache.getStatementSQL(key);
//			if (result != null) {
//				if (con.isAddTimeStamp() && addTimeStamp)
//					result = this.stampSQL(result);
//				if (Trace.isEnabled())
//					Trace.traceSQL("Translated SQL :" + result);
//				return result;
//			}
			// if (UG_CONVERT && sql != null)
			// sql = unicodeFromNoUC(sql);
			String sqlFixed = trans.getSql(sql);
			// if (isJdbcOdbcBug())
			// sqlFixed = fixJdbcOdbcCharToByte(sqlFixed);
//			cache.putStatementSQL(key, sqlFixed);
			if (con.isAddTimeStamp() && addTimeStamp)
				sqlFixed = this.stampSQL(sqlFixed);
//			if (Trace.isEnabled())
//				Trace.traceSQL("Translated SQL :" + sql);
			return sqlFixed;
		} finally {
//			ThreadTracer.getInstance().updateEvent("end translate");
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getResultSetHoldability()
	 */

	public int getResultSetHoldability() throws SQLException {
		return dummy.getResultSetHoldability();
		// throw new UnsupportedOperationException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getMoreResults(int)
	 */

	public boolean getMoreResults(int current) throws SQLException {
		return dummy.getMoreResults(current);
		// throw new UnsupportedOperationException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getGeneratedKeys()
	 */

	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String,
	 * java.lang.String[])
	 */

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	String getId() {

		return new StringBuffer().append("Statement(").append(con.id)
				.append("_").append(id).toString();
	}

	public Adapter getAdapter() {
		return adapter;
	}

	public Statement getVendorObject() {
		return dummy;
	}

//	protected SqlHistory getCurrentSqlHistory() {
//		SqlHistory sqlHistory = SqlHistoryMap.getCurrentSqlHistoryMap()
//				.getSqlHistory(con.id);
//		if (sqlHistory == null) {
//			sqlHistory = new SqlHistory();
//			sqlHistory.setDataSource(dataSource);
//			sqlHistory.setConnId(con.id);
//			sqlHistory.setThrowable(new Throwable());
//			sqlHistory.setThreadName(Thread.currentThread().getName());
//			sqlHistory.setClientAddress((String) Logger.getMDC("remoteAddr"));
//			sqlHistory.setClientPort("" + Logger.getMDC("remotePort"));
//			SqlHistoryMap.getCurrentSqlHistoryMap().addSqlHistory(sqlHistory);
//		}
//		return sqlHistory;
//	}

	protected void afterExecuteSql(String sql, long beginTime) {
//		long useTime = System.currentTimeMillis() - beginTime;
//		if (SqlMonitorParam.timeThreshold > 0
//				&& useTime > SqlMonitorParam.timeThreshold) {
//			if (SqlMonitorParam.log.isInfoEnabled()
//					&& (SqlMonitorParam.clientFilter == null || SqlMonitorParam.clientFilter
//							.matcher(
//									""
//											+ getCurrentSqlHistory()
//													.getClientAddress())
//							.matches())) {
//				SqlMonitorParam.log.info("<" + sql + "><" + useTime + ">");
//			}
//		}

//		getCurrentSqlHistory().setLastSql(null);
		// TODO:NEED HGY AUDIT.
//		ThreadTracer.getInstance().endSql();
	}

	@Override
	public boolean isClosed() throws SQLException {
		try {
			return dummy.isClosed();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	@Override
	public boolean isPoolable() throws SQLException {
		try {
			return dummy.isPoolable();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		try {
			dummy.setPoolable(poolable);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		try {
			return dummy.isWrapperFor(iface);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		try {
			return dummy.unwrap(iface);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	// TODO:NEED HGY AUDIT.
	protected String getDataSource() {
		return dataSource;
	}
}