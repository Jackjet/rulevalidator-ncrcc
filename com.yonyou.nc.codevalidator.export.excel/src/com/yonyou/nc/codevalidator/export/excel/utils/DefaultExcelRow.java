package com.yonyou.nc.codevalidator.export.excel.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultExcelRow implements ExcelRow {
	private Map<Integer, String> values = new HashMap<Integer, String>();

	private Map<String, ExcelColumn> columns;

	public DefaultExcelRow(Map<String, ExcelColumn> columns) {
		this.columns = columns;
	}

	@Override
	public String getValueByIndex(int columnIndex) {
		return this.values.get(Integer.valueOf(columnIndex));
	}

	@Override
	public String getValueByName(String columnName) {
		ExcelColumn col = this.columns.get(columnName);
		return this.getValueByIndex(col.getColumnIndex());
	}

	@Override
	public void setValueByIndex(int columnIndex, String value) {
		this.values.put(Integer.valueOf(columnIndex), value);
	}

	@Override
	public void setValueByName(String columnName, String value) {
		ExcelColumn col = this.columns.get(columnName);
		this.values.put(Integer.valueOf(col.getColumnIndex()), value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Collection<ExcelColumn> cols = this.columns.values();
		for (ExcelColumn col : cols) {
			sb.append(", ");
			sb.append(col.getColumnName());
			sb.append("=");
			sb.append(this.getValueByIndex(col.getColumnIndex()));
		}
		sb.delete(0, 1);
		return sb.toString();
	}

}