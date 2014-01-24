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
 * רע����DataSet�Ľ��������
 * <p>
 * ���봦��DataSetȡ���ֶ������ظ��Ĵ����߼�
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
	 * ��ResultSetMetaData�л�ö�Ӧ���������ֶ��б�
	 * <p>
	 *	ע��������ظ����ֶΣ������������field����ô��һ��Ϊfield�ڶ���Ϊ1_field����������
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
