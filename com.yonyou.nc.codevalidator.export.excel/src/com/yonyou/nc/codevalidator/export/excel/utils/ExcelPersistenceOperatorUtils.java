package com.yonyou.nc.codevalidator.export.excel.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * Excel格式输出文件的操作工具类
 * @author mazhqa
 * @since V2.1
 */
public final class ExcelPersistenceOperatorUtils {

	private ExcelPersistenceOperatorUtils() {

	}

	public static Map<String, ExcelDataSet> loadConfigExcel(File excelFile) throws ExcelOperateException {
		Map<String, ExcelDataSet> result = new LinkedHashMap<String, ExcelDataSet>();
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(excelFile);
			Sheet[] sheets = wb.getSheets();
			for (Sheet sheet : sheets) {
				// 列定义
				ExcelColumn[] cols = loadExcelColumn(sheet);
				Map<String, ExcelColumn> map = getColumnNameMap(cols);
				// 行数据
				ExcelRow[] rows = loadExcelRow(sheet, map);
				// 结果集
				DefaultExcelDataSet ers = new DefaultExcelDataSet();
				ers.setColumns(cols);
				ers.setRows(rows);
				ers.setIdentityName(sheet.getName());
				result.put(sheet.getName(), ers);
			}
		} catch (IOException e) {
			throw new ExcelOperateException("读取excel文件错误", e);
		} catch (BiffException e) {
			throw new ExcelOperateException("读取excel文件错误", e);
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
		return result;
	}

	public static Map<String, ExcelDataSet> loadConfigExcel(InputStream is) throws ExcelOperateException {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(is);
			Sheet[] sheets = wb.getSheets();
			Map<String, ExcelDataSet> result = new LinkedHashMap<String, ExcelDataSet>(sheets.length);
			for (Sheet sheet : sheets) {
				// 列定义
				ExcelColumn[] cols = loadExcelColumn(sheet);
				Map<String, ExcelColumn> map = getColumnNameMap(cols);
				// 行数据
				ExcelRow[] rows = loadExcelRow(sheet, map);

				// 结果集
				DefaultExcelDataSet ers = new DefaultExcelDataSet();
				ers.setColumns(cols);
				ers.setRows(rows);
				ers.setIdentityName(sheet.getName());
				result.put(sheet.getName(), ers);
			}
			return result;
		} catch (IOException e) {
			throw new ExcelOperateException("读取excel文件错误", e);
		} catch (BiffException e) {
			throw new ExcelOperateException("读取excel文件错误", e);
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
	}

	public static void saveExcel(File excelFile, ExcelDataSet[] rs) throws ExcelOperateException {
		WritableWorkbook wb = null;
		try {
			wb = Workbook.createWorkbook(excelFile);
			for (int i = 0; i < rs.length; i++) {
				String sheetName = StringUtils.isBlank(rs[i].getIdentityName()) ? "Sheet" + i : rs[i].getIdentityName();
				WritableSheet sheet = wb.createSheet(sheetName, i);
				setExcelRowSetToSheet(rs[i], sheet);
			}
			wb.write();
		} catch (IOException e) {
			throw new ExcelOperateException("保存excel文件错误", e);
		} catch (WriteException e) {
			throw new ExcelOperateException("保存excel文件错误", e);
		} finally {
			try {
				if(wb != null) {
					wb.close();
				}
			} catch (WriteException e) {
				throw new ExcelOperateException("保存excel文件错误", e);
			} catch (IOException e) {
				throw new ExcelOperateException("保存excel文件错误", e);
			}
		}
	}

	public static void saveExcel(OutputStream os, ExcelDataSet[] rs) throws ExcelOperateException {
		WritableWorkbook wb = null;
		try {
			wb = Workbook.createWorkbook(os);
			for (int i = 0; i < rs.length; i++) {
				String sheetName = StringUtils.isBlank(rs[i].getIdentityName()) ? "Sheet" + i : rs[i].getIdentityName();
				WritableSheet sheet = wb.createSheet(sheetName, i);
				setExcelRowSetToSheet(rs[i], sheet);
			}
			wb.write();
		} catch (IOException e) {
			throw new ExcelOperateException("保存excel文件错误", e);
		} catch (WriteException e) {
			try {
				if(wb != null) {
					wb.close();
				}
			} catch (WriteException e1) {
				throw new ExcelOperateException("保存excel文件错误", e);
			} catch (IOException e1) {
				throw new ExcelOperateException("保存excel文件错误", e);
			}
		}
	}

	private static void setExcelRowSetToSheet(ExcelDataSet rs, WritableSheet sheet) throws WriteException {
		// 写入列名
		int columnCount = rs.getColumnCount();
		ExcelColumn[] cols = rs.getColumns();
		for (int i = 0; i < columnCount; i++) {
			Label l = new Label(i, 0, cols[i].getColumnName());
			sheet.addCell(l);
		}
		// 写入单元格值
		int rowCount = rs.getRowCount();
		ExcelRow[] rows = rs.getRows();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				Label l = new Label(j, i + 1, rows[i].getValueByIndex(j));
				sheet.addCell(l);
			}
		}
	}

	private static ExcelColumn[] loadExcelColumn(Sheet sheet) {
		int columnCount = sheet.getRow(0).length;
		ExcelColumn[] cols = new ExcelColumn[columnCount];
		Cell[] cells = sheet.getRow(0);
		for (int i = 0; i < columnCount; i++) {
			DefaultExcelColumn col = new DefaultExcelColumn();
			col.setColumnIndex(i);
			col.setColumnName(cells[i].getContents());
			cols[i] = col;
		}
		return cols;
	}

	private static Map<String, ExcelColumn> getColumnNameMap(ExcelColumn[] cols) {
		Map<String, ExcelColumn> map = new HashMap<String, ExcelColumn>();
		for (ExcelColumn col : cols) {
			map.put(col.getColumnName(), col);
		}
		return map;
	}

	private static ExcelRow[] loadExcelRow(Sheet sheet, Map<String, ExcelColumn> map) {
		int rowCount = sheet.getRows();
		int columnCount = sheet.getColumns();
		List<ExcelRow> rows = new ArrayList<ExcelRow>(rowCount);
		for (int i = 1; i < rowCount; i++) {
			DefaultExcelRow row = new DefaultExcelRow(map);
			boolean nullRow = true;
			for (int j = 0; j < columnCount; j++) {
				Cell cell = sheet.getCell(j, i);
				String value = null;
				if (cell != null) {
					value = cell.getContents();
					if (value != null && value.length() > 0) {
						nullRow = false;
					}
				}
				row.setValueByIndex(j, value);
			}
			if (nullRow) {
				break;
			}
			rows.add(row);
		}
		return rows.toArray(new ExcelRow[rows.size()]);
	}

}
