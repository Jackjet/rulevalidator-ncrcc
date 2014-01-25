package com.yonyou.nc.codevalidator.adapter;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.yonyou.nc.codevalidator.crossdb.CrossDBPreparedStatement;
import com.yonyou.nc.codevalidator.crossdb.CrossDBStatement;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class SQLServerAdapter extends BaseAdapter {
	private static Class<?> stmtClass;
	private static Method sm;
	static {
		try {
			stmtClass = Class
					.forName("com.microsoft.sqlserver.jdbc.SQLServerStatement");
			sm = stmtClass.getMethod("setResponseBuffering", String.class);
		} catch (Exception e) {
			Logger.error("SQLServerAdapter static init error", e);
		}
	}

	public String getName() {
		return getClass().getName();
	}

	public String getDriverClass() {
		return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	}

	public Object getObject(ResultSet rs, int columnIndex, int scale)
			throws SQLException {
		Object value = super.getObject(rs, columnIndex, scale);
		if (value instanceof Short)
			return Integer.valueOf(String.valueOf(value));
		return value;
	}

	public Object getObject(ResultSet rs, String columnName, int scale)
			throws SQLException {
		Object value = super.getObject(rs, columnName, scale);
		if (value instanceof Short)
			return Integer.valueOf(String.valueOf(value));
		return value;
	}

	public String getBinaryConstant(String s) {
		return "0x" + s;
	}

	public String getNow() throws SQLException {
		return "select GETDATE() value ";
	}

	public void setNull(CrossDBPreparedStatement prep, int parameterIndex,
			int sqlType) throws SQLException {
		if (sqlType == Types.CLOB) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.VARCHAR);
		} else if (sqlType == Types.BLOB) {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, Types.BINARY);
		} else {
			((PreparedStatement) prep.getVendorObject()).setNull(
					parameterIndex, sqlType);
		}
	}

	@Override
	public void supportHugeData(CrossDBStatement stmt) throws SQLException {
		if (stmtClass != null && sm != null) {
			Statement vs = stmt.getVendorObject();
			try {
				sm.invoke(vs, "adaptive");
			} catch (Exception e) {
				throw new SQLException("not support adaptive");
			}
		} else {
			throw new SQLException("invalid sqlserver");
		}

	}

}
