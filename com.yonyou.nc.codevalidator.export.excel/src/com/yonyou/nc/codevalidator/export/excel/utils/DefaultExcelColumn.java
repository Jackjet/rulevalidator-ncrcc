package com.yonyou.nc.codevalidator.export.excel.utils;

public class DefaultExcelColumn implements ExcelColumn {
	private int columnIndex;

	private String columnName;

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public int getColumnIndex() {
		return this.columnIndex;
	}

	@Override
	public String getColumnName() {
		return this.columnName;
	}

	@Override
	public String toString() {
		return this.columnIndex + "=" + this.columnName;
	}

}