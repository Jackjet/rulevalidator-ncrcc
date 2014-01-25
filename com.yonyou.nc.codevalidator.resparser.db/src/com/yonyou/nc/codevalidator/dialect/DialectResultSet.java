package com.yonyou.nc.codevalidator.dialect;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class DialectResultSet implements ResultSet {

	private ResultSet realResultSet = null;

	private boolean needTranslateWave = true;

	private DialectAdapter adapter = null;

	public final static String NULL_WAVE = "~";

	public DialectResultSet(ResultSet rs) {
//		boolean inServer = RuntimeEnv.getInstance().isRunningInServer();
//		needTranslateWave = !inServer;
		this.realResultSet = rs;
	}

	public void setAdapter(DialectAdapter adapter) {
		this.adapter = adapter;
	}

	public void setTranslateWave(boolean b) {
		this.needTranslateWave = b;
	}

	/**
	 * 
	 * 对～统一转为null
	 * 
	 * @param str
	 * @return
	 */
	private Object waveToNull(Object str) {
		if (str == null) {
			return null;
		}
		if (str instanceof String && NULL_WAVE.equals(str.toString().trim())) {
			return null;
		} else {
			return str;
		}
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		String str = realResultSet.getString(columnIndex);
		if (needTranslateWave) {
			return (String) waveToNull(str);
		} else {
			return str;
		}

	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		String str = realResultSet.getString(columnLabel);
		if (needTranslateWave) {
			return (String) waveToNull(str);
		} else {
			return str;
		}
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		Object obj = null;
		if (adapter != null) {
			obj = adapter.getObjectByColumnIndex(realResultSet, columnIndex);
		} else {
			obj = realResultSet.getString(columnIndex);
		}
		if (needTranslateWave) {
			return waveToNull(obj);
		} else {
			return obj;
		}
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		Object obj = null;
		if (adapter != null) {
			obj = adapter.getObjectByColumnName(realResultSet, columnLabel);
		} else {
			obj = realResultSet.getString(columnLabel);
		}
		if (needTranslateWave) {
			return waveToNull(obj);
		} else {
			return obj;
		}
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		Object obj = realResultSet.getObject(columnIndex, map);
		return waveToNull(obj);
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		Object obj = realResultSet.getObject(columnLabel, map);
		if (needTranslateWave) {
			return waveToNull(obj);
		} else {
			return obj;
		}
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		return realResultSet.absolute(row);
	}

	@Override
	public void afterLast() throws SQLException {
		realResultSet.afterLast();
	}

	@Override
	public void beforeFirst() throws SQLException {
		realResultSet.beforeFirst();

	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		realResultSet.cancelRowUpdates();

	}

	@Override
	public void clearWarnings() throws SQLException {
		realResultSet.clearWarnings();

	}

	@Override
	public void close() throws SQLException {
		realResultSet.close();

	}

	@Override
	public void deleteRow() throws SQLException {
		realResultSet.deleteRow();

	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		return realResultSet.findColumn(columnLabel);
	}

	@Override
	public boolean first() throws SQLException {
		return realResultSet.first();
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		return realResultSet.getArray(columnIndex);
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		return realResultSet.getArray(columnLabel);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return realResultSet.getAsciiStream(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return realResultSet.getAsciiStream(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return realResultSet.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return realResultSet.getBigDecimal(columnLabel);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		return realResultSet.getBigDecimal(columnIndex, scale);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		return realResultSet.getBigDecimal(columnLabel, scale);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return realResultSet.getBinaryStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return realResultSet.getBinaryStream(columnLabel);
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		return realResultSet.getBlob(columnIndex);
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		return realResultSet.getBlob(columnLabel);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return realResultSet.getBoolean(columnIndex);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return realResultSet.getBoolean(columnLabel);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return realResultSet.getByte(columnIndex);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return realResultSet.getByte(columnLabel);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return realResultSet.getBytes(columnIndex);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return realResultSet.getBytes(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return realResultSet.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return realResultSet.getCharacterStream(columnLabel);
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		return realResultSet.getClob(columnIndex);
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		return realResultSet.getClob(columnLabel);
	}

	@Override
	public int getConcurrency() throws SQLException {
		return realResultSet.getConcurrency();
	}

	@Override
	public String getCursorName() throws SQLException {
		return realResultSet.getCursorName();
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return realResultSet.getDate(columnIndex);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return realResultSet.getDate(columnLabel);
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return realResultSet.getDate(columnIndex, cal);
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return realResultSet.getDate(columnLabel, cal);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return realResultSet.getDouble(columnIndex);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return realResultSet.getDouble(columnLabel);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return realResultSet.getFetchDirection();
	}

	@Override
	public int getFetchSize() throws SQLException {
		return realResultSet.getFetchSize();
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return realResultSet.getFloat(columnIndex);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return realResultSet.getFloat(columnLabel);
	}

	@Override
	public int getHoldability() throws SQLException {
		return realResultSet.getHoldability();
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return realResultSet.getInt(columnIndex);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return realResultSet.getInt(columnLabel);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return realResultSet.getLong(columnIndex);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return realResultSet.getLong(columnLabel);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return realResultSet.getMetaData();
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return realResultSet.getNCharacterStream(columnIndex);
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return realResultSet.getNCharacterStream(columnLabel);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return realResultSet.getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return realResultSet.getNClob(columnLabel);
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		return realResultSet.getNString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		return realResultSet.getNString(columnLabel);
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		return realResultSet.getRef(columnIndex);
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		return realResultSet.getRef(columnLabel);
	}

	@Override
	public int getRow() throws SQLException {
		return realResultSet.getRow();
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return realResultSet.getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return realResultSet.getRowId(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return realResultSet.getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return realResultSet.getSQLXML(columnLabel);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return realResultSet.getShort(columnIndex);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return realResultSet.getShort(columnLabel);
	}

	@Override
	public Statement getStatement() throws SQLException {
		return realResultSet.getStatement();
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return realResultSet.getTime(columnIndex);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return realResultSet.getTime(columnLabel);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return realResultSet.getTime(columnIndex, cal);
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return realResultSet.getTime(columnLabel, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return realResultSet.getTimestamp(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return realResultSet.getTimestamp(columnLabel);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		return realResultSet.getTimestamp(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		return realResultSet.getTimestamp(columnLabel, cal);
	}

	@Override
	public int getType() throws SQLException {
		return realResultSet.getType();
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return realResultSet.getURL(columnIndex);
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		return realResultSet.getURL(columnLabel);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return realResultSet.getUnicodeStream(columnIndex);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return realResultSet.getUnicodeStream(columnLabel);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return realResultSet.getWarnings();
	}

	@Override
	public void insertRow() throws SQLException {
		realResultSet.insertRow();
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return realResultSet.isAfterLast();
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return realResultSet.isBeforeFirst();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return realResultSet.isClosed();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return realResultSet.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		return realResultSet.isLast();
	}

	@Override
	public boolean last() throws SQLException {
		return realResultSet.last();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		realResultSet.moveToCurrentRow();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		realResultSet.moveToInsertRow();

	}

	@Override
	public boolean next() throws SQLException {
		return realResultSet.next();
	}

	@Override
	public boolean previous() throws SQLException {
		return realResultSet.previous();
	}

	@Override
	public void refreshRow() throws SQLException {
		realResultSet.refreshRow();
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		return realResultSet.relative(rows);
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return realResultSet.rowDeleted();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return realResultSet.rowInserted();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return realResultSet.rowUpdated();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		realResultSet.setFetchDirection(direction);
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		realResultSet.setFetchSize(rows);

	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		realResultSet.updateArray(columnIndex, x);

	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		realResultSet.updateArray(columnLabel, x);

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		realResultSet.updateAsciiStream(columnIndex, x);

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		realResultSet.updateAsciiStream(columnLabel, x);

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnIndex, x, length);

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnLabel, x, length);

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnIndex, x, length);

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		realResultSet.updateAsciiStream(columnLabel, x, length);

	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		realResultSet.updateBigDecimal(columnIndex, x);

	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		realResultSet.updateBigDecimal(columnLabel, x);

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		realResultSet.updateBinaryStream(columnIndex, x);

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		realResultSet.updateBinaryStream(columnLabel, x);

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		realResultSet.updateBinaryStream(columnIndex, x, length);

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		realResultSet.updateBinaryStream(columnLabel, x, length);

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		realResultSet.updateBinaryStream(columnIndex, x, length);

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		realResultSet.updateBinaryStream(columnLabel, x, length);

	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		realResultSet.updateBlob(columnIndex, x);

	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		realResultSet.updateBlob(columnLabel, x);

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		realResultSet.updateBlob(columnIndex, inputStream);

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		realResultSet.updateBlob(columnLabel, inputStream);

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		realResultSet.updateBlob(columnIndex, inputStream, length);

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		realResultSet.updateBlob(columnLabel, inputStream, length);

	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		realResultSet.updateBoolean(columnIndex, x);

	}

	@Override
	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		realResultSet.updateBoolean(columnLabel, x);

	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		realResultSet.updateByte(columnIndex, x);

	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		realResultSet.updateByte(columnLabel, x);

	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		realResultSet.updateBytes(columnIndex, x);

	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		realResultSet.updateBytes(columnLabel, x);

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		realResultSet.updateCharacterStream(columnIndex, x);

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateCharacterStream(columnLabel, reader);

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		realResultSet.updateCharacterStream(columnIndex, x, length);

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		realResultSet.updateCharacterStream(columnLabel, reader, length);

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		realResultSet.updateCharacterStream(columnIndex, x, length);

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		realResultSet.updateCharacterStream(columnLabel, reader, length);

	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		realResultSet.updateClob(columnIndex, x);

	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		realResultSet.updateClob(columnLabel, x);

	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		realResultSet.updateClob(columnIndex, reader);

	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateClob(columnLabel, reader);

	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		realResultSet.updateClob(columnIndex, reader, length);

	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		realResultSet.updateClob(columnLabel, reader, length);

	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		realResultSet.updateDate(columnIndex, x);

	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		realResultSet.updateDate(columnLabel, x);

	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		realResultSet.updateDouble(columnIndex, x);

	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		realResultSet.updateDouble(columnLabel, x);

	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		realResultSet.updateFloat(columnIndex, x);

	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		realResultSet.updateFloat(columnLabel, x);

	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		realResultSet.updateInt(columnIndex, x);

	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		realResultSet.updateInt(columnLabel, x);

	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		realResultSet.updateLong(columnIndex, x);

	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		realResultSet.updateLong(columnLabel, x);

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		realResultSet.updateNCharacterStream(columnIndex, x);

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateNCharacterStream(columnLabel, reader);

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		realResultSet.updateNCharacterStream(columnIndex, x, length);

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		realResultSet.updateNCharacterStream(columnLabel, reader, length);

	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		realResultSet.updateNClob(columnIndex, nClob);

	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		realResultSet.updateNClob(columnLabel, nClob);

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		realResultSet.updateNClob(columnIndex, reader);

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		realResultSet.updateNClob(columnLabel, reader);

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		realResultSet.updateNClob(columnIndex, reader, length);

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		realResultSet.updateNClob(columnLabel, reader, length);

	}

	@Override
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		realResultSet.updateNString(columnIndex, nString);

	}

	@Override
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		realResultSet.updateNString(columnLabel, nString);

	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		realResultSet.updateNull(columnIndex);

	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		realResultSet.updateNull(columnLabel);

	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		realResultSet.updateObject(columnIndex, x);

	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		realResultSet.updateObject(columnLabel, x);

	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
			throws SQLException {
		realResultSet.updateObject(columnIndex, x, scaleOrLength);

	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
			throws SQLException {
		realResultSet.updateObject(columnLabel, x, scaleOrLength);

	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		realResultSet.updateRef(columnIndex, x);

	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		realResultSet.updateRef(columnLabel, x);

	}

	@Override
	public void updateRow() throws SQLException {
		realResultSet.updateRow();

	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		realResultSet.updateRowId(columnIndex, x);

	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		realResultSet.updateRowId(columnLabel, x);

	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		realResultSet.updateSQLXML(columnIndex, xmlObject);

	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		realResultSet.updateSQLXML(columnLabel, xmlObject);

	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		realResultSet.updateShort(columnIndex, x);

	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		realResultSet.updateShort(columnLabel, x);
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		realResultSet.updateString(columnIndex, x);
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		realResultSet.updateString(columnLabel, x);
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		realResultSet.updateTime(columnIndex, x);
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		realResultSet.updateTime(columnLabel, x);

	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		realResultSet.updateTimestamp(columnIndex, x);

	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		realResultSet.updateTimestamp(columnLabel, x);
	}

	@Override
	public boolean wasNull() throws SQLException {
		return realResultSet.wasNull();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return realResultSet.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return realResultSet.unwrap(iface);
	}

}