package com.yonyou.nc.codevalidator.resparser.dataset;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class DataRow {

	private DataSet dataSet;

	private Map<DataColumn, DataCell> cells = new HashMap<DataColumn, DataCell>();

	public DataRow(DataSet dataSet) {
		this.dataSet = dataSet;
		for (DataColumn col : this.dataSet.getColumns()) {
			addCell(col);
		}
	}

	protected DataCell addCell(DataColumn col) {
		DataCell cell = cells.get(col);
		if (cell == null) {
			cell = new DataCell(col);
			cells.put(col, cell);
		}
		return cell;
	}

	public DataRow setValue(String colName, Object value) {
		return setValue(getColumnFromTable(colName), value);
	}

	public DataRow setValue(DataColumn col, Object value) {
		DataCell cell = getCell(col);
		cell.setValue(col, value);
		return this;
	}

	public Object getValue(String colName) {
		return getValue(getColumnFromTable(colName));
	}

	public Object getValue(DataColumn col) {
		return getCell(col).value;
	}
	
	/**
	 * 判断某列名是否在dataRow中存在
	 * @param colName
	 * @return
	 */
	public boolean hasColumn(String colName) {
		DataColumn col = this.dataSet.getColumn(colName);
		return col != null;
	}

	protected DataCell getCell(DataColumn col) {
		if (col == null) {
			throw new IllegalArgumentException("A DataColumn is required");
		}
		DataCell cell = cells.get(col);
		if (cell == null && col.getDataSet() == this.dataSet) {
			cell = addCell(col);
		}
		return cell;
	}

	private DataColumn getColumnFromTable(String colName) {
		DataColumn col = this.dataSet.getColumn(colName);
		if (col != null) {
			return col;
		}
		String msg = "Unknown Column \"{0}\" in table \"{1}\"";
		Object[] args = new Object[] { colName, this.dataSet.getName() };
		throw new IllegalArgumentException(MessageFormat.format(msg, args));
	}

	DataSet getDataSet() {
		return dataSet;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<DataColumn, DataCell> entry : cells.entrySet()) {
			result.append(entry.getKey().getName()).append(":").append(entry.getValue().getValue()).append(" ");
		}
		return result.toString();
	}

	private static final class DataCell {
		Object value;

		// /**
		// * Instantiates a cell for a given column.
		// *
		// * @param col
		// * The DataColumn for which to create a cell.
		// */
		// DataCell(DataColumn col, Object value) {
		// this.value = value;
		// }

		DataCell(DataColumn col) {
			this.value = col;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(DataColumn col, Object newValue) {
			this.value = newValue;
		}

	}

}
