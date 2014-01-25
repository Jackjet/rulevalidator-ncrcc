package com.yonyou.nc.codevalidator.sdk.datasource;

import java.io.Serializable;
import java.util.Properties;

/**
 * User: ºÎ¹ÚÓî Date: 2005-11-9 Time: 11:27:11
 * 
 * The IDataSourceMeta implementation
 */
public class DataSourceMeta implements Cloneable, Serializable {

	private static final long serialVersionUID = 1774004595340815036L;

	private String dataSourceName = null;

	private String oidMark = null;

	private String databaseUrl = "";

	private String user = "";

	private String password = "";

	private String driverClassName = "";

	private boolean dualFlag;

	public boolean isDualFlag() {
		return dualFlag;
	}

	public void setDualFlag(boolean dualFlag) {
		this.dualFlag = dualFlag;
	}

	private Properties jdbcProperties = new Properties();

	public DataSourceMeta() {

	}

	public DataSourceMeta(String adataSourceName, String aoidMark, String databaseUrl, String user, String password,
			String driverClassName) {
		dataSourceName = adataSourceName;
		oidMark = aoidMark;
		this.databaseUrl = databaseUrl;
		this.user = user;
		this.password = password;
		this.driverClassName = driverClassName;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public String getOIDMark() {
		return oidMark;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public Properties getJdbcProperties() {
		return jdbcProperties;
	}

	public void setJdbcProperties(Properties jdbcProperties) {
		if (jdbcProperties == null) {
			this.jdbcProperties = new Properties();
		} else {
			this.jdbcProperties = jdbcProperties;
		}
	}

	public Object clone() {
		DataSourceMeta result = new DataSourceMeta();
		result.user = this.user;
		result.password = this.password;
		result.databaseUrl = this.databaseUrl;
		result.dataSourceName = this.dataSourceName;
		result.driverClassName = this.driverClassName;
		result.jdbcProperties = this.jdbcProperties;
		result.oidMark = this.oidMark;
		return result;
	}
}
