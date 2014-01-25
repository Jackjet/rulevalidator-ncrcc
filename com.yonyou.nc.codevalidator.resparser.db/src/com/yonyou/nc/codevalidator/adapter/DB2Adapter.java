package com.yonyou.nc.codevalidator.adapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.yonyou.nc.codevalidator.crossdb.CrossDBPreparedStatement;
import com.yonyou.nc.codevalidator.crossdb.CrossDBStatement;

/**
 * @nopublish User: 贺扬 Date: 2005-5-16 Time: 10:52:34 DB2Adapter类的说明
 */
public class DB2Adapter extends BaseAdapter {
	public String getName() {
		return getClass().getName();
	}

	public String getDriverClass() {
		return "COM.ibm.db2.jdbc.net.DB2Driver";
	}

	public String getBinaryConstant(String s) {
		return "BLOB(X'" + s + "')";
	}

	public String getNow() throws SQLException {
		return "select current timestamp from (values 1) as value";
	}

	public void setNull(CrossDBPreparedStatement prep, int parameterIndex,
			int sqlType) throws SQLException {
		if (sqlType == Types.CLOB) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.VARCHAR);
		} else if (sqlType == Types.BLOB)
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.BINARY);
		else if (sqlType == Types.SMALLINT)
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.INTEGER);
		else {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, sqlType);
		}
	}

	synchronized public void setBinaryStream(CrossDBPreparedStatement prep,
			int parameterIndex, InputStream x, int length) throws SQLException {
		if (x == null) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.BINARY);
			return;
		}
		byte[] buffer = null;
		if (length > 0) {
			buffer = new byte[length];
			int offset = 0;
			try {
				while (length > 0) {
					int i = x.read(buffer, offset, length);
					offset += i;
					length -= i;
				}
			} catch (Throwable e) {
				throw convertThrowable(e);
			}
		} else {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			buffer = new byte[1024];
			try {
				while (true) {
					int i = x.read(buffer, 0, buffer.length);
					if (i > 0)
						bout.write(buffer, 0, i);
					if (i == -1) {
						break;
					}
				}
			} catch (Throwable e) {
				throw convertThrowable(e);
			}

			buffer = bout.toByteArray();
		}
		((PreparedStatement) prep.getVendorObject()).setBytes(parameterIndex,
				buffer);
	}

	public void setCharacterStream(CrossDBPreparedStatement prep,
			int parameterIndex, Reader x, int length) throws SQLException {
		if (x == null) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.VARCHAR);
			return;
		}
		char[] buffer = null;
		String toWrite = null;
		if (length > 0) {
			buffer = new char[length];
			int offset = 0;
			try {
				while (length > 0) {
					int i = x.read(buffer, offset, length);
					offset += i;
					length -= i;
				}
			} catch (Throwable e) {
				throw convertThrowable(e);
			}

			toWrite = new String(buffer);

		} else {
			StringWriter sw = new StringWriter();

			buffer = new char[1024];

			try {
				while (length > 0) {
					int i = x.read(buffer, 0, buffer.length);
					if (i > 0) {
						sw.write(buffer, 0, i);
					}

					if (i == -1) {
						break;
					}
				}
			} catch (Throwable e) {
				throw convertThrowable(e);
			}

			toWrite = sw.toString();

		}
		byte[] toWriteBytes = toWrite.getBytes();
		java.io.ByteArrayInputStream input = new ByteArrayInputStream(
				toWriteBytes);
		((PreparedStatement) prep.getVendorObject()).setAsciiStream(
				parameterIndex, input, toWriteBytes.length);
	}

	@Override
	public void supportHugeData(CrossDBStatement stmt) {

	}

}
