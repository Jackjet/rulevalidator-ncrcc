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
 *         数据库访问帮助类封装了常用的持久层访问操作
 */

final public class BaseDAO {
	private String dataSource = null;

	int maxRows = 100000;

	boolean addTimestamp = true;

//	/**
//	 * 默认构造函数，将使用当前数据源
//	 */
//	public BaseDAO() {
//	}

	/**
	 * 有参构造函数，将使用指定数据源
	 * 
	 * @param dataSource
	 *            数据源名称
	 */
	public BaseDAO(String dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * 根据SQL 执行数据库查询,并返回ResultSetProcessor处理后的对象 （非 Javadoc）
	 * 
	 * @param sql
	 *            查询的SQL
	 * @param processor
	 *            结果集处理器
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
	 * 根据SQL 执行数据库查询,并返回ResultSetProcessor处理后的对象 （非 Javadoc）
 	 * <P> 注意： 此方法并不关闭connection，需要进行手动关闭 
 	 * <p> 增加了关闭statement的逻辑，避免游标不够用的错误
	 * @param sql - 
	 *            查询的SQL
	 * @param connection - 用到的连接，一般在使用临时表进行查询时需要缓存connection
	 * @param processor - 
	 *            结果集处理器
	 * @return - 返回执行的结果
	 * @throws DAOException - 数据访问对象的异常
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
	 * 根据指定SQL 执行有参数的数据库查询,并返回ResultSetProcessor处理后的对象
	 * 
	 * @param sql
	 *            查询的SQL
	 * @param parameter
	 *            查询参数
	 * @param processor
	 *            结果集处理器
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
	 * 根据指定SQL 执行有参数的数据库更新操作
	 * 
	 * @param sql
	 *            更新的sql
	 * @param parameter
	 *            更新参数
	 * @return
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
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
	 * 创建数据库临时表
	 * @param tableName - 临时表名称
	 * @param columns - 列名称
	 * @param idx - 索引序列
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
	 * 根据指定SQL 执行无参数的数据库更新操作
	 * 
	 * @param sql
	 *            更新的sql
	 * @return
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
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
	 * 获得数据源类型
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @throws java.sql.SQLException
	 *             异常说明。
	 */
	public int getDBType() {
		return DataSourceCenter.getInstance().getDatabaseType(dataSource);
	}

	/**
	 * 返回数据库相关的表名
	 * 
	 * @param dbType
	 *            int
	 * @param tableName
	 *            java.lang.String
	 * @return java.lang.String
	 * @since ：V1.00
	 */
	protected String getTableName(int dbType, String tableName) {
		String strTn = tableName;
		switch (dbType) {
		case DBConsts.POSTGRESQL:
			strTn = tableName;
			break;
		case DBConsts.ORACLE:
		case DBConsts.DB2:
			// ORACLE需将表名大写
			strTn = tableName.toUpperCase();
			break;
		}
		return strTn;
	}

	/**
	 * 判断数据表是否存在
	 * 
	 * @param tableName
	 *            数据表名称
	 * @return
	 * @throws DAOException
	 *             出错抛出DAOException
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
