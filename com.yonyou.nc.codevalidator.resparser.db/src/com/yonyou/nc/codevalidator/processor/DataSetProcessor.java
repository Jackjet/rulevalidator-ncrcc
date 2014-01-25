package com.yonyou.nc.codevalidator.processor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;

/**
 * 专注处理DataSet的结果处理类
 * <p>
 * 加入处理当DataSet取的字段名称重复的处理逻辑
 * 
 * @author mazhqa
 * @since V1.0
 */
public class DataSetProcessor extends BaseProcessor {

	private static final long serialVersionUID = 1L;

	@Override
	public Object processResultSet(ResultSet rs) throws SQLException {
		DataSet ds = new DataSet();
		ResultSetMetaData metaData = rs.getMetaData();

		List<String> columnNames = getColumnNames(metaData);
		for (int i = 0; i < columnNames.size(); i++) {
			ds.createColumn(Object.class, i, columnNames.get(i));
		}

		while (rs.next()) {
			DataRow row = ds.appendRow();
			for (int i = 0; i < ds.getColumns().size(); i++) {
				row.setValue(ds.getColumns().get(i),
						ProcessorUtils.getColumnValue(metaData.getColumnType(i + 1), rs, i + 1));
			}
		}
		return ds;
	}

	/**
	 * 从ResultSetMetaData中获得对应的列名称字段列表
	 * <p>
	 *	注：如果有重复的字段，比如存在两个field，那么第一个为field第二个为1_field，依次类推
	 * 
	 * @param resultSetMetaData
	 * @return
	 * @throws SQLException
	 */
	private List<String> getColumnNames(ResultSetMetaData resultSetMetaData) throws SQLException {
		List<String> columnNames = new ArrayList<String>();
		Map<String, Integer> columnNameCountMap = new HashMap<String, Integer>();
		for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
			String columnName = resultSetMetaData.getColumnName(i + 1);
			Integer count = columnNameCountMap.get(columnName) == null ? Integer.valueOf(0) : columnNameCountMap
					.get(columnName);
			columnNames.add(count.equals(Integer.valueOf(0)) ? columnName : String.format("%s_%s", count, columnName));
			count++;
			columnNameCountMap.put(columnName, count);
		}
		return columnNames;
	}

}
