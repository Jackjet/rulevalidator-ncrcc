package com.yonyou.nc.codevalidator.export.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.export.api.AbstractPortionRulePersistence;
import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.export.excel.utils.DefaultExcelColumn;
import com.yonyou.nc.codevalidator.export.excel.utils.DefaultExcelDataSet;
import com.yonyou.nc.codevalidator.export.excel.utils.DefaultExcelRow;
import com.yonyou.nc.codevalidator.export.excel.utils.ExcelColumn;
import com.yonyou.nc.codevalidator.export.excel.utils.ExcelDataSet;
import com.yonyou.nc.codevalidator.export.excel.utils.ExcelPersistenceOperatorUtils;
import com.yonyou.nc.codevalidator.export.excel.utils.ExcelRow;
import com.yonyou.nc.codevalidator.export.excel.utils.ExcelRuleConstants;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class ExcelExportStrategy extends AbstractPortionRulePersistence {

	private static final int MAX_EXPORT_CHARACTERS = 1000;

	@Override
	public String batchExportResult(BusinessComponent businessComponent, List<IRuleExecuteResult> ruleResultList,RuleExportContext context)
			throws RuleBaseException {
		String outputFilePath = getRuleResultFilePath(businessComponent);
		File outputFile = new File(outputFilePath);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdir();
		}
		ExcelPersistenceOperatorUtils.saveExcel(outputFile, converToExcelDataSet(ruleResultList));
		return outputFilePath;
	}

	private ExcelDataSet[] converToExcelDataSet(List<IRuleExecuteResult> ruleExecResultList) {
		DefaultExcelDataSet excelDataSet = new DefaultExcelDataSet();
		String[] cols = ExcelRuleConstants.EXCELCOLSSTR;
		Map<String, ExcelColumn> columnNameToExcelColumnMap = new HashMap<String, ExcelColumn>();
		excelDataSet.setColumns(getExcelCols(cols, columnNameToExcelColumnMap));
		List<ExcelRow> excelRowList = new ArrayList<ExcelRow>(ruleExecResultList.size());
		Map<String, RuleDefinitionAnnotationVO> allRuleDefinitionMap = RuleDefinitionsReader.getInstance()
				.getAllRuleDefinitionMap();
		for (IRuleExecuteResult ruleExecuteResult : ruleExecResultList) {
			ExcelRow excelRow = new DefaultExcelRow(columnNameToExcelColumnMap);
			String ruleIdentifier = ruleExecuteResult.getRuleDefinitionIdentifier();
			RuleDefinitionAnnotationVO ruleDefinitionVO = allRuleDefinitionMap.get(ruleIdentifier);

			excelRow.setValueByName(ExcelRuleConstants.EXECUTE_LEVEL, ruleExecuteResult.getRuleExecuteContext()
					.getRuleConfigContext().getRuleExecuteLevel().getDisplayName());
			excelRow.setValueByName(ExcelRuleConstants.SERIAL_NO, ruleDefinitionVO.getSimpleIdentifier());
			excelRow.setValueByName(ExcelRuleConstants.CATELOG, ruleDefinitionVO.getCatalog().getName());
			excelRow.setValueByName(ExcelRuleConstants.SUB_CATELOG, ruleDefinitionVO.getSubCatalog().getName());
			excelRow.setValueByName(ExcelRuleConstants.DESCRIPTION, ruleDefinitionVO.getDescription());
			excelRow.setValueByName(ExcelRuleConstants.TEST_CLASSNAME, ruleIdentifier);
			excelRow.setValueByName(ExcelRuleConstants.SPECIAL_PARAMETER, ruleDefinitionVO.getSpecialParamStr());

			// 设置备注字段长度小于1000，防止值过大导致写入错误
			if (ruleExecuteResult.getNote() != null) {
				excelRow.setValueByName(ExcelRuleConstants.REMARK,
						ruleExecuteResult.getNote().length() > MAX_EXPORT_CHARACTERS ? ruleExecuteResult.getNote()
								.substring(0, MAX_EXPORT_CHARACTERS) + "..." : ruleExecuteResult.getNote());
			}
			excelRow.setValueByName(ExcelRuleConstants.ISPASS, ruleExecuteResult.getResult());
			excelRowList.add(excelRow);
		}
		excelDataSet.setRows(excelRowList.toArray(new ExcelRow[excelRowList.size()]));
		return new ExcelDataSet[] { excelDataSet };
	}

	private ExcelColumn[] getExcelCols(String[] cols, Map<String, ExcelColumn> colsMap) {
		ExcelColumn[] excelColumns = new DefaultExcelColumn[cols.length];
		for (int i = 0; i < cols.length; i++) {
			DefaultExcelColumn excelColumn = new DefaultExcelColumn();
			excelColumn.setColumnIndex(i);
			excelColumn.setColumnName(cols[i]);
			colsMap.put(cols[i], excelColumn);
			excelColumns[i] = excelColumn;
		}
		return excelColumns;
	}

	@Override
	public String getResultFolderName() {
		return "excel-result";
	}

	@Override
	public String getFileExtension() {
		return "xls";
	}

	// @Override
	// public String batchExportGlobalResult(List<IRuleExecuteResult>
	// ruleResultList) throws RuleBaseException {
	// SystemRuntimeContext systemRuntimeContext =
	// ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext().getSystemRuntimeContext();
	// String globalConfigFilePath =
	// systemRuntimeContext.getGlobalConfigFilePath();
	// File file = new File(globalConfigFilePath);
	// File topFolder = file.getParentFile();
	// File resultFolder = new File(topFolder, getResultFolderName());
	// if(!resultFolder.exists()){
	// try {
	// FileUtils.forceMkdir(resultFolder);
	// } catch (IOException e) {
	// Logger.error("创建文件夹操作时失败...");
	// throw new RuleBaseException(e);
	// }
	// }
	// String fileName = String.format("global_%1$tF_%1$tH-%1$tM-%1$tS.%2$s",
	// new Date(), getFileExtension());
	// File resultFile = new File(resultFolder, fileName);
	// ExcelPersistenceOperatorUtils.saveExcel(resultFile,
	// converToExcelDataSet(ruleResultList));
	// return resultFile.getAbsolutePath();
	// }

}
