package com.yonyou.nc.codevalidator.adapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import oracle.jdbc.OracleStatement;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import com.yonyou.nc.codevalidator.crossdb.CrossDBPreparedStatement;
import com.yonyou.nc.codevalidator.crossdb.CrossDBStatement;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class OracleAdapter extends BaseAdapter {
	protected boolean isOracle9i;

	protected boolean supportsAnsiJoinSyntax;

	private final int MAX_CHAR2CLOB_BUFFER = 1024;

	String tableName = "table_test";
	
	public String getName() {
		return getClass().getName();
	}

	public String getDriverClass() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	public String getBinaryConstant(String s) {
		return "HEXTORAW('" + s + "')";
	}

	public String getNow() throws SQLException {
		return "select sysdate value from dual";
	}

	public String getDateConstant(String s) {
		s = java.sql.Date.valueOf(s).toString();
		return "TO_DATE('" + s + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS')";
	}

	public String getTimestampConstant(String s) {
		s = java.sql.Timestamp.valueOf(s).toString();
		int i = s.indexOf('.');
		if (i != -1) {
			s = s.substring(0, i);
		}
		return "TO_DATE('" + s + "', 'YYYY-MM-DD HH24:MI:SS')";
	}

	public boolean supportsAnsiJoinSyntax() {
		return supportsAnsiJoinSyntax;
	}

	public void setNull(CrossDBPreparedStatement prep, int parameterIndex,
			int sqlType) throws SQLException {
		if (sqlType == Types.CHAR) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.VARCHAR);
		} else {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, sqlType);
		}
	}

	public byte[] getBytes(ResultSet rs, int columnIndex) throws SQLException {
		byte[] result = null;
		try {
			result = getBlobByte(rs, columnIndex);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
		return result;
	}

	public byte[] getBytes(ResultSet rs, String columnName) throws SQLException {
		byte[] result = null;
		try {
			result = getBlobByte(rs, columnName);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
		return result;
	}

	public String getClobString(ResultSet rs, String columnName)
			throws SQLException {
		oracle.sql.CLOB tmpClob = null;

		tmpClob = (oracle.sql.CLOB) rs.getClob(columnName);
		if (tmpClob == null)
			return null;
		return tmpClob.getSubString(1, (int) tmpClob.length());
	}

	public String getClobString(ResultSet rs, int columnIndex)
			throws SQLException {
		oracle.sql.CLOB tmpClob = null;
		tmpClob = (oracle.sql.CLOB) rs.getClob(columnIndex);

		if (tmpClob == null)
			return null;
		return tmpClob.getSubString(1, (int) tmpClob.length());
	}

	public void setBytes(CrossDBPreparedStatement prep, int parameterIndex,
			byte[] x) throws SQLException {
		if (x == null || x.length < 4000) {
			((PreparedStatement) prep.getVendorObject()).setBytes(
					parameterIndex, x);
		} else {
			setBinaryStream(prep, parameterIndex, new ByteArrayInputStream(x),
					x.length);
		}
	}

	@SuppressWarnings("deprecation")
	public void setBinaryStream(CrossDBPreparedStatement prep,
			int parameterIndex, InputStream x, int length) throws SQLException {
		if (x == null) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.BLOB);
			return;
		}
		try {
//			oracle.sql.BLOB blob = oracle.sql.BLOB.createTemporary(
//					getExtractor().getNativeConnection(nativeConn), false,
//					oracle.sql.BLOB.DURATION_SESSION);
			oracle.sql.BLOB blob = oracle.sql.BLOB.createTemporary(
					nativeConn, false,
					oracle.sql.BLOB.DURATION_SESSION);

			blob.open(BLOB.MODE_READWRITE);

			OutputStream out = blob.getBinaryOutputStream();
			byte[] buffer = new byte[2 * 1024];
			int max = buffer.length;

			while (true) {
				int len = x.read(buffer, 0, max);
				if (len == -1) {
					break;
				}
				out.write(buffer, 0, len);
			}
			out.close();
			((PreparedStatement) prep.getVendorObject()).setBlob(
					parameterIndex, blob);
		} catch (Throwable e) {
			throw convertThrowable(e);
		}
	}

	public void setAsciiStream(CrossDBPreparedStatement prep,
			int parameterIndex, InputStream x, int length) throws SQLException {
		if (x == null) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.CLOB);
			return;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			for (int i = 0; i < length; i++) {
				int c = x.read();
				if (c == -1) {
					throw new SQLException("stream is closed at i:" + i
									+ " length:" + length);
				}
				buffer.append((char) c);
			}
			setCharacterStream(prep, parameterIndex,
					new StringReader(buffer.toString()), buffer.toString()
							.length());
		} catch (IOException e) {
			throw convertThrowable(e);
		}
	}

	@SuppressWarnings("deprecation")
	public void setCharacterStream(CrossDBPreparedStatement prep,
			int parameterIndex, Reader x, int length) throws SQLException {
		if (x == null) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.CLOB);
			return;
		}
		// Statement stat = conn.createStatement();
		// createTempLobTable(stat);
		try {
			oracle.sql.CLOB clob = oracle.sql.CLOB.createTemporary(
					nativeConn, false,
					oracle.sql.CLOB.DURATION_SESSION);
//			oracle.sql.CLOB clob = oracle.sql.CLOB.createTemporary(
//					getExtractor().getNativeConnection(nativeConn), false,
//					oracle.sql.CLOB.DURATION_SESSION);

			clob.open(CLOB.MODE_READWRITE);

			Writer out = clob.getCharacterOutputStream();

			char[] buffer = new char[MAX_CHAR2CLOB_BUFFER];
//			int remaining = length;
			while (true) {
				// int max=Math.max(buffer.length,remaining);
				int max = buffer.length;
				int len = x.read(buffer, 0, max);
				if (len == -1) {
					break;
				}
//				remaining -= len;
				out.write(buffer, 0, len);
			}
			out.close();
			((PreparedStatement) prep.getVendorObject()).setClob(
					parameterIndex, clob);
			// Logger.info("------ set String over-----");
		} catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			throw convertThrowable(e);
		} finally {

		}
	}

	public byte[] getBlobByte(ResultSet rs, int index) throws SQLException,
			IOException {
		return getBytes(rs.getBlob(index));
	}

	private byte[] getBytes(Blob b) throws SQLException, IOException {
		java.io.InputStream in = null;
		java.io.ByteArrayOutputStream baos = null;
		try {
			if (b == null)
				return null;
			in = b.getBinaryStream();
			baos = new java.io.ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = in.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			return baos.toByteArray();
		} finally {
			if (in != null)
				in.close();
			if (baos != null)
				baos.close();
		}
	}

	public byte[] getBlobByte(ResultSet rs, String columnName)
			throws SQLException, IOException {
		return getBytes(rs.getBlob(columnName));
	}

	public Object getObject(ResultSet rs, int columnIndex, int scale)
			throws SQLException {
		Object value = super.getObject(rs, columnIndex, scale);
		if (value instanceof Blob) {
			try {
				return getBytes((Blob) value);
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
				throw convertThrowable(e);
			}
		}
		if (value instanceof java.math.BigDecimal && scale <= 0) {
			BigDecimal decimal = ((BigDecimal) value).abs();
			if ((decimal.doubleValue() - decimal.intValue()) < 10e-9) {
				value = Integer.valueOf(((java.math.BigDecimal) value).intValue());
			}
		}
		return value;
	}

	public Object getObject(ResultSet rs, String columnName, int scale)
			throws SQLException {
		Object value = super.getObject(rs, columnName, scale);
		if (value instanceof Blob) {
			try {
				return getBytes((Blob) value);
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
				throw convertThrowable(e);
			}
		}
		if (value instanceof BigDecimal) {
			if (scale <= 0) {
				BigDecimal decimal = ((BigDecimal) value).abs();
				if ((decimal.doubleValue() - decimal.intValue()) < 10e-9) {
					value =Integer.valueOf(((BigDecimal) value).intValue());
				}
			}
		}
		return value;
	}

	@Override
	public void supportHugeData(CrossDBStatement stmt) throws SQLException {
		Statement vs = stmt.getVendorObject();
		if (vs instanceof OracleStatement) {
			((OracleStatement) vs).setRowPrefetch(1);
		}
	}
	
//	private NativeJdbcExtractor getExtractor() {
//		boolean stx = this.nativeConn.getClass().getName()
//				.startsWith("nc.bs.framework.ds");
//		NativeJdbcExtractor extractor = stx ? new SingleTxJdbcExtractor()
//				: NativeJdbcExtractorFactory.createJdbcExtractor();
//		return extractor;
//
//	}

}
