package com.yonyou.nc.codevalidator.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.yonyou.nc.codevalidator.crossdb.CrossDBConnection;
import com.yonyou.nc.codevalidator.datasource.DataSourceCenter;

/**
 * @author hey 数据库连接工厂，封装数据库连接的构造过程，简化客户端的调用
 */

public class ConnectionFactory {
	/**
	 * 得到当前线程数据源的连接
	 * 
	 * @return 返回数据库连接
	 * @throws SQLException
	 *             如果在得到数据连接过程中发生错误抛出数据访问异常
	 */
	static public Connection getConnection() throws SQLException {
		Connection dummy = DataSourceCenter.getInstance().getConnection();
		CrossDBConnection realConn = new CrossDBConnection(dummy);
		return realConn;
	}

	/**
	 * 得到指定数据源的连接
	 * 
	 * @param dataSource
	 *            指定的数据源明称
	 * @return 返回数据库连接
	 * @throws SQLException
	 *             如果在得到数据连接过程中发生错误抛出数据访问异常
	 */
	static public Connection getConnection(String dataSource)
			throws SQLException {
		CrossDBConnection realConn = new CrossDBConnection(DataSourceCenter
				.getInstance().getConnection(dataSource), dataSource);
		return realConn;
	}
}
