/*
 * 创建日期 2005-7-13
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.yonyou.nc.codevalidator.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * @author hey
 *         <p/>
 *         对象持久化管理器抽象类
 */
abstract public class PersistenceManager {
	protected int maxRows = 100000;

	/**
	 * 释放资源
	 */

	abstract public void release();

	/**
	 * 得到JdbcSession
	 * 
	 * @return 返回JdbcSession
	 */
	abstract public JdbcSession getJdbcSession();

	/**
	 * 得到数据库类型
	 * 
	 * @return
	 */

	abstract public int getDBType();

	/**
	 * 得到当前数据库的DatabaseMetaData
	 * 
	 * @return DatabaseMetaData
	 */
	public abstract DatabaseMetaData getMetaData();

	/**
	 * 得到当前数据库的Catalog
	 * 
	 * @return Catalog名称
	 */
	public abstract String getCatalog();

	/**
	 * 得到当前数据库的Schema
	 * 
	 * @return Schema名称
	 */
	public abstract String getSchema();

	// /**
	// * 根据默认数据源得到PersistenceManager实例
	// *
	// * @return
	// * @throws DAOException
	// * 如果出错则抛出DAOException
	// */
	// static public PersistenceManager getInstance() throws DAOException {
	// return new JdbcPersistenceManager();
	// }

	/**
	 * 根据传入的数据源参数得到PersistenceManager实例
	 * 
	 * @param dataSourceName
	 *            数据源名称
	 * @return
	 * @throws DAOException
	 *             如果出错则抛出DAOException
	 */
	static public PersistenceManager getInstance(String dataSourceName) throws DAOException {
		return new JdbcPersistenceManager(dataSourceName);

	}

	/**
	 * 根据传入的数据源参数得到PersistenceManager实例
	 * 
	 * @param connection
	 *            - 数据库连接
	 * @return
	 * @throws DAOException
	 *             如果出错则抛出DAOException
	 */
	static public PersistenceManager getInstance(Connection connection) throws DAOException {
		return new JdbcPersistenceManager(connection);

	}

	/**
	 * 根据传入的JdbcSession参数得到PersistenceManager实例
	 * 
	 * @param session
	 *            JdbcSession参数
	 * @return
	 * @throws DAOException
	 *             如果出错则抛出DAOException
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