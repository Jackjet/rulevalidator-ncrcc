package com.yonyou.nc.codevalidator.export.excel.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Migrate from origin system.
 * <p>
 * Content from excel rule. The rule content has header.
 * 
 * @author mazhqa
 * 
 */
public class ExcelRuleContent {

	/**
	 * excel first line is header.
	 */
	protected List<List<String>> header = new ArrayList<List<String>>();
	/**
	 * exclude header, others are content.
	 */
	protected List<List<List<String>>> content = new ArrayList<List<List<String>>>();

	protected List<String> searchHeader(int sheet, int column) {
		while (header.size() <= sheet) {
			header.add(new ArrayList<String>());
		}
		List<String> ls = header.get(sheet);
		while (ls.size() <= column) {
			ls.add("");
		}
		return ls;
	}

	public String getColumnName(int sheet, int column) {
		return searchHeader(sheet, column).get(column);
	}

	public void setColumnName(int sheet, int column, String value) {
		searchHeader(sheet, column).set(column, value);
	}

	public int getSheetCount() {
		return content.size();
	}

	public int getRowCount(int sheet) {
		return content.get(sheet).size();
	}

	public int getColumnCount(int sheet, int row) {
		return content.get(sheet).get(row).size();
	}

	public int getColumnCount(int sheet) {
		return getColumnCount(sheet, 0);
	}

	public String getValue(int sheet, int row, int column) {
		return searchCell(sheet, row, column).get(column);
	}

	public void setValue(int sheet, int row, int column, String value) {
		searchCell(sheet, row, column).set(column, value);
	}

	public String getValue(int sheet, int row, String column) {
		return getValue(sheet, row, searchColumn(sheet, column));
	}

	public void setValue(int sheet, int row, String column, String value) {
		setValue(sheet, row, searchColumn(sheet, column), value);
	}

	protected int searchColumn(int sheet, String column) {
		int i = -1;
		List<String> ls = header.get(sheet);
		for (String s : ls) {
			i++;
			if (column.equals(s)) {
				break;
			}
		}
		return i;
	}

	protected List<String> searchCell(int sheet, int row, int column) {
		while (content.size() <= sheet) {
			content.add(new ArrayList<List<String>>());
		}
		List<List<String>> ls = content.get(sheet);
		while (ls.size() <= row) {
			ls.add(new ArrayList<String>());
		}
		List<String> ls2 = ls.get(row);
		while (ls2.size() <= column) {
			ls2.add("");
		}
		return ls2;
	}
}
