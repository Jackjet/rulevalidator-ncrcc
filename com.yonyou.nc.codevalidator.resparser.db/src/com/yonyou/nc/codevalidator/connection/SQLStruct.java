package com.yonyou.nc.codevalidator.connection;

public class SQLStruct {

	private String sql = null;

	private boolean addTs = false;

	public SQLStruct(String sql, boolean addTs) {
		this.sql = sql;
		this.addTs = addTs;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isAddTs() {
		return addTs;
	}

	public void setAddTs(boolean addTs) {
		this.addTs = addTs;
	}

}
