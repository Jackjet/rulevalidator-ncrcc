package com.yonyou.nc.codevalidator.export.excel.utils;

public interface ExcelRow {
	
	String getValueByIndex(int columnIndex);
	
	String getValueByName(String columnName);
	
	void setValueByIndex(int columnIndex, String value);
	
	void setValueByName(String columnName, String value);
}
