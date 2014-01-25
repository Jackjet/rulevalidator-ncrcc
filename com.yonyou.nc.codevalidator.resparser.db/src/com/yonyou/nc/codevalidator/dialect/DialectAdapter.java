package com.yonyou.nc.codevalidator.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;

interface IDialectAdapter {
	Object getObjectByColumnIndex(ResultSet rs, int columnIndex)
			throws SQLException;

	Object getObjectByColumnName(ResultSet rs, String columnName)
			throws SQLException;
}

public class DialectAdapter implements IDialectAdapter {
	private String dbProductName = null;

	public DialectAdapter(String dbProductName) {
		this.dbProductName = dbProductName;
	}

	private IDialectAdapter getAdapter() {
		if (dbProductName.toUpperCase().indexOf("POSTGRESQL") != -1)
			return new PostgreDialectAdapter();
		if (dbProductName.toUpperCase().indexOf("DB2") != -1)
			return new DB2DialectAdapter();
		if (dbProductName.toUpperCase().indexOf("ORACLE") != -1)
			return new OracleDialectAdapter();
		if (dbProductName.toUpperCase().indexOf("SQL") != -1)
			return new SQLServerDialectAdapter();
		return new BaseAdapter();
	}

	@Override
	public Object getObjectByColumnIndex(ResultSet rs, int columnIndex)
			throws SQLException {
		IDialectAdapter adapter = getAdapter();
		if (adapter == null) {
			return rs.getObject(columnIndex);
		}
		return adapter.getObjectByColumnIndex(rs, columnIndex);
	}

	@Override
	public Object getObjectByColumnName(ResultSet rs, String columnName)
			throws SQLException {
		IDialectAdapter adapter = getAdapter();
		if (adapter == null) {
			return rs.getObject(columnName);
		}
		return adapter.getObjectByColumnName(rs, columnName);
	}

}

class BaseAdapter implements IDialectAdapter {

	@Override
	public Object getObjectByColumnIndex(ResultSet rs, int columnIndex)
			throws SQLException {
		return rs.getObject(columnIndex);
	}

	@Override
	public Object getObjectByColumnName(ResultSet rs, String columnName)
			throws SQLException {
		return rs.getObject(columnName);
	}

}

class OracleDialectAdapter extends BaseAdapter {
}

class DB2DialectAdapter extends BaseAdapter {
}

class SQLServerDialectAdapter extends BaseAdapter {
}

class PostgreDialectAdapter extends BaseAdapter {
}
