/*
 * �������� 2005-7-13
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.yonyou.nc.codevalidator.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * @author hey
 *         <p/>
 *         ����־û�������������
 */
abstract public class PersistenceManager {
	protected int maxRows = 100000;

	/**
	 * �ͷ���Դ
	 */

	abstract public void release();

	/**
	 * �õ�JdbcSession
	 * 
	 * @return ����JdbcSession
	 */
	abstract public JdbcSession getJdbcSession();

	/**
	 * �õ����ݿ�����
	 * 
	 * @return
	 */

	abstract public int getDBType();

	/**
	 * �õ���ǰ���ݿ��DatabaseMetaData
	 * 
	 * @return DatabaseMetaData
	 */
	public abstract DatabaseMetaData getMetaData();

	/**
	 * �õ���ǰ���ݿ��Catalog
	 * 
	 * @return Catalog����
	 */
	public abstract String getCatalog();

	/**
	 * �õ���ǰ���ݿ��Schema
	 * 
	 * @return Schema����
	 */
	public abstract String getSchema();

	// /**
	// * ����Ĭ������Դ�õ�PersistenceManagerʵ��
	// *
	// * @return
	// * @throws DAOException
	// * ����������׳�DAOException
	// */
	// static public PersistenceManager getInstance() throws DAOException {
	// return new JdbcPersistenceManager();
	// }

	/**
	 * ���ݴ��������Դ�����õ�PersistenceManagerʵ��
	 * 
	 * @param dataSourceName
	 *            ����Դ����
	 * @return
	 * @throws DAOException
	 *             ����������׳�DAOException
	 */
	static public PersistenceManager getInstance(String dataSourceName) throws DAOException {
		return new JdbcPersistenceManager(dataSourceName);

	}

	/**
	 * ���ݴ��������Դ�����õ�PersistenceManagerʵ��
	 * 
	 * @param connection
	 *            - ���ݿ�����
	 * @return
	 * @throws DAOException
	 *             ����������׳�DAOException
	 */
	static public PersistenceManager getInstance(Connection connection) throws DAOException {
		return new JdbcPersistenceManager(connection);

	}

	/**
	 * ���ݴ����JdbcSession�����õ�PersistenceManagerʵ��
	 * 
	 * @param session
	 *            JdbcSession����
	 * @return
	 * @throws DAOException
	 *             ����������׳�DAOException
	 */
	static public PersistenceManager getInstance(JdbcSession session) {
		return new JdbcPersistenceManager(session);

	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

}