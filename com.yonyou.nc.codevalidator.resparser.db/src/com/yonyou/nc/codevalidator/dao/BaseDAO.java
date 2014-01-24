package com.yonyou.nc.codevalidator.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.yonyou.nc.codevalidator.datasource.DataSourceCenter;
import com.yonyou.nc.codevalidator.processor.ResultSetProcessor;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.temptable.TempTable;
import com.yonyou.nc.codevalidator.type.DBConsts;

/**
 * @author hey
 *         <p/>
 *         ���ݿ���ʰ������װ�˳��õĳ־ò���ʲ���
 */

final public class BaseDAO {
	private String dataSource = null;

	int maxRows = 100000;

	boolean addTimestamp = true;

//	/**
//	 * Ĭ�Ϲ��캯������ʹ�õ�ǰ����Դ
//	 */
//	public BaseDAO() {
//	}

	/**
	 * �вι��캯������ʹ��ָ������Դ
	 * 
	 * @param dataSource
	 *            ����Դ����
	 */
	public BaseDAO(String dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * ����SQL ִ�����ݿ��ѯ,������ResultSetProcessor�����Ķ��� ���� Javadoc��
	 * 
	 * @param sql
	 *            ��ѯ��SQL
	 * @param processor
	 *            �����������
	 */
	public Object executeQuery(String sql, ResultSetProcessor processor) throws DAOException {
		PersistenceManager manager = null;
		Object value = null;
		manager = createPersistenceManager(dataSource);
		JdbcSession session = manager.getJdbcSession();
		try {
			value = session.executeQuery(sql, processor);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			session.closeAll();
			if (manager != null)
				manager.release();
		}
		return value;
	}
	
	/**
	 * ����SQL ִ�����ݿ��ѯ,������ResultSetProcessor�����Ķ��� ���� Javadoc��
 	 * <P> ע�⣺ �˷��������ر�connection����Ҫ�����ֶ��ر� 
 	 * <p> �����˹ر�statement���߼��������α겻���õĴ���
	 * @param sql - 
	 *            ��ѯ��SQL
	 * @param connection - �õ������ӣ�һ����ʹ����ʱ����в�ѯʱ��Ҫ����connection
	 * @param processor - 
	 *            �����������
	 * @return - ����ִ�еĽ��
	 * @throws DAOException - ���ݷ��ʶ�����쳣
	 */
	public Object executeQuery(String sql, Connection connection, ResultSetProcessor processor) throws DAOException {
		PersistenceManager manager = null;
		Object value = null;
		manager = createPersistenceManager(connection);
		JdbcSession session = manager.getJdbcSession();
		try {
			value = session.executeQuery(sql, processor);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			session.closeOwnStatements();
//			session.closeAll();
//			if (manager != null)
//				manager.release();
		}
		return value;
	}

	/**
	 * ����ָ��SQL ִ���в��������ݿ��ѯ,������ResultSetProcessor�����Ķ���
	 * 
	 * @param sql
	 *            ��ѯ��SQL
	 * @param parameter
	 *            ��ѯ����
	 * @param processor
	 *            �����������
	 */
	public Object executeQuery(String sql, SQLParameter parameter, ResultSetProcessor processor) throws DAOException {
		PersistenceManager manager = null;
		Object value = null;
		manager = createPersistenceManager(dataSource);
		JdbcSession session = manager.getJdbcSession();
		try {
			value = session.executeQuery(sql, parameter, processor);
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			session.closeAll();
			if (manager != null)
				manager.release();
		}
		return value;
	}

	/**
	 * ����ָ��SQL ִ���в��������ݿ���²���
	 * 
	 * @param sql
	 *            ���µ�sql
	 * @param parameter
	 *            ���²���
	 * @return
	 * @throws DAOException
	 *             ���·��������׳�DAOException
	 */
	public int executeUpdate(String sql, SQLParameter parameter) throws DAOException {
		PersistenceManager manager = null;
		int value = 0;
		manager = createPersistenceManager(dataSource);
		JdbcSession session = manager.getJdbcSession();
		Connection connection = null;
		try {
			value = session.executeUpdate(sql, parameter);
			connection = session.getConnection();
			connection.commit();
		} catch (SQLException e) {
			rollBackConnection(connection, e);
		} catch (DAOException e) {
			rollBackConnection(connection, e);
		} finally {
			closeConnection(connection);
			if (manager != null)
				manager.release();
		}
		return value;
	}
	
	private void rollBackConnection(Connection connection, Exception e1) throws DAOException{
		try {
			connection.rollback();
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e);
		}
		Logger.error(e1.getMessage(), e1);
	}
	
	private void closeConnection(Connection connection) throws DAOException{
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e);
		}
	}
	
	
	/**
	 * �������ݿ���ʱ��
	 * @param tableName - ��ʱ������
	 * @param columns - ������
	 * @param idx - ��������
	 * @return
	 * @throws DAOException
	 */
	public String createTempTable(Connection connection, String tableName, String columns, String... idx) throws DAOException{
		try {
			TempTable tempTable = new TempTable();
			return tempTable.createTempTable(connection, tableName, columns, idx);
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * ����ָ��SQL ִ���޲��������ݿ���²���
	 * 
	 * @param sql
	 *            ���µ�sql
	 * @return
	 * @throws DAOException
	 *             ���·��������׳�DAOException
	 */
	public int executeUpdate(String sql) throws DAOException {
		PersistenceManager manager = null;
		int value =0;
		manager = createPersistenceManager(dataSource);
		JdbcSession session = manager.getJdbcSession();
		Connection connection = session.getConnection();
		try {
			value = session.executeUpdate(sql);
			connection.commit();
		} catch (SQLException e) {
			rollBackConnection(connection, e);
		} catch (DAOException e) {
			rollBackConnection(connection, e);
		} finally {
			closeConnection(connection);
			if (manager != null)
				manager.release();
		}
		return value;
	}


	/**
	 * �������Դ����
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @throws java.sql.SQLException
	 *             �쳣˵����
	 */
	public int getDBType() {
		return DataSourceCenter.getInstance().getDatabaseType(dataSource);
	}

	/**
	 * �������ݿ���صı���
	 * 
	 * @param dbType
	 *            int
	 * @param tableName
	 *            java.lang.String
	 * @return java.lang.String
	 * @since ��V1.00
	 */
	protected String getTableName(int dbType, String tableName) {
		String strTn = tableName;
		switch (dbType) {
		case DBConsts.POSTGRESQL:
			strTn = tableName;
			break;
		case DBConsts.ORACLE:
		case DBConsts.DB2:
			// ORACLE�轫������д
			strTn = tableName.toUpperCase();
			break;
		}
		return strTn;
	}

	/**
	 * �ж����ݱ��Ƿ����
	 * 
	 * @param tableName
	 *            ���ݱ�����
	 * @return
	 * @throws DAOException
	 *             �����׳�DAOException
	 */
	public boolean isTableExisted(String tableName) throws DAOException {
		if (tableName == null)
			throw new NullPointerException("TableName is null!");
		PersistenceManager manager = null;
		ResultSet rs = null;
		try {
			manager = createPersistenceManager(dataSource);
			int dbType = manager.getDBType();
			DatabaseMetaData dbmd = manager.getMetaData();
			if (dbType == DBConsts.ORACLE || dbType == DBConsts.DB2 || dbType == DBConsts.POSTGRESQL
					|| dbType == DBConsts.GBASE) {
				rs = dbmd.getTables(manager.getCatalog(), manager.getSchema(), getTableName(dbType, tableName),
						new String[] { "TABLE" });
			} else {
				rs = dbmd.getTables(null, null, getTableName(dbType, tableName), new String[] { "TABLE" });
			}
			while (rs.next()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			DBUtil.closeRs(rs);
			if (manager != null)
				manager.release();
		}
	}

	public int getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public void setAddTimeStamp(boolean addTimeStamp) {
		this.addTimestamp = addTimeStamp;
	}

	public boolean getAddTimeStamp() {
		return addTimestamp;
	}

	public PersistenceManager createPersistenceManager(String ds) throws DAOException {
		PersistenceManager manager = PersistenceManager.getInstance(ds);
		manager.setMaxRows(maxRows);
		return manager;
	}
	
	public PersistenceManager createPersistenceManager(Connection connection) throws DAOException {
		PersistenceManager manager = PersistenceManager.getInstance(connection);
		manager.setMaxRows(maxRows);
		return manager;
	}
	
//	public PersistenceManager createPersistenceManager() throws DAOException {
//		PersistenceManager manager = PersistenceManager.getInstance(dataSource);
//		manager.setMaxRows(maxRows);
//		return manager;
//	}

}
