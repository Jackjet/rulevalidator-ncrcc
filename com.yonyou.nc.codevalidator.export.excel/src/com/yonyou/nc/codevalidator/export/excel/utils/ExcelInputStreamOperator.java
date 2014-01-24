package com.yonyou.nc.codevalidator.export.excel.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class ExcelInputStreamOperator {

	private InputStream excelInputStream;

	public ExcelInputStreamOperator(InputStream excelFile) throws RuleBaseException {
		this.excelInputStream = excelFile;
	}

	public Map<String, ExcelDataSet> loadConfigExcel() throws ExcelOperateException {
		Map<String, ExcelDataSet> result = new LinkedHashMap<String, ExcelDataSet>();
		try {
			Workbook wb = Workbook.getWorkbook(excelInputStream);
			Sheet[] sheets = wb.getSheets();
			for (Sheet sheet : sheets) {
				// 列定义
				ExcelColumn[] cols = this.loadExcelColumn(sheet);
				Map<String, ExcelColumn> map = this.getColumnNameMap(cols);

				// 行数据
				ExcelRow[] rows = this.loadExcelRow(sheet, map);

				// 结果集
				DefaultExcelDataSet ers = new DefaultExcelDataSet();
				ers.setColumns(cols);
				ers.setRows(rows);
				ers.setIdentityName(sheet.getName());
				result.put(sheet.getName(), ers);
			}
			wb.close();
		} catch (Exception e) {
			throw new ExcelOperateException("读取excel文件错误", e);
		}
		return result;
	}

	// public void saveExcel(ExcelDataSet[] rs) throws ExcelOperateException {
	// try {
	// WritableWorkbook wb = Workbook.createWorkbook(this.excelInputStream);
	// for (int i = 0; i < rs.length; i++) {
	// String sheetName = StringUtils.isBlank(rs[i].getIdentityName()) ? "Sheet"
	// + i
	// : rs[i].getIdentityName();
	// WritableSheet sheet = wb.createSheet(sheetName, i);
	// this.setExcelRowSetToSheet(rs[i], sheet);
	// }
	// wb.write();
	// wb.close();
	// } catch (Exception e) {
	// throw new ExcelOperateException("保存excel文件错误", e);
	// }
	// }
//
//	private void setExcelRowSetToSheet(ExcelDataSet rs, WritableSheet sheet) throws RowsExceededException,
//			WriteException {
//		// 写入列名
//		int columnCount = rs.getColumnCount();
//		ExcelColumn[] cols = rs.getColumns();
//		for (int i = 0; i < columnCount; i++) {
//			Label l = new Label(i, 0, cols[i].getColumnName());
//			sheet.addCell(l);
//		}
//		// 写入单元格值
//		int rowCount = rs.getRowCount();
//		ExcelRow[] rows = rs.getRows();
//		for (int i = 0; i < rowCount; i++) {
//			for (int j = 0; j < columnCount; j++) {
//				Label l = new Label(j, i + 1, rows[i].getValueByIndex(j));
//				sheet.addCell(l);
//			}
//		}
//	}

	private ExcelColumn[] loadExcelColumn(Sheet sheet) {
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

	private Map<String, ExcelColumn> getColumnNameMap(ExcelColumn[] cols) {
		Map<String, ExcelColumn> map = new HashMap<String, ExcelColumn>();
		for (ExcelColumn col : cols) {
			map.put(col.getColumnName(), col);
		}
		return map;
	}

	private ExcelRow[] loadExcelRow(Sheet sheet, Map<String, ExcelColumn> map) {
		int rowCount = sheet.getRows();
		int columnCount = sheet.getColumns();
		List<ExcelRow> rows = new ArrayList<ExcelRow>();
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
