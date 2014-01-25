package com.yonyou.nc.codevalidator.resparser.dataset;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * sql数据集
 * 
 * @author luoweid
 * 
 */
public class DataSet {

	private String name;

	protected Map<String, DataColumn> columns = new LinkedHashMap<String, DataColumn>();

	protected List<DataRow> rows = new ArrayList<DataRow>();

	public DataSet() {

	}

	public void createColumns(Class<?> colType, String... colNames) {
		if (colNames == null || colNames.length == 0) {
			return;
		}

		for (int i = 0; i < colNames.length; i++) {
			createColumn(colType, i, colNames[i]);
		}
	}

	public DataColumn createColumn(Class<?> colType, int index, String colName) {
		return new DataColumn(this, index, colName.toUpperCase(), colType);
	}

	/**
	 * 获取列信息
	 * 
	 * @return
	 */
	public List<DataColumn> getColumns() {
		return Collections.unmodifiableList(new ArrayList<DataColumn>(columns.values()));
	}

	public List<String> getColumnNames() {
		return new ArrayList<String>(columns.keySet());
	}

	/**
	 * 获取指定列名的列信息
	 * 
	 * @param colName
	 * @return
	 */
	public DataColumn getColumn(String colName) {
		return columns.get(colName.toUpperCase());
	}

	/**
	 * 获取行信息
	 * 
	 * @return
	 */
	public List<DataRow> getRows() {
		return Collections.unmodifiableList(rows);
	}

	/**
	 * 获取指定行号的行信息
	 * 
	 * @param index
	 * @return
	 */
	public DataRow getRow(int index) {
		return rows.get(index);
	}

	/**
	 * 获取行总数
	 * 
	 * @return
	 */
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * 获取指定cell值
	 * 
	 * @param index
	 *            行号
	 * @param columnName
	 *            列名
	 * @return
	 */
	public Object getValue(int index, String columnName) {
		String col = columnName.toUpperCase();
		enforceColumnName(col);
		return rows.get(index).getValue(col);
	}

	public DataRow appendRow() {
		final DataRow row = new DataRow(this);
		rows.add(row);
		return row;
	}

	/**
	 * 获取指定cell值
	 * 
	 * @param row
	 *            行对象
	 * @param col
	 *            列对象
	 * @return
	 */
	public Object getValue(DataRow row, DataColumn col) {
		if (row == null) {
			throw new IllegalArgumentException("A row must be given to getValue(row,col)");
		}
		if (col == null) {
			throw new IllegalArgumentException("A column must be given to getValue(row,col)");
		}
		if (row.getDataSet() != this) {
			String msg = "The given row is not from the {0} table";
			throw new IllegalArgumentException(MessageFormat.format(msg, new Object[] { name }));
		}

		if (col.getDataSet() != this) {
			String msg = "The given column is not from the {0} table";
			throw new IllegalArgumentException(MessageFormat.format(msg, new Object[] { name }));
		}
		return row.getValue(col);
	}

	private void enforceColumnName(String columnName) {
		if (!columns.containsKey(columnName)) {
			String msg = "Unknown Column \"{0]\" in table \"{1}\"";
			Object[] args = new Object[] { columnName, name };
			throw new IllegalArgumentException(MessageFormat.format(msg, args));
		}
	}

	public boolean isEmpty() {
		return rows.size() < 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("数据集列表:");
		for (DataRow dataRow : rows) {
			result.append(dataRow.toString()).append("\n");
		}
		return result.toString();
	}
}
