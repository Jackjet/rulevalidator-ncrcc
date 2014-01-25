package com.yonyou.nc.codevalidator.resparser.dbinit;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.temp.AbstractTempTableResourceCreator;
import com.yonyou.nc.codevalidator.resparser.temp.TempTableExecContextOperator;
import com.yonyou.nc.codevalidator.rule.CreatorConstants;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.utils.FileLocatorUtils;

/**
 * 预置脚本资源分析器，用于分析预置脚本的内容
 * @author mazhqa
 * @since V2.7.0
 */
public class DbInitResourceCreator extends AbstractTempTableResourceCreator {

	public static final String NCSCRIPT = "ncscript";

	@Override
	public void createTempResources(Connection connection) throws RuleBaseException {
		Logger.info("开始进行预置脚本初始化...");
		RuntimeContext currentRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper()
				.getCurrentRuntimeContext();
		String ncHome = currentRuntimeContext.getNcHome();
		File ncscriptFile = new File(ncHome, NCSCRIPT);
		Collection<File> dbInitFolder = FileLocatorUtils.findDirWithSuffixFiles(ncscriptFile, "dbcreate", "sql");

		String tableColumns = String
				.format("%s varchar(1024), %s varchar(1024), %s varchar(1024), %s varchar(1024), %s varchar(1024), %s varchar(1024), %s varchar(2048)",
						(Object[]) DbInitTableFieldValue.ALL_TABLE_FIELDS);
		String dbInitTableName = resourceQueryFactory.createTempTable(connection,
				DbInitTableFieldValue.DBINIT_TEMPTABLE_NAME, tableColumns, DbInitTableFieldValue.TABLE_ID_FIELD);
		TempTableExecContextOperator.getTempTableExecContext().setDbcreateTableName(dbInitTableName);
		for (File sqlFolder : dbInitFolder) {
			List<DbInitTableFieldValue> initTableFieldValues = DbInitFileAnalyser.analyseDbInitTableFolder(sqlFolder);
			List<Object[]> tableDataArrays = new ArrayList<Object[]>();
			for (DbInitTableFieldValue dbInitTableFieldValue : initTableFieldValues) {
				String id = String.format("%s-%s-%s-%s", dbInitTableFieldValue.getModule(),
						dbInitTableFieldValue.getBusinessComponent(), dbInitTableFieldValue.getTableName(),
						dbInitTableFieldValue.getPrimaryValue());
				tableDataArrays.add(new Object[] { id, dbInitTableFieldValue.getTableName(),
						dbInitTableFieldValue.getModule(), dbInitTableFieldValue.getBusinessComponent(),
						dbInitTableFieldValue.getPrimaryKey(), dbInitTableFieldValue.getPrimaryValue(),
						dbInitTableFieldValue.getInsertContent() });
			}

			resourceQueryFactory.insertDataToTempTable(dbInitTableName, Arrays.asList(DbInitTableFieldValue.ALL_TABLE_FIELDS),
					tableDataArrays, connection);
		}
		Logger.info("预置脚本初始化完成...");
	}

	@Override
	public String getIdentifier() {
		return CreatorConstants.DBINIT;
	}

}
