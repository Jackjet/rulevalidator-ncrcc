package com.yonyou.nc.codevalidator.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import com.yonyou.nc.codevalidator.resparser.datasource.DataSourceMetaMgr;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;

public class PhysicalDbPool extends DBConnectionPool {

	private DataSourceMeta meta;
	private Driver driver;

	public PhysicalDbPool(DataSourceMeta meta) {
		this.meta = meta;
		if (meta == null) {
			meta = DataSourceMetaMgr.getInstance().getDefaultDataSourceMeta();
		}
		initDriver();
		super.initPool();
	}

	public void initDriver() {
		try {
			driver = (Driver) Class.forName(meta.getDriverClassName().trim())
					.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException("init PhysicalDbPool error", e);
		}
	}

	@Override
	protected PoolableConnection createConnection() throws SQLException {
		String strUrl = meta.getDatabaseUrl();
		String user = meta.getUser();
		String pwd = meta.getPassword();
		java.util.Properties info = meta.getJdbcProperties();
		if (user != null) {
			info.put("user", user);
		}
		if (pwd != null) {
			info.put("password", pwd);
		}
		Connection newConn = driver.connect(strUrl, info);
		PoolableConnection conn = new PoolableConnection(newConn);
		conn.setPool(this);
		return conn;
	}

	@Override
	public PoolableConnection getConnection() {
		PoolableConnection conn = super.getConnection();
		return conn;
	}
}
