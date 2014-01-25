package com.yonyou.nc.codevalidator.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

public class ProcessorUtils {

	/**
	 * 结果集转换成数组
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	static public Object[] toArray(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		Object[] result = new Object[cols];

		for (int i = 0; i < cols; i++) {
			result[i] = rs.getObject(i + 1);
		}

		return result;
	}

	/**
	 * 结果集转换成HashMap
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	static public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int cols = metaData.getColumnCount();
		Map<String, Object> rsValues = new HashMap<String, Object>();
		for (int i = 1; i <= cols; i++) {
			Object value = getColumnValue(metaData.getColumnType(i), rs, i);
			rsValues.put(metaData.getColumnName(i).toLowerCase(), value);
		}
		return rsValues;
	}



	/**
	 * 结果集转换成向量集合
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	static public Vector<?> toVector(ResultSet rs) throws SQLException {
		Vector<Vector<Object>> v = new Vector<Vector<Object>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int nColumnCount = rsmd.getColumnCount();

		while (rs.next()) {
			Vector<Object> tmpV = new Vector<Object>();
			for (int i = 1; i <= nColumnCount; i++) {
				Object o;
				if (rsmd.getColumnType(i) == Types.CHAR
						|| rsmd.getColumnType(i) == Types.VARCHAR) {
					o = rs.getString(i);

				} else {
					o = rs.getObject(i);
				}

				tmpV.addElement(o);
			}
			v.addElement(tmpV);
		}
		return v;
	}

	public static Object getColumnValue(int type, ResultSet resultSet, int i)
			throws SQLException {
		Object value;
		switch (type) {
		case Types.VARCHAR:
		case Types.CHAR:
			value = resultSet.getString(i);
			break;
		// case Types.INTEGER:
		// case Types.DECIMAL:
		// value = new Integer(resultSet.getInt(i));
		// break;
		case Types.BLOB:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
		case Types.BINARY:
			value = InOutUtil.deserialize(resultSet.getBytes(i));
			break;
		case Types.CLOB:
			value = getClob(resultSet, i);
			break;
		default:
			value = resultSet.getObject(i);
			break;
		}
		return value;
	}

	public static Object getColumnValue(int type, ResultSet resultSet,
			String colName) throws SQLException {
		Object value;
		switch (type) {
		case Types.VARCHAR:
		case Types.CHAR:
			value = resultSet.getString(colName);
			break;
		// case Types.INTEGER:
		// case Types.DECIMAL:
		// value = new Integer(resultSet.getInt(i));
		// break;
		case Types.BLOB:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
		case Types.BINARY:
			value = InOutUtil.deserialize(resultSet.getBytes(colName));
			break;
		case Types.CLOB:
			value = getClob(resultSet, colName);
			break;
		default:
			value = resultSet.getObject(colName);
			break;
		}
		return value;
	}

	/**
	 * @param rs
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	static public String getClob(ResultSet rs, int columnIndex)
			throws SQLException {
		Reader rsReader = null;
		BufferedReader reader = null;
		StringBuffer buffer = null;
		try {
			rsReader = rs.getCharacterStream(columnIndex);
			if (rsReader == null)
				return null;
			reader = new BufferedReader(rsReader);
			// Reader reader = rs.getCharacterStream(columnIndex);
			buffer = new StringBuffer();

			while (true) {
				String c = reader.readLine();
				if (c == null) {
					break;
				}
				buffer.append(c);
			}
		} catch (IOException e) {
			Logger.error("get clob content:"+e.getMessage(), e);
		} finally {
			try {
				if (rsReader != null) {
					rsReader.close();
				}
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}
			try {
				if (reader != null) {

					reader.close();
				}
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}

		}

		return buffer.toString();
	}

	static public String getClob(ResultSet rs, String colName)
			throws SQLException {
		Reader rsReader = null;
		BufferedReader reader = null;
		// Reader reader = rs.getCharacterStream(columnIndex);
		StringBuffer buffer = null;
		try {
			rsReader = rs.getCharacterStream(colName);
			if (rsReader == null)
				return null;
			reader = new BufferedReader(rsReader);
			buffer = new StringBuffer();
			while (true) {
				String c = reader.readLine();
				if (c == null) {
					break;
				}
				buffer.append(c);
			}
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		} finally {
			try {
				if (rsReader != null) {
					rsReader.close();
				}
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}
			try {
				if (reader != null) {

					reader.close();
				}
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}

		}
		return buffer.toString();
	}

	/**
	 * @param rs
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	static public byte[] getBlob(ResultSet rs, int columnIndex)
			throws SQLException {
		return rs.getBytes(columnIndex);
	}

}
