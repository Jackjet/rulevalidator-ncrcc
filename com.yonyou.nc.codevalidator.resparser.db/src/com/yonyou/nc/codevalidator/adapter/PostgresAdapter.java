package com.yonyou.nc.codevalidator.adapter;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.yonyou.nc.codevalidator.crossdb.CrossDBPreparedStatement;
import com.yonyou.nc.codevalidator.crossdb.CrossDBStatement;
import com.yonyou.nc.codevalidator.dao.DBUtil;

public class PostgresAdapter extends BaseAdapter {
	public String getName() {
		return getClass().getName();
	}

	public String getDriverClass() {
		return "org.postgresql.Driver";
	}

	public Object getObject(ResultSet rs, int columnIndex, int scale)
			throws SQLException {
		Object value = super.getObject(rs, columnIndex, scale);
		return convert(value, scale);
	}

	public Object getObject(ResultSet rs, String columnName, int scale)
			throws SQLException {
		Object value = super.getObject(rs, columnName, scale);
		return convert(value, scale);
	}

	private static Object convert(Object value, int scale) {
		if (value instanceof Short) {
			return Integer.valueOf(String.valueOf(value));
		} else if (value instanceof BigDecimal) {
			if (value instanceof java.math.BigDecimal && scale <= 0) {
				if (DBUtil.needToInt((BigDecimal) value)) {
					value = Integer.valueOf(((BigDecimal) value).intValue());
				}
			}
			return value;
		} else if (value instanceof Long) {
			Long l = Math.abs(((Long) value));
			if (l.compareTo(Long.valueOf(Integer.MAX_VALUE)) < 0) {
				return ((Long) value).intValue();
			}
		}
		return value;
	}

	public boolean getBoolean(ResultSet rs, String columnName)
			throws SQLException {
		Object obj = rs.getObject(columnName);
		if (null == obj) {
			return false;
		}
		if (obj instanceof Integer) {
			return ((Integer)obj) == 0 ? false : true; 
		} else if (obj instanceof String) {
			return ((String)obj).equals("Y") ? true : false; 
		}
		return false;
	}

	public boolean getBoolean(ResultSet rs, int columnIndex)
			throws SQLException {
		Object obj = rs.getObject(columnIndex);
		if (null == obj) {
			return false;
		}
		if (obj instanceof Integer ) {
			return ((Integer)obj) == 0 ? false : true; 
		} else if (obj instanceof String) {
			return ((String)obj).equals("Y") || "1".equals(obj) ? true : false; 
		}
		return false;
	}

//	@Override
//	public void setBoolean(CrossDBPreparedStatement prep, int parameterIndex,
//			boolean v) throws SQLException {
//		((PreparedStatement) prep.getVendorObject()).setString(parameterIndex,
//				v ? "Y" : "N");
//	}
	
	@Override
	public void setBoolean(CrossDBPreparedStatement prep, int parameterIndex,
			boolean v) throws SQLException {
		((PreparedStatement) prep.getVendorObject()).setInt(parameterIndex,
				v ? 1 : 0);
	}
	

	@Override
	public void supportHugeData(CrossDBStatement stmt) throws SQLException {

	}

}
