package com.yonyou.nc.codevalidator.resparser.temp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class TempTableExecContext {

	private final Map<String, Boolean> initializedMap = new HashMap<String, Boolean>();
	private Connection connection;

	private String multiLangTableName;
	private String dbcreateTableName;
	private String dbcreateDetailTableName;
	private String dbinitTableName;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void initialized(String resId) {
		initializedMap.put(resId, true);
	}

	public boolean isInitialized(String resId) {
		return initializedMap.containsKey(resId) && initializedMap.get(resId);
	}

	public String getMultiLangTableName() {
		return multiLangTableName;
	}

	public void setMultiLangTableName(String multiLangTableName) {
		this.multiLangTableName = multiLangTableName;
	}

	public String getDbcreateTableName() {
		return dbcreateTableName;
	}

	public void setDbcreateTableName(String dbcreateTableName) {
		this.dbcreateTableName = dbcreateTableName;
	}

	public String getDbcreateDetailTableName() {
		return dbcreateDetailTableName;
	}

	public void setDbcreateDetailTableName(String dbcreateDetailTableName) {
		this.dbcreateDetailTableName = dbcreateDetailTableName;
	}

	public String getDbinitTableName() {
		return dbinitTableName;
	}

	public void setDbinitTableName(String dbinitTableName) {
		this.dbinitTableName = dbinitTableName;
	}

}
