package com.yonyou.nc.codevalidator.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.type.DBConsts;

/**
 * User: 贺扬<br>
 * Date: 2005-6-24 Time: 10:56:48<br>
 * 对象持久化管理器
 */
public class JdbcPersistenceManager extends PersistenceManager {
	// 数据库会话
	JdbcSession session;

	// 数据源名称
	String dataSource = null;

	private DatabaseMetaData dbmd = null;

	private static Map<String, ColCache> colCacheMap = new ConcurrentHashMap<String, ColCache>();

	class ColCache {
		private Map<String, Map<String, Integer>> typeCache = new ConcurrentHashMap<String, Map<String, Integer>>();
		private Map<String, Map<String, Integer>> sizeCache = new ConcurrentHashMap<String, Map<String, Integer>>();;
	}

	/**
	 * 无参数构造函数
	 * 
	 * @throws DAOException
	 */
	protected JdbcPersistenceManager() throws DAOException {
		init();
	}

	/**
	 * 有参数构造函数
	 * 
	 * @param dataSource
	 *            数据源名称
	 * @throws DAOException
	 *             如果获得连接发生错误则抛出异常
	 */
	protected JdbcPersistenceManager(String dataSource) throws DAOException {
		this.dataSource = dataSource;
		init();
	}

	protected JdbcPersistenceManager(Connection connection) throws DAOException {
		init(connection);
	}
	
	protected JdbcPersistenceManager(JdbcSession session) {
		session.setMaxRows(maxRows);
		this.session = session;
	}

	/**
	 * 得到JdbcSession
	 * 
	 * @return 返回JdbcSession
	 */
	public JdbcSession getJdbcSession() {
		return session;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		if (dbmd != null)
			dbmd = null;
		if (session != null) {
			session.closeAll();
			session = null;
		}

	}


//	private void addParameter(SQLParameter parameter, SQLParameter addParams) {
//		if (addParams != null)
//			for (int i = 0; i < addParams.getCountParams(); i++) {
//				parameter.addParam(addParams.get(i));
//			}
//	}
//
//
//
//	private String appendOrderBy(String sql, String[] orderBys) {
//		if (sql == null) {
//			throw new RuntimeException("sql is null");
//		}
//
//		if (orderBys == null || orderBys.length == 0) {
//			return sql;
//		}
//
//		StringBuffer orderClause = new StringBuffer(" ORDER BY ");
//
//		int len = orderClause.length();
//
//		for (String s : orderBys) {
//			if (s != null) {
//				orderClause.append(s).append(',');
//			}
//		}
//
//		if (orderClause.length() > len) {
//			orderClause.setLength(orderClause.length() - 1);
//
//			return sql + orderClause;
//		} else {
//			return sql;
//		}
//
//	}

	public int getDBType() {
		return session.getDbType();
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public DatabaseMetaData getMetaData() {
		if (dbmd == null)
			dbmd = getJdbcSession().getMetaData();
		return dbmd;
	}

	public String getCatalog() {
		String catalog = null;
		switch (getDBType()) {
		case DBConsts.GBASE:
		case DBConsts.POSTGRESQL:
			try {
				catalog = getConnection().getCatalog();
			} catch (SQLException e) {
			}
			break;
		case DBConsts.SQLSERVER:
		case DBConsts.DB2:
			// null means drop catalog name from the selection criteria
			catalog = null;
			break;
		case DBConsts.ORACLE:
			// "" retrieves those without a catalog
			catalog = "";
			break;
		}
		return catalog;
	}

	public String getSchema() {
		String strSche = null;
		try {
			String schema = getMetaData().getUserName();
			switch (getDBType()) {
			case DBConsts.POSTGRESQL:
				strSche = null;
				break;
			case DBConsts.SQLSERVER:
				strSche = "dbo";
				break;
			case DBConsts.ORACLE:
			case DBConsts.DB2: {
				if (schema == null || schema.length() == 0)
					throw new IllegalArgumentException("ORACLE Database mode does not allow to be null!!");
				// ORACLE需将模式名大写
				strSche = schema.toUpperCase();
				break;
			}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return strSche;
	}

	/**
	 * 初始化数据库会话连接
	 * 
	 * @throws DAOException
	 */
	private void init() throws DAOException {
		if (dataSource == null)
			session = new JdbcSession();
		else
			session = new JdbcSession(dataSource);

		session.setMaxRows(maxRows);
	}
	
	private void init(Connection connection) throws DAOException {
		session = new JdbcSession(connection);
		session.setMaxRows(maxRows);
	}

//	private void isNull(Object vo) {
//		if (vo == null) {
//			throw new IllegalArgumentException("vo object parameter is null!!");
//		}
//	}

	public void setMaxRows(int maxRows) {
		super.setMaxRows(maxRows);
		session.setMaxRows(maxRows);
	}


	public Connection getConnection() {
		if (session != null)
			return session.getConnection();
		return null;
	}

//	private ColCache getColCache() {
//		String ds = ds();
//		synchronized (colCacheMap) {
//			ColCache colCache = colCacheMap.get(ds);
//			if (colCache == null) {
//				colCache = new ColCache();
//				colCacheMap.put(ds, colCache);
//			}
//			return colCache;
//		}
//	}

//	private String ds() {
//		return dataSource == null ? DataSourceCenter.getInstance()
//				.getSourceName() : dataSource;
//	}

//	private Map<String, Integer> getColmnSize(String table) throws DAOException {
//		ColCache cache = getColCache();
//
//		Map<String, Integer> result = cache.sizeCache.get(table);
//		if (result == null || result.size() == 0) {
//			result = new HashMap<String, Integer>();
//			ResultSet rsColumns = null;
//			try {
//				if (getDBType() == DBConsts.SQLSERVER && table.startsWith("#")) {
//					Statement stmt = null;
//					try {
//						stmt = getConnection().createStatement();
//						rsColumns = stmt.executeQuery("select top 0 * from "
//								+ table);
//						ResultSetMetaData rsMeta = rsColumns.getMetaData();
//						int count = rsMeta.getColumnCount();
//
//						for (int i = 1; i < count + 1; i++) {
//							result.put(rsMeta.getColumnName(i).toUpperCase(),
//									rsMeta.getPrecision(i));
//						}
//						if (result.size() >= 0) {
//							cache.sizeCache.put(table, result);
//						} else {
//							throw new DAOException("no column info for: "
//									+ table + " at datasource: " + ds());
//
//						}
//
//						return result;
//					} finally {
//						if (stmt != null) {
//							stmt.close();
//						}
//					}
//				} else {
//					DatabaseMetaData dmd = getConnection().getMetaData();
//					if (getDBType() == DBConsts.ORACLE
//							|| getDBType() == DBConsts.DB2) {
//						rsColumns = dmd.getColumns(null, dmd.getUserName()
//								.toUpperCase(), table.toUpperCase(), "%");
//					} else if (getDBType() == DBConsts.POSTGRESQL
//							|| getDBType() == DBConsts.GBASE) {
//						rsColumns = dmd.getColumns(getCatalog(),
//								dmd.getUserName(), table, "%");
//					} else {
//						rsColumns = dmd.getColumns(null, null,
//								table.toUpperCase(), "%");
//					}
//					while (rsColumns.next()) {
//						result.put(rsColumns.getString("COLUMN_NAME")
//								.toUpperCase(), rsColumns.getInt("COLUMN_SIZE"));
//					}
//
//					if (result.size() >= 0) {
//						cache.sizeCache.put(table, result);
//					} else {
//						throw new DAOException("no column info for: "
//								+ table + " at datasource: " + ds());
//
//					}
//				}
//			} catch (SQLException e) {
//				Logger.error("get table metadata error", e);
//				throw new DAOException(e);
//			} finally {
//				DBUtil.closeRs(rsColumns);
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 得到列的类型
//	 * 
//	 * @param table
//	 * @return
//	 */
//	private Map<String, Integer> getColmnTypes(String table) throws DAOException {
//
//		ColCache cache = getColCache();
//
//		Map<String, Integer> result = cache.typeCache.get(table);
//		// 同时查出列的长度信息
//		Map<String, Integer> sizeMap = cache.sizeCache.get(table);
//		if (result == null || result.size() == 0) {
//			boolean querySize = false;
//			if (sizeMap == null) {
//				sizeMap = new HashMap<String, Integer>();
//				querySize = true;
//			}
//			Map<String, Integer> typeMap = new HashMap<String, Integer>();
//			ResultSet rsColumns = null;
//			try {
//				if (getDBType() == DBConsts.SQLSERVER && table.startsWith("#")) {
//					Statement stmt = null;
//					try {
//						stmt = getConnection().createStatement();
//						rsColumns = stmt.executeQuery("select top 0 * from "
//								+ table);
//						ResultSetMetaData rsMeta = rsColumns.getMetaData();
//						int count = rsMeta.getColumnCount();
//
//						for (int i = 1; i < count + 1; i++) {
//							typeMap.put(rsMeta.getColumnName(i),
//									rsMeta.getColumnType(i));
//							if (querySize) {
//								sizeMap.put(rsMeta.getColumnName(i)
//										.toUpperCase(), rsMeta.getPrecision(i));
//							}
//						}
//						if (typeMap.size() > 0) {
//							cache.typeCache.put(table, typeMap);
//							cache.sizeCache.put(table, sizeMap);
//						} else {
//							throw new DAOException("no column info for: "
//									+ table + " at datasource: " + ds());
//
//						}
//
//						return typeMap;
//					} finally {
//						if (stmt != null) {
//							stmt.close();
//						}
//					}
//
//				} else {
//
//					if (getDBType() == DBConsts.SQLSERVER)
//						rsColumns = getMetaData().getColumns(null, null,
//								table.toUpperCase(), "%");
//					else if (getDBType() == DBConsts.POSTGRESQL)
//						rsColumns = getMetaData().getColumns(null, null,
//								table.toLowerCase(), "%");
//					else
//						rsColumns = getMetaData().getColumns(null, getSchema(),
//								table.toUpperCase(), "%");
//					while (rsColumns.next()) {
//						String columnName = rsColumns.getString("COLUMN_NAME")
//								.toUpperCase();
//						int columnType = rsColumns.getShort("DATA_TYPE");
//						typeMap.put(columnName, columnType);
//						if (querySize) {
//							sizeMap.put(rsColumns.getString("COLUMN_NAME")
//									.toUpperCase(), rsColumns
//									.getInt("COLUMN_SIZE"));
//						}
//					}
//
//					if (typeMap.size() > 0) {
//						cache.typeCache.put(table, typeMap);
//						cache.sizeCache.put(table, sizeMap);
//					} else {
//						throw new DAOException("no column info for: "
//								+ table + " at datasource: " + ds());
//
//					}
//
//					return typeMap;
//				}
//			} catch (SQLException e) {
//				Logger.error("get table metadata error", e);
//				throw new DAOException(e);
//			} finally {
//				DBUtil.closeRs(rsColumns);
//			}
//		}
//		return result;
//	}

	public static void clearColumnTypes(String table) {
		if (colCacheMap.size() == 0) {
			return;
		}
		for (ColCache colCache : colCacheMap.values()) {
			colCache.typeCache.remove(table);
			colCache.sizeCache.remove(table);
		}
	}


}
