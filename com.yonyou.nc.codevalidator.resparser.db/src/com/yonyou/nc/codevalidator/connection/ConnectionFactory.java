package com.yonyou.nc.codevalidator.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.yonyou.nc.codevalidator.crossdb.CrossDBConnection;
import com.yonyou.nc.codevalidator.datasource.DataSourceCenter;

/**
 * @author hey ���ݿ����ӹ�������װ���ݿ����ӵĹ�����̣��򻯿ͻ��˵ĵ���
 */

public class ConnectionFactory {
	/**
	 * �õ���ǰ�߳�����Դ������
	 * 
	 * @return �������ݿ�����
	 * @throws SQLException
	 *             ����ڵõ��������ӹ����з��������׳����ݷ����쳣
	 */
	static public Connection getConnection() throws SQLException {
		Connection dummy = DataSourceCenter.getInstance().getConnection();
		CrossDBConnection realConn = new CrossDBConnection(dummy);
		return realConn;
	}

	/**
	 * �õ�ָ������Դ������
	 * 
	 * @param dataSource
	 *            ָ��������Դ����
	 * @return �������ݿ�����
	 * @throws SQLException
	 *             ����ڵõ��������ӹ����з��������׳����ݷ����쳣
	 */
	static public Connection getConnection(String dataSource)
			throws SQLException {
		CrossDBConnection realConn = new CrossDBConnection(DataSourceCenter
				.getInstance().getConnection(dataSource), dataSource);
		return realConn;
	}
}
