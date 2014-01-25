package com.yonyou.nc.codevalidator.connection;

import java.sql.Connection;

public interface ConnectionPool {
	public abstract Connection getConnection();
}
