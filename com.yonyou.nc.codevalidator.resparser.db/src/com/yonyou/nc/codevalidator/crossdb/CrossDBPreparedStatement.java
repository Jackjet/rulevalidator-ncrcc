package com.yonyou.nc.codevalidator.crossdb;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class CrossDBPreparedStatement extends CrossDBStatement implements PreparedStatement {
	int id = counter++;

	private ArrayList<Object> parameterValues;

	private String sqlTemplate;

	private boolean addTs = false;

	public CrossDBPreparedStatement(java.sql.PreparedStatement dummy, CrossDBConnection con, String sql, int dbtype,
			boolean batchSupport, boolean odbcBug, String dataSource, boolean addTs) {
		super(dummy, con, dbtype, batchSupport, odbcBug, dataSource);
		sqlTemplate = sql;
		parameterValues = new ArrayList<Object>();
		this.addTs = addTs;

	}

	public CrossDBPreparedStatement(java.sql.Statement dummy, CrossDBConnection con, String sql, int dbtype,
			boolean batchSupport, boolean odbcBug, String dataSource, boolean addTs) {
		super(dummy, con, dbtype, batchSupport, odbcBug, dataSource);
		sqlTemplate = sql;
		parameterValues = new ArrayList<Object>();
		this.addTs = addTs;
	}

	public void addBatch() throws SQLException {
		// try {
		addTsParam();
		// if (Trace.isEnabled())
		// Trace.traceQuote(getId(), getSQLString());
		((java.sql.PreparedStatement) dummy).addBatch();
		parameterValues.clear();
		// } catch (SQLException e) {
		// Trace.traceException(null, e);
		// throw trans.getSqlException(e);
		// }

	}

	public void clearParameters() throws SQLException {
		// try {
		((java.sql.PreparedStatement) dummy).clearParameters();
		parameterValues.clear();
		// } catch (SQLException e) {
		// throw trans.getSqlException(e);
		// }
	}

	public boolean execute() throws SQLException {
		long beforeTime = System.currentTimeMillis();
		addTsParam();
//		String sqlString = getSQLString();
		// getCurrentSqlHistory().setLastSql("batch:" + sqlString);
		// TODO:NEED HGY AUDIT.
		// ThreadTracer.getInstance().addNewSql(sqlString, con.id,
		// getDataSource());
		try {
			return ((PreparedStatement) dummy).execute();
		} catch (SQLException e) {
			// Trace.traceException(getSQLString(), e);
			// throw trans.getSqlException(e);
			throw e;
		} finally {
			// if (Trace.isEnabled())
			// Trace.traceQuote(getId(), getSQLString(), beforeTime);
			parameterValues.clear();
			this.afterExecuteSql(sqlTemplate, beforeTime);

		}

	}

	public ResultSet executeQuery() throws SQLException {
		long beforeTime = System.currentTimeMillis();
		// TODO:NEED HGY AUDIT.
//		String sqlString = getSQLString();
		// ThreadTracer.getInstance().addNewSql(sqlString, con.id,
		// getDataSource());
		// getCurrentSqlHistory().setLastSql("batch:" + sqlString);

		try {

			CrossDBResultSet r = new CrossDBResultSet(((java.sql.PreparedStatement) dummy).executeQuery(), this);
			r.setMaxRows(maxRows);
			registerResultSet(r);
			return r;
		} catch (SQLException e) {
			// Trace.traceException(getSQLString(), e);
			// throw trans.getSqlException(e);
			throw e;
		} finally {
			// if (Trace.isEnabled())
			// Trace.traceQuote(getId(), getSQLString(), beforeTime);
			parameterValues.clear();
			this.afterExecuteSql(sqlTemplate, beforeTime);

		}

	}

	public int executeUpdate() throws SQLException {
		long beforeTime = System.currentTimeMillis();
		// TODO:NEED HGY AUDIT.
		addTsParam();
//		String sqlString = getSQLString();
		// ThreadTracer.getInstance().addNewSql(sqlString, con.id,
		// getDataSource());
		// getCurrentSqlHistory().setLastSql(sqlString);
		int ret = 0;

		try {
			ret = ((java.sql.PreparedStatement) dummy).executeUpdate();
			return ret;
		} catch (SQLException e) {
			// Trace.traceException(getSQLString(), e);
			// throw trans.getSqlException(e);
			throw e;
		} finally {
			// if (Trace.isEnabled()) {
			// Trace.traceQuote(getId(), getSQLString(), beforeTime);
			// Trace.traceSQL("Update rows= " + ret);
			// parameterValues.clear();
			// }
			this.afterExecuteSql(sqlTemplate, beforeTime);
		}

	}

	public java.sql.ResultSetMetaData getMetaData() throws SQLException {
		return ((java.sql.PreparedStatement) dummy).getMetaData();
	}

	public void setArray(int i, java.sql.Array x) throws SQLException {
		i = reSetIdx(i);
		try {
			saveQueryParamValue(i, x);
			((java.sql.PreparedStatement) dummy).setArray(i, x);
		} catch (SQLException e) {
			logSetError(i, x, e);
		}
	}

	public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, Types.CLOB);
			} else {
				parameterIndex = reSetIdx(parameterIndex);
				saveQueryParamValue(parameterIndex, x);
				adapter.setAsciiStream(this, parameterIndex, x, length);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		saveQueryParamValue(parameterIndex, x);
		((java.sql.PreparedStatement) dummy).setBigDecimal(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
		try {

			if (x == null) {
				setNull(parameterIndex, Types.BINARY);
			} else {
				parameterIndex = reSetIdx(parameterIndex);
				saveQueryParamValue(parameterIndex, x);
				adapter.setBinaryStream(this, parameterIndex, x, length);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setBlob(int i, java.sql.Blob x) throws SQLException {
		i = reSetIdx(i);
		saveQueryParamValue(i, x);

		throw new UnsupportedOperationException();
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		saveQueryParamValue(parameterIndex, new Boolean(x));
		try {
			adapter.setBoolean(this, parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		saveQueryParamValue(parameterIndex, Integer.valueOf(x));
		try {

			((java.sql.PreparedStatement) dummy).setByte(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, Types.BINARY);
			} else {
				parameterIndex = reSetIdx(parameterIndex);
				saveQueryParamValue(parameterIndex, x);
				adapter.setBytes(this, parameterIndex, x);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) throws SQLException {
		try {

			if (reader == null) {
				setNull(parameterIndex, Types.CLOB);
			} else {
				parameterIndex = reSetIdx(parameterIndex);
				saveQueryParamValue(parameterIndex, reader);
				adapter.setCharacterStream(this, parameterIndex, reader, length);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, reader, e);
			throw trans.getSqlException(e);
		}
	}

	public void setClob(int i, java.sql.Clob x) throws SQLException {
		i = reSetIdx(i);
		saveQueryParamValue(i, x);

		throw new UnsupportedOperationException();
	}

	public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		saveQueryParamValue(parameterIndex, x);

		((java.sql.PreparedStatement) dummy).setDate((parameterIndex), x);
	}

	public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar calendar) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		saveQueryParamValue(parameterIndex, x);
		try {
			if (x != null) {

				calendar = (Calendar) calendar.clone();
				calendar.setTime(x);
				Calendar local = Calendar.getInstance();
				convertTime(calendar, local);
				x = new java.sql.Date(local.getTime().getTime());
			}
			((java.sql.PreparedStatement) dummy).setDate(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setDBEncoding(String encoding) {
	}

	public void setDouble(int arg0, double arg1) throws SQLException {
		arg0 = reSetIdx(arg0);
		try {
			saveQueryParamValue(arg0, new Double(arg1));
			((java.sql.PreparedStatement) dummy).setDouble(arg0, arg1);
		} catch (SQLException e) {
			logSetError(arg0, arg1, e);
			throw trans.getSqlException(e);
		}
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, new Float(x));

			((java.sql.PreparedStatement) dummy).setFloat(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, Integer.valueOf(x));

			((java.sql.PreparedStatement) dummy).setInt(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, Long.valueOf(x));

			((java.sql.PreparedStatement) dummy).setLong(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, null);

			adapter.setNull(this, parameterIndex, sqlType);
		} catch (SQLException e) {
			logSetError(parameterIndex, null, e);
			throw trans.getSqlException(e);
		}

	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		// paramIndex = reSetIdx(paramIndex);
		// saveQueryParamValue(paramIndex, null);

		setNull(paramIndex, sqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, x);

			if (x == null) {
				throw new SQLException("x is null");
			}
			if (x != null && x instanceof String) {
				setString(parameterIndex, (String) x);
			} else {
				((java.sql.PreparedStatement) dummy).setObject(parameterIndex, x);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}

	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		try {
			x = convertObject(x, targetSqlType);
			if (x == null) {
				setNull(parameterIndex, targetSqlType);
			} else {
				parameterIndex = reSetIdx(parameterIndex);
				saveQueryParamValue(parameterIndex, x);
				((java.sql.PreparedStatement) dummy).setObject(parameterIndex, x, targetSqlType);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		try {
			x = convertObject(x, targetSqlType);
			if (x == null) {
				setNull(parameterIndex, targetSqlType);
			} else {
				parameterIndex = reSetIdx(parameterIndex);
				saveQueryParamValue(parameterIndex, x);
				((java.sql.PreparedStatement) dummy).setObject(parameterIndex, x, targetSqlType, scale);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setRef(int i, java.sql.Ref x) throws SQLException {
		i = reSetIdx(i);
		try {
			saveQueryParamValue(i, x);
			((java.sql.PreparedStatement) dummy).setRef(i, x);
		} catch (SQLException e) {
			logSetError(i, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, Short.valueOf(x));

			((java.sql.PreparedStatement) dummy).setShort(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, java.sql.Types.VARCHAR);
				return;
			}
			parameterIndex = reSetIdx(parameterIndex);
			saveQueryParamValue(parameterIndex, x);
			adapter.setString(this, parameterIndex, x);

		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setTime(int parameterIndex, java.sql.Time x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, java.sql.Types.TIME);
				return;
			}
			parameterIndex = reSetIdx(parameterIndex);
			saveQueryParamValue(parameterIndex, x);
			((java.sql.PreparedStatement) dummy).setTime(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar calendar) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		saveQueryParamValue(parameterIndex, x);

		if (x != null) {
			calendar = (Calendar) calendar.clone();
			calendar.setTime(x);
			Calendar local = Calendar.getInstance();
			convertTime(calendar, local);
			x = new java.sql.Time(local.getTime().getTime());
		}
		try {
			((java.sql.PreparedStatement) dummy).setTime(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw e;
		}
	}

	public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws SQLException {
		try {

			if (x == null) {
				setNull(parameterIndex, java.sql.Types.TIMESTAMP);
				return;
			}
			parameterIndex = reSetIdx(parameterIndex);
			saveQueryParamValue(parameterIndex, x);
			((java.sql.PreparedStatement) dummy).setTimestamp(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar calendar) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, x);
			if (x != null) {
				calendar = (Calendar) calendar.clone();
				calendar.setTime(x);
				Calendar local = Calendar.getInstance();
				convertTime(calendar, local);
				x = new java.sql.Timestamp(local.getTime().getTime());
			}
			((java.sql.PreparedStatement) dummy).setTimestamp(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}
	}

	public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int arg2) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		saveQueryParamValue(parameterIndex, x);

		// if (Trace.isEnabled())
		// Trace.trace(getId(), Trace.quoteObject(x));
		throw new UnsupportedOperationException();
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		parameterIndex = reSetIdx(parameterIndex);
		try {
			saveQueryParamValue(parameterIndex, x);
			((java.sql.PreparedStatement) dummy).setURL(parameterIndex, x);
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}

	}

	private void logSetError(int index, Object value, Throwable thr) {
		// if (Logger.isErrorEnabled()) {
		StringBuffer sb = new StringBuffer();
		if (sqlTemplate != null) {
			sb.append('<').append(this.sqlTemplate).append('>');
		}

		sb.append("set parameter error,parameter index=").append(index).append(" parameter value=").append(value);
		Logger.error(sb.toString(), thr);
		// }
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return ((java.sql.PreparedStatement) dummy).getParameterMetaData();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		return dummy.getResultSetHoldability();

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	String getId() {
		return "PreparedStatement(" + con.id + "_" + id + ")";
	}

	private Object convertObject(Object x, int targetSqlType) {
		if (x == null) {
			return null;
		}
		int originalSqlType;
		if (x instanceof String) {
			originalSqlType = Types.VARCHAR;
		} else if (x instanceof java.math.BigDecimal) {
			originalSqlType = Types.DECIMAL;
		} else if (x instanceof Boolean) {
			originalSqlType = Types.BIT;
		} else if (x instanceof Integer) {
			originalSqlType = Types.INTEGER;
		} else if (x instanceof Long) {
			originalSqlType = Types.BIGINT;
		} else if (x instanceof Float) {
			originalSqlType = Types.REAL;
		} else if (x instanceof Double) {
			originalSqlType = Types.DOUBLE;
		} else if (x instanceof byte[]) {
			originalSqlType = Types.BINARY;
		} else if (x instanceof java.sql.Date) {
			originalSqlType = Types.DATE;
		} else if (x instanceof java.sql.Time) {
			originalSqlType = Types.TIME;
		} else if (x instanceof java.sql.Timestamp) {
			originalSqlType = Types.TIMESTAMP;
		} else {
			return x;
		}
		if (originalSqlType == targetSqlType) {
			return x;
		}
		switch (targetSqlType) {
		case Types.VARCHAR:
			return x.toString();
		case Types.DECIMAL:
			return new java.math.BigDecimal(x.toString());
		case Types.BIT:
			return Boolean.valueOf(x.toString());
		case Types.INTEGER:
			return Integer.valueOf(x.toString());
		case Types.BIGINT:
			return Long.valueOf(x.toString());
		case Types.REAL:
			return Float.valueOf(x.toString());
		case Types.DOUBLE:
			return Double.valueOf(x.toString());
		case Types.BINARY:
			return x;
		case Types.DATE:
			return x;
		case Types.TIME:
			return x;
		case Types.TIMESTAMP:
			return x;
		default:
			return x;
		}
	}

	private void convertTime(Calendar from, Calendar to) {
		to.set(Calendar.YEAR, from.get(Calendar.YEAR));
		to.set(Calendar.MONTH, from.get(Calendar.MONTH));
		to.set(Calendar.DAY_OF_MONTH, from.get(Calendar.DAY_OF_MONTH));
		to.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY));
		to.set(Calendar.MINUTE, from.get(Calendar.MINUTE));
		to.set(Calendar.SECOND, from.get(Calendar.SECOND));
		to.set(Calendar.MILLISECOND, from.get(Calendar.MILLISECOND));
	}

	private void saveQueryParamValue(int position, Object obj) {
//		if (Trace.isEnabled()) {
			Object strValue;
			if (obj instanceof String || obj instanceof Date) {
				strValue = "'" + obj + "'";
			} else {
				if (obj == null) {
					strValue = "null";
				} else {
					strValue = obj;
				}
			}
			while (position >= parameterValues.size()) {
				parameterValues.add(null);
			}
			parameterValues.set(position, strValue);
//		}
	}

	public String getSQLString() {
		StringBuffer buf = new StringBuffer();
		int qMarkCount = 0;
		StringTokenizer tok = new StringTokenizer(sqlTemplate + " ", "?");
		while (tok.hasMoreTokens()) {
			String oneChunk = tok.nextToken();
			buf.append(oneChunk);
			try {
				Object value;
				if (parameterValues.size() > 1 + qMarkCount) {
					value = parameterValues.get(1 + qMarkCount++);
				} else {
					if (tok.hasMoreTokens()) {
						value = null;
					} else {
						value = "";
					}
				}
				buf.append("" + value);
			} catch (Throwable e) {
				buf.append("ERROR WHEN PRODUCING QUERY STRING FOR LOG." + e.toString());
			}
		}
		return buf.toString().trim();
	}

	public int[] executeBatch() throws SQLException {
		int[] result = null;
		long beforeTime = System.currentTimeMillis();
		// TODO:NEED HGY TO AUDIT.
//		String sqlString = getSQLString();
		// getCurrentSqlHistory().setLastSql(sqlString);
//		ThreadTracer.getInstance().addNewSql(sqlString, con.id, getDataSource());
		try {
			result = dummy.executeBatch();
			return result;
		} catch (SQLException e) {
//			Trace.traceException(sqlTemplate, e);
			throw trans.getSqlException(e);
		} finally {
//			if (Trace.isEnabled()) {
//				Trace.trace(getId(), beforeTime);
//				Trace.traceSQL("Batch result " + Trace.quote(result));
//			}
			afterExecuteSql(sqlTemplate, beforeTime);
		}
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		try {
			if (x == null) {
				setNull(parameterIndex, Types.BINARY);
			} else {
				parameterIndex = reSetIdx(parameterIndex);
				saveQueryParamValue(parameterIndex, x);
				adapter.setBinaryStream(this, parameterIndex, x, -1);
			}
		} catch (SQLException e) {
			logSetError(parameterIndex, x, e);
			throw trans.getSqlException(e);
		}

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * 添加ts参数
	 * 
	 * @throws SQLException
	 */
	private void addTsParam() throws SQLException {
		// TODO NEED AUDIT
		if (addTs) {
			int tsIdx = 1;
			String ts = CrossDBObject.getTimeStampString();
			try {
				saveQueryParamValue(tsIdx, ts);
				if (ts == null) {
					setNull(tsIdx, java.sql.Types.VARCHAR);
					return;
				}
				adapter.setString(this, tsIdx, ts);
			} catch (SQLException e) {
				logSetError(tsIdx, ts, e);
				throw trans.getSqlException(e);
			}
		}
	}

	/**
	 * 重置参数添加位置，如需TS，则后移一位
	 * 
	 * @param i
	 * @return
	 */
	private int reSetIdx(int i) {
		// TODO NEED AUDIT
		if (addTs) {
			i++;
		}
		return i;
	}

}