package com.yonyou.nc.codevalidator.resparser.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.connection.PoolableConnection;
import com.yonyou.nc.codevalidator.crossdb.CrossDBConnection;
import com.yonyou.nc.codevalidator.dao.BaseDAO;
import com.yonyou.nc.codevalidator.dao.BaseDAOManager;
import com.yonyou.nc.codevalidator.dao.DAOException;
import com.yonyou.nc.codevalidator.dao.JdbcSession;
import com.yonyou.nc.codevalidator.dao.PersistenceManager;
import com.yonyou.nc.codevalidator.dao.SQLParameter;
import com.yonyou.nc.codevalidator.datasource.DataSourceCenter;
import com.yonyou.nc.codevalidator.datasource.SingleTxwareDataSource;
import com.yonyou.nc.codevalidator.datasource.SingleTxwareDataSource.SingleTxConnection;
import com.yonyou.nc.codevalidator.processor.ArrayListProcessor;
import com.yonyou.nc.codevalidator.processor.BaseProcessor;
import com.yonyou.nc.codevalidator.processor.DataSetProcessor;
import com.yonyou.nc.codevalidator.processor.MapListProcessor;
import com.yonyou.nc.codevalidator.resparser.IScriptResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.TableColumn;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.type.DBConsts;

public class ScriptResourceQueryFactory implements IScriptResourceQueryFactory {

	@Override
	public ResourceType getType() {
		return ResourceType.SCRIPT;
	}

	private BaseDAO getCurrentBaseDao(ScriptResourceQuery scriptResourceQuery) {
		String dataSource = scriptResourceQuery.getRuntimeContext().getDataSource();
		return BaseDAOManager.getInstance().getBaseDAO(dataSource);
	}

	private BaseDAO getCurrentBaseDao() throws RuleBaseException {
		String dataSource = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext()
				.getDataSource();
		return BaseDAOManager.getInstance().getBaseDAO(dataSource);
	}

	@Override
	public List<ScriptResource> getResource(ScriptResourceQuery resourceQuery) throws ResourceParserException {
		List<ScriptResource> result = new ArrayList<ScriptResource>();
		String queryString = resourceQuery.getQueryString();
		try {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> queryResultList = (List<Map<String, Object>>) this.getCurrentBaseDao(
					resourceQuery).executeQuery(queryString, new MapListProcessor());
			for (Map<String, Object> queryResult : queryResultList) {
				result.add(new ScriptResource(queryResult));
			}
			return result;
		} catch (DAOException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public ScriptDataSetResource getResourceAsDataSet(ScriptResourceQuery resourceQuery) throws ResourceParserException {
		Map<String, Object> result = new HashMap<String, Object>();
		String queryString = resourceQuery.getQueryString();
		try {
			DataSet queryResult = (DataSet) this.getCurrentBaseDao(resourceQuery).executeQuery(queryString,
					new DataSetProcessor());
			result.put(ScriptDataSetResource.DEFAULTDATASETNAME, queryResult);
			return new ScriptDataSetResource(result);
		} catch (DAOException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public ScriptDataSetResource getResourceAsDataSetWithConnection(ScriptResourceQuery resourceQuery,
			Connection connection) throws ResourceParserException {
		Map<String, Object> result = new HashMap<String, Object>();
		String queryString = resourceQuery.getQueryString();
		try {
			BaseDAO currentBaseDao = this.getCurrentBaseDao(resourceQuery);
			DataSet queryResult = (DataSet) currentBaseDao
					.executeQuery(queryString, connection, new DataSetProcessor());
			result.put(ScriptDataSetResource.DEFAULTDATASETNAME, queryResult);
			return new ScriptDataSetResource(result);
		} catch (DAOException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public String createTempTable(Connection connection, String tableName, String columns, String... idx)
			throws ResourceParserException {
		try {
			return this.getCurrentBaseDao().createTempTable(connection, tableName, columns, idx);
		} catch (DAOException e) {
			throw new ResourceParserException(e);
		} catch (RuleBaseException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public void insertDataToTempTable(String tempTableName, List<String> columnNames, List<Object[]> dataArrays,
			Connection connection) throws ResourceParserException {
		try {
			PersistenceManager manager = PersistenceManager.getInstance(connection);
			JdbcSession session = manager.getJdbcSession();
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("insert into ").append(tempTableName).append(" (");
			StringBuilder replaceBuilder = new StringBuilder();
			for (int i = 0; i < columnNames.size(); i++) {
				if (i != 0) {
					sqlBuilder.append(",");
					replaceBuilder.append(",");
				}
				sqlBuilder.append(columnNames.get(i));
				replaceBuilder.append("?");
			}
			sqlBuilder.append(") values(").append(replaceBuilder.toString()).append(")");
			for (Object[] dataArray : dataArrays) {
				SQLParameter param = new SQLParameter();
				for (Object dataParam : dataArray) {
					if (dataParam == null) {
						param.addParam("null");
					} else {
						param.addParam(dataParam);
					}
				}
				session.addBatch(sqlBuilder.toString(), param);
			}
			session.executeBatch();
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new ResourceParserException(e.getMessage(), e);
		}
	}

	@Override
	public Connection createTempConnection() throws ResourceParserException {
		try {
			String dataSource = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext()
					.getDataSource();
			PersistenceManager manager = PersistenceManager.getInstance(dataSource);
			JdbcSession session = manager.getJdbcSession();
			return session.getConnection();
		} catch (RuleBaseException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public void closeConnection(Connection connection) throws ResourceParserException {
		try {
			if (connection != null) {
				connection.close();
			}
			if (connection instanceof CrossDBConnection) {
				CrossDBConnection crossDBConnection = (CrossDBConnection) connection;
				Connection pConnection = crossDBConnection.getPConnection();

				if (pConnection instanceof SingleTxConnection) {
					SingleTxConnection singleTxConnection = (SingleTxConnection) pConnection;
					Connection originalConn = singleTxConnection.getOriginalConn();
					if (originalConn instanceof PoolableConnection) {
						PoolableConnection poolableConnection = (PoolableConnection) originalConn;
						poolableConnection.reallyClose();
					}
				}
			}
			
			//clamaa: 同时清理DataSourceCenter中的缓存
			DataSourceCenter.getInstance().clearCache();
			SingleTxwareDataSource.clearPool();
		} catch (SQLException e) {
			throw new ResourceParserException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> queryMetaDataClassByFuncodes(String sql, String[] funcodes, String dataSource)
			throws ResourceParserException {
		List<String> resultList = new ArrayList<String>();
		try {
			resultList = (List<String>) BaseDAOManager.getInstance().getBaseDAO(dataSource)
					.executeQuery(sql, new BaseProcessor() {

						private static final long serialVersionUID = 5388906818779244706L;

						@Override
						public List<String> processResultSet(ResultSet rs) throws SQLException {
							List<String> result = new ArrayList<String>();
							while (rs.next()) {
								result.add(rs.getString(1));
							}
							return result;
						}

					});
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new ResourceParserException(e.getMessage(), e);
		}
		return resultList;
	}

	@Override
	public ScriptDataSetResource getResourceAsDataSet(String queryString, String dataSourceName)
			throws ResourceParserException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			DataSet queryResult = (DataSet) this.getCurrentBaseDao(dataSourceName).executeQuery(queryString,
					new DataSetProcessor());
			result.put(ScriptDataSetResource.DEFAULTDATASETNAME, queryResult);
			return new ScriptDataSetResource(result);
		} catch (DAOException e) {
			throw new ResourceParserException(e);
		}
	}

	private BaseDAO getCurrentBaseDao(String dataSource) {
		return BaseDAOManager.getInstance().getBaseDAO(dataSource);
	}

	@Override
	public List<TableColumn> getTableColumns(String tableName, String dataSourceName) throws ResourceParserException {
		BaseDAO currentBaseDao = getCurrentBaseDao(dataSourceName);
		String sql = null;
		switch (currentBaseDao.getDBType()) {
		case DBConsts.ORACLE:
			sql = getOracleTableSQL(tableName);
			break;
		case DBConsts.SQLSERVER:
			sql = getSQLSeverTableSQL(tableName);
			break;
		case DBConsts.DB2:
			sql = getDB2TableSQL(tableName);
			break;
		case DBConsts.POSTGRESQL:
			sql = getPostGreTableSQL(tableName);
			break;
		default:
			break;
		}
		try {
			List<TableColumn> tableColumnList = new ArrayList<TableColumn>();
			@SuppressWarnings("unchecked")
			List<Object[]> objList = (List<Object[]>) currentBaseDao.executeQuery(sql, new ArrayListProcessor());
			for (Object[] objects : objList) {
				TableColumn tableColumn = new TableColumn();
				tableColumn.setColumnName((String) objects[0]);
				tableColumn.setColumnType((String) objects[1]);
				tableColumn.setLength((Integer) objects[2]);
				tableColumnList.add(tableColumn);
			}
			return tableColumnList;
		} catch (DAOException e) {
			throw new ResourceParserException(e);
		}
	}

	private String getOracleTableSQL(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("select column_name as columnName,       ");
		sb.append("       data_type   as columnType,       ");
		sb.append("       data_length as length           ");
		// sb.append("       nullable    as nullable,         ");
		sb.append("  from user_tab_columns                 ");
		sb.append(" where table_name = '" + tableName.toUpperCase() + "' ");
		return sb.toString();
	}

	private String getSQLSeverTableSQL(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.name as columnName,                 ");
		sb.append("       c.name as columnType,                 ");
		sb.append("       b.length as length                   ");
		sb.append("  FROM sysobjects a,syscolumns b,systypes c  ");
		sb.append(" WHERE a.id = b.id                           ");
		sb.append("   AND a.xtype = 'U'                         ");
		sb.append("   AND b.xtype=c.xtype                       ");
		sb.append("   AND a.name = '" + tableName + "'          ");
		return sb.toString();
	}

	private String getDB2TableSQL(String tableName) {
		StringBuilder sb = new StringBuilder();
		// TODO: 未完成
		return sb.toString();
	}

	private String getPostGreTableSQL(String tableName) {
		StringBuilder sb = new StringBuilder();
		// TODO: 未完成
		return sb.toString();
	}

}
