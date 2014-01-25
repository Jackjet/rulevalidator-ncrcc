package com.yonyou.nc.codevalidator.crossdb;

/**
 * @nopublish
 *ResultSet.java
 *  User: 贺扬
 */

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

import com.yonyou.nc.codevalidator.adapter.Adapter;
import com.yonyou.nc.codevalidator.adapter.BaseAdapter;
import com.yonyou.nc.codevalidator.sqltrans.SqlTranslator;
import com.yonyou.nc.codevalidator.type.DBConsts;

public class CrossDBResultSet extends CrossDBObject implements ResultSet, DBConsts {

	private static int MAX_ROW = Integer.parseInt(System.getProperty("nc.resultset.max", "300000"));
	private static int counter;

	private int id = counter++;

	private ResultSet rs;

	private CrossDBStatement stmt;

	private SqlTranslator trans = null;

	private Adapter adapter = null;

	private int nRowCount = 0;

	private int maxRows = 50000;

//	private StringBuffer resultString = new StringBuffer("");

	/**
	 * ABCDEResult 构造子注解。
	 */
	public CrossDBResultSet() {
		super();
	}

	public CrossDBResultSet(java.sql.ResultSet dummy, CrossDBStatement stmt) {
		super();
		this.stmt = stmt;
		this.rs = dummy;
		trans = stmt.getSqlTranslator();
		adapter = stmt.getAdapter();
		if (stmt.nModuleLang != 0) {
			nModuleLang = stmt.nModuleLang;
			UG_CONVERT = true;
			GU_CONVERT = true;
		}

	}

	public void setTranlateWave(boolean tranlateWave) {
		if (adapter != null && adapter instanceof BaseAdapter) {
			BaseAdapter ba = (BaseAdapter) adapter;
			ba.setWaveToNull(tranlateWave);
		}
	}

	public ResultSet getResultSet() {
		return rs;
	}

	public boolean absolute(int row) throws SQLException {
		try {
			return rs.absolute(row);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public void afterLast() throws SQLException {
		try {
			rs.afterLast();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void beforeFirst() throws SQLException {
		try {
			rs.beforeFirst();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void cancelRowUpdates() throws SQLException {
		try {
			rs.cancelRowUpdates();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void clearWarnings() throws SQLException {
		try {
			rs.clearWarnings();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void close() throws SQLException {
		try {

			if (rs != null) {
				// if (Trace.isEnabled())
				// Trace.traceSQL("Query Rows=" + nRowCount + "");
				rs.close();
				rs = null;
				// TODO:NEED HGY TO AUDIT.
				// ThreadTracer.getInstance().endReadRs(nRowCount);
			}
			if (stmt != null)
				stmt.deregisterResultSet(this);
			// if (Trace.isEnabled())
			// Trace.traceResult("Close:" + getId());
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void deleteRow() throws SQLException {
		try {
			rs.deleteRow();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int findColumn(String arg0) throws SQLException {
		try {
			return rs.findColumn(arg0);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public boolean first() throws SQLException {
		try {
			return rs.first();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public Array getArray(int i) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Array getArray(String colName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public java.io.InputStream getAsciiStream(int columnIndex) throws SQLException {
		try {
			InputStream result = rs.getAsciiStream(columnIndex);
			// if (Trace.isEnabled())
			// resultString = resultString.append("|").append(
			// Trace.quoteObject(result));
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.io.InputStream getAsciiStream(String columnName) throws SQLException {
		try {
			InputStream result = rs.getAsciiStream(columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.math.BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		try {

			return rs.getBigDecimal(columnIndex);

		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public java.math.BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public java.math.BigDecimal getBigDecimal(String columnName) throws SQLException {
		try {
			return rs.getBigDecimal(columnName);

		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.math.BigDecimal getBigDecimal(String columnIndex, int scale) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public java.io.InputStream getBinaryStream(int columnIndex) throws SQLException {
		try {
			InputStream result = rs.getBinaryStream(columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.io.InputStream getBinaryStream(String columnName) throws SQLException {
		try {
			InputStream result = rs.getBinaryStream(columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Blob getBlob(int i) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Blob getBlob(String colName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		try {

			boolean result = adapter.getBoolean(rs, columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean getBoolean(String columnName) throws SQLException {
		try {
			boolean result = adapter.getBoolean(rs, columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public byte getByte(int columnIndex) throws SQLException {
		try {
			byte result = rs.getByte(columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public byte getByte(String columnName) throws SQLException {
		try {

			byte result = rs.getByte(columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		try {

			byte[] result = adapter.getBytes(rs, columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public byte[] getBytes(String columnName) throws SQLException {
		try {

			byte[] result = adapter.getBytes(rs, columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.io.Reader getCharacterStream(int columnIndex) throws SQLException {
		try {

			Reader result = adapter.getCharacterStream(rs, columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.io.Reader getCharacterStream(String columnName) throws SQLException {
		try {
			return adapter.getCharacterStream(rs, columnName);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Clob getClob(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Clob getClob(String colName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getConcurrency() throws SQLException {
		try {
			return rs.getConcurrency();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public String getCursorName() throws SQLException {
		try {
			return rs.getCursorName();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Date getDate(int arg0) throws SQLException {
		try {
			Date result = rs.getDate(arg0);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Date getDate(int columnIndex, java.util.Calendar cal) throws SQLException {
		try {
			Date result = rs.getDate(columnIndex, cal);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Date getDate(String arg0) throws SQLException {
		try {
			Date result = rs.getDate(arg0);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Date getDate(String columnName, java.util.Calendar cal) throws SQLException {
		try {
			Date result = rs.getDate(columnName, cal);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public double getDouble(int columnIndex) throws SQLException {
		try {
			double result = rs.getDouble(columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public double getDouble(String columnName) throws SQLException {
		try {

			double result = rs.getDouble(columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getFetchDirection() throws SQLException {
		try {
			return rs.getFetchDirection();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getFetchSize() throws SQLException {
		try {
			return rs.getFetchSize();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public float getFloat(int columnIndex) throws SQLException {
		try {

			float result = rs.getFloat(columnIndex);
			return result;

		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public float getFloat(String columnName) throws SQLException {
		try {

			float result = rs.getFloat(columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getInt(int columnIndex) throws SQLException {
		try {
			return rs.getInt(columnIndex);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getInt(String columnName) throws SQLException {
		try {

			return rs.getInt(columnName);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public long getLong(int columnIndex) throws SQLException {
		try {

			long result = rs.getLong(columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public long getLong(String columnName) throws SQLException {
		try {
			long result = rs.getLong(columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	private java.sql.ResultSetMetaData metaData = null;

	// todo
	public java.sql.ResultSetMetaData getMetaData() throws SQLException {
		if (metaData == null) {
			try {
				metaData = new CrossDBResultSetMetaData(rs.getMetaData());
			} catch (SQLException e) {
				throw trans.getSqlException(e);
			}
		}
		return metaData;

	}

	public Object getObject(int index) throws SQLException {
		try {

			return adapter.getObject(rs, index, getMetaData().getScale(index));
		} catch (NegativeArraySizeException e) {
			return null;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Object getObject(String columnName) throws SQLException {
		try {

			return adapter.getObject(rs, columnName, getMetaData().getScale(getColumnIndex(columnName)));
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public Ref getRef(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Ref getRef(String colName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getRow() throws SQLException {
		return rs.getRow();
	}

	public short getShort(int columnIndex) throws SQLException {
		try {

			short result = rs.getShort(columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public short getShort(String columnName) throws SQLException {
		try {
			short result = rs.getShort(columnName);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Statement getStatement() throws SQLException {
		return this.stmt;
	}

	public String getString(int columnIndex) throws SQLException {
		try {
			return adapter.getString(rs, columnIndex);

		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public String getString(String columnName) throws SQLException {
		try {
			return adapter.getString(rs, columnName);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Time getTime(int columnIndex) throws SQLException {
		try {
			Time result = rs.getTime(columnIndex);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Time getTime(int columnIndex, java.util.Calendar cal) throws SQLException {
		try {
			Time result = rs.getTime(columnIndex, cal);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public java.sql.Time getTime(String arg0) throws SQLException {
		try {
			Time result = rs.getTime(arg0);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Time getTime(String columnName, java.util.Calendar cal) throws SQLException {
		try {
			Time result = rs.getTime(columnName, cal);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Timestamp getTimestamp(int arg0) throws SQLException {
		try {
			Timestamp result = rs.getTimestamp(arg0);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Timestamp getTimestamp(int columnIndex, java.util.Calendar cal) throws SQLException {
		try {
			Timestamp result = rs.getTimestamp(columnIndex, cal);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Timestamp getTimestamp(String arg0) throws SQLException {
		try {
			Timestamp result = rs.getTimestamp(arg0);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public java.sql.Timestamp getTimestamp(String columnName, java.util.Calendar cal) throws SQLException {
		try {
			Timestamp result = rs.getTimestamp(columnName, cal);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getType() throws SQLException {
		return rs.getType();
	}

	public java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException {

		throw new UnsupportedOperationException();
	}

	public java.io.InputStream getUnicodeStream(String columnName) throws SQLException {

		throw new UnsupportedOperationException();
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		try {
			return rs.getWarnings();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void insertRow() throws SQLException {
		rs.insertRow();
	}

	public boolean isAfterLast() throws SQLException {
		try {
			return rs.isAfterLast();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean isBeforeFirst() throws SQLException {
		try {
			return rs.isBeforeFirst();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean isFirst() throws SQLException {
		try {
			return rs.isFirst();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean isLast() throws SQLException {
		try {
			return rs.isLast();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean last() throws SQLException {
		try {
			return rs.last();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void moveToCurrentRow() throws SQLException {
		try {
			rs.moveToCurrentRow();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void moveToInsertRow() throws SQLException {
		try {
			rs.moveToInsertRow();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean next() throws SQLException {
		try {
			// TODO:NEED HGY AUDIT.
//			if (nRowCount == 0) {
//				ThreadTracer.getInstance().beginReadRs();
//			}
			if (maxRows > 0 && nRowCount > maxRows) {
				throw new SQLException("Read too much data from database for one time!!: " + maxRows);

			} else if (nRowCount > MAX_ROW) {
				throw new SQLException("Read too much data from database for one time!!(-1): " + MAX_ROW);
			}
			boolean hasNext = rs.next();

			if (hasNext) {
				nRowCount++;
			}

			return hasNext;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean previous() throws SQLException {
		try {
			return rs.previous();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void refreshRow() throws SQLException {
		try {
			rs.refreshRow();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean relative(int rows) throws SQLException {
		try {
			return rs.relative(rows);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public boolean rowDeleted() throws SQLException {
		try {
			return rs.rowDeleted();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean rowInserted() throws SQLException {
		try {
			return rs.rowInserted();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public boolean rowUpdated() throws SQLException {
		try {
			return rs.rowUpdated();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setFetchDirection(int direction) throws SQLException {
		try {
			rs.setFetchDirection(direction);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void setFetchSize(int rows) throws SQLException {
		try {
			rs.setFetchSize(rows);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) throws SQLException {
		rs.updateAsciiStream(columnIndex, x, length);

	}

	public void updateAsciiStream(String columnName, java.io.InputStream x, int length) throws SQLException {
		rs.updateAsciiStream(columnName, x, length);

	}

	public void updateBigDecimal(int columnIndex, java.math.BigDecimal x) throws SQLException {
		rs.updateBigDecimal(columnIndex, x);

	}

	public void updateBigDecimal(String columnName, java.math.BigDecimal x) throws SQLException {
		rs.updateBigDecimal(columnName, x);

	}

	public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) throws SQLException {
		rs.updateBinaryStream(columnIndex, x, length);

	}

	public void updateBinaryStream(String columnName, java.io.InputStream x, int length) throws SQLException {
		rs.updateBinaryStream(columnName, x, length);

	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		rs.updateBoolean(columnIndex, x);

	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		rs.updateBoolean(columnName, x);

	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		rs.updateByte(columnIndex, x);

	}

	public void updateByte(String columnName, byte x) throws SQLException {
		rs.updateByte(columnName, x);

	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		rs.updateBytes(columnIndex, x);

	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		rs.updateBytes(columnName, x);

	}

	public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) throws SQLException {
		rs.updateCharacterStream(columnIndex, x, length);

	}

	public void updateCharacterStream(String columnName, java.io.Reader reader, int length) throws SQLException {
		rs.updateCharacterStream(columnName, reader, length);

	}

	public void updateDate(int columnIndex, java.sql.Date x) throws SQLException {
		rs.updateDate(columnIndex, x);

	}

	public void updateDate(String columnName, java.sql.Date x) throws SQLException {
		rs.updateDate(columnName, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		rs.updateDouble(columnIndex, x);

	}

	public void updateDouble(String columnName, double x) throws SQLException {
		rs.updateDouble(columnName, x);

	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		rs.updateFloat(columnIndex, x);

	}

	public void updateFloat(String columnName, float x) throws SQLException {

		rs.updateFloat(columnName, x);
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		rs.updateInt(columnIndex, x);

	}

	public void updateInt(String columnName, int x) throws SQLException {
		rs.updateInt(columnName, x);

	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		rs.updateLong(columnIndex, x);

	}

	public void updateLong(String columnName, long x) throws SQLException {
		rs.updateLong(columnName, x);

	}

	public void updateNull(int columnIndex) throws SQLException {
		rs.updateNull(columnIndex);

	}

	public void updateNull(String columnName) throws SQLException {
		rs.updateNull(columnName);

	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		rs.updateObject(columnIndex, x);

	}

	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
		rs.updateObject(columnIndex, x, scale);

	}

	public void updateObject(String columnName, Object x) throws SQLException {
		rs.updateObject(columnName, x);

	}

	public void updateObject(String columnName, Object x, int scale) throws SQLException {
		rs.updateObject(columnName, x, scale);

	}

	public void updateRow() throws SQLException {
		rs.updateRow();

	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		rs.updateShort(columnIndex, x);
	}

	public void updateShort(String columnName, short x) throws SQLException {
		rs.updateShort(columnName, x);

	}

	public void updateString(int columnIndex, String x) throws SQLException {
		rs.updateString(columnIndex, x);

	}

	public void updateString(String columnName, String x) throws SQLException {
		rs.updateString(columnName, x);

	}

	public void updateTime(int columnIndex, java.sql.Time x) throws SQLException {
		rs.updateTime(columnIndex, x);

	}

	public void updateTime(String columnName, java.sql.Time x) throws SQLException {
		rs.updateTime(columnName, x);

	}

	public void updateTimestamp(int columnIndex, java.sql.Timestamp x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	public void updateTimestamp(String columnName, java.sql.Timestamp x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	public boolean wasNull() throws SQLException {
		try {
			return rs.wasNull();
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public int getColumnIndex(String colName) throws SQLException {
		CrossDBResultSetMetaData rmd = (CrossDBResultSetMetaData) getMetaData();
		int idx = 0;
		for (int i = 0; i < rmd.getColumnCount(); i++) {
			if (colName.equalsIgnoreCase(rmd.getColumnName(i)) || colName.equalsIgnoreCase(rmd.getColumnLabel(i))) {
				idx = i + 1;
				break;
			}
		}
		return idx;
	}

	/**
	 * 为了得到申请结果集的位置
	 */
	protected Exception eCreateResultSetException;

	/**
	 * 读取过多少行
	 */
	// private int nRowCount = 0;
	/**
	 * 此处插入方法说明。 创建日期：(2002-8-7 19:19:59)
	 * 
	 * @param index
	 *            int
	 * @return byte[]
	 */
	public byte[] getBlobBytes(int index) throws SQLException, java.io.IOException {
		try {
			return adapter.getBlobByte(rs, index);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-10-28 16:13:01)
	 * 
	 * @param
	 * @return byte[]
	 */
	public byte[] getBlobBytes(String columnName) throws SQLException, java.io.IOException {
		try {
			return adapter.getBlobByte(rs, columnName);
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	public String getClobStrings(int columnIndex) throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId(), columnIndex);
			String result = adapter.getClobString(rs, columnIndex);
//			if (Trace.isEnabled())
//				Trace.traceResult(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}

	}

	public String getClobStrings(String colName) throws SQLException {
		try {
//			if (Trace.isEnabled())
//				Trace.trace(getId(), colName);
			String result = adapter.getClobString(rs, colName);
//			if (Trace.isEnabled())
//				Trace.traceResult(result);
			return result;
		} catch (SQLException e) {
			throw trans.getSqlException(e);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#getURL(int)
	 */
	public URL getURL(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	public URL getURL(String columnName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	public void updateArray(String columnName, Array x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	public void updateBlob(String columnName, Blob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	public void updateClob(String columnName, Clob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	public void updateRef(String columnName, Ref x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	String getId() {

		return "resultset(" + stmt.con.id + "_" + id + ")";
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.rs.isClosed();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {throw new UnsupportedOperationException();}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public void updateNClob(int columnIndex, NClob clob) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(String columnLabel, NClob clob) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNString(int columnIndex, String string) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateNString(String columnLabel, String string) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException();	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("not supported");
	}
}