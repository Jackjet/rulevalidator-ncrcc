package com.yonyou.nc.codevalidator.export.excel.utils;

public interface ExcelDataSet {
	
	String getIdentityName();
	
	ExcelColumn[] getColumns();

	int getColumnCount();

	ExcelRow[] getRows();

	int getRowCount();

}
