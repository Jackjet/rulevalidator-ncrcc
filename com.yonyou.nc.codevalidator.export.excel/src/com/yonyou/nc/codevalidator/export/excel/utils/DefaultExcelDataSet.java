package com.yonyou.nc.codevalidator.export.excel.utils;

public class DefaultExcelDataSet implements ExcelDataSet {
	private ExcelColumn[] columns;

	private ExcelRow[] rows;

	private String identityName;

	public void setColumns(ExcelColumn[] columns) {
		this.columns = columns;
	}

	public void setRows(ExcelRow[] rows) {
		this.rows = rows;
	}

	@Override
	public ExcelColumn[] getColumns() {
		return this.columns;
	}

	@Override
	public int getColumnCount() {
		return columns != null ? this.columns.length : 0;
	}

	@Override
	public ExcelRow[] getRows() {
		return this.rows;
	}

	@Override
	public int getRowCount() {
		return rows != null ? this.rows.length : 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ExcelRow row : this.rows) {
			sb.append(", {");
			sb.append(row.toString());
			sb.append("}");
		}
		sb.delete(0, 1);
		return "[" + sb.toString() + "]";
	}

	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}

	@Override
	public String getIdentityName() {
		return this.identityName;
	}

}