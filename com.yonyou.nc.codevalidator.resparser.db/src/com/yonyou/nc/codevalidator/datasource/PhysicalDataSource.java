package com.yonyou.nc.codevalidator.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.yonyou.nc.codevalidator.connection.PhysicalDbPool;
import com.yonyou.nc.codevalidator.resparser.datasource.DataSourceMetaMgr;
import com.yonyou.nc.codevalidator.resparser.datasource.IDataSourceService;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;

/**
 * @author ºÎ¹ÚÓî
 * 
 *         Date: 2006-5-12 Time: 9:38:55
 */
public class PhysicalDataSource implements DataSource {
	private static Map<String, PhysicalDbPool> pools = new HashMap<String, PhysicalDbPool>();

	private DataSourceMeta meta;

	public PhysicalDataSource(String dsName) {
		String dataSourceName = (dsName == null ? IDataSourceService.DEFAULT_DATASOURCE_NAME : dsName);
		meta = dsName == null ? DataSourceMetaMgr.getInstance().getDefaultDataSourceMeta() :
			DataSourceMetaMgr.getInstance().getDataSourceMeta(dataSourceName);
		init(meta);
	}

//	public PhysicalDataSource() {
//		meta = DataSourceMetaMgr.getInstance().getDefaultDataSourceMeta();
//		init(meta);
//	}

	private void init(DataSourceMeta meta) {
		if (pools.get(meta.getDataSourceName()) == null) {
			PhysicalDbPool newPool = new PhysicalDbPool(meta);
			pools.put(meta.getDataSourceName(), newPool);
		}
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public void setLoginTimeout(int seconds) throws SQLException {

	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	public Connection getConnection() throws SQLException {
		PhysicalDbPool pool = pools.get(meta.getDataSourceName());
		return pool.getConnection();
	}

	public Connection getConnection(String user, String pwd)
			throws SQLException {
		throw new SQLException("not support this mode to access db");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("not supported");
	}

}
