package com.yonyou.nc.codevalidator.resparser.dataset;

import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

public class DataColumn {

	private int index;

	private DataSet dataSet;

	private String name;

	private Class<?> type = Object.class;

	private boolean readOnly = true;

	private boolean keyColumn = false;

	public DataColumn(DataSet table, int index, String name) {
		this.dataSet = table;
		this.index = index;
		this.name = name;
		this.type = String.class;
	}

	public DataColumn(DataSet dataSet, int index, String name, Class<?> type) {
		this.dataSet = dataSet;
		this.index = index;
		this.name = name;
		this.type = type;
		dataSet.columns.put(name, this);
	}

	public void setReadOnly(boolean readOnly) {
		if (this.readOnly != readOnly) {
			this.readOnly = readOnly;
		}
	}

	protected boolean isKeyColumn() {
		return keyColumn;
	}

	protected void setKeyColumn(boolean keyColumn) {
		this.keyColumn = keyColumn;
	}

	protected DataSet getDataSet() {
		return dataSet;
	}

	protected int getIndex() {
		return index;
	}

	protected String getName() {
		return name;
	}

	protected Class<?> getType() {
		return type;
	}

	public String toString() {
		return StringUtils.isBlank(getDataSet().getName()) ? "Column " + getName() : "Column " + getDataSet().getName()
				+ "." + getName();
	}

}
