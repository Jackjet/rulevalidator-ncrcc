package com.yonyou.nc.codevalidator.resparser.dbcreate;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.temp.AbstractTempTableResourceCreator;
import com.yonyou.nc.codevalidator.resparser.temp.TempTableExecContextOperator;
import com.yonyou.nc.codevalidator.rule.CreatorConstants;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.utils.FileLocatorUtils;

/**
 * 用于集中处理建库脚本的操作类
 * 
 * @author mazhqa
 * @since V2.3
 */
public final class DbCreateResourceCreator extends AbstractTempTableResourceCreator {

	public static final String NCSCRIPT = "ncscript";
	public static final String FIND_DB_TYPE = "ORACLE";
	public static final String FILE_SUFFIX = "sql";

	@SuppressWarnings("unchecked")
	@Override
	public void createTempResources(Connection connection) throws RuleBaseException {
		Logger.info("开始进行建库脚本初始化...");
		RuntimeContext currentRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper()
				.getCurrentRuntimeContext();
		String ncHome = currentRuntimeContext.getNcHome();
		Collection<File> dbCreateFolders = FileLocatorUtils.findDirWithGivenName(new File(ncHome, NCSCRIPT),
				FIND_DB_TYPE);
		List<File> sqlFileList = new ArrayList<File>();
		for (File dbCreateFolder : dbCreateFolders) {
			sqlFileList.addAll(FileUtils.listFiles(dbCreateFolder, new String[] { FILE_SUFFIX }, true));
		}

		List<DbCreateTable> dbCreateTableList = new ArrayList<DbCreateTable>();
		for (File dbCreateSqlFile : sqlFileList) {
			List<DbCreateTable> generateDbCreateTables = DbCreateFileAnalyser.generateDbCreateTables(dbCreateSqlFile);
			dbCreateTableList.addAll(generateDbCreateTables);
		}

		String tableColumns = String.format(
				"%s varchar(1024), %s varchar(1024), %s varchar(1024), %s varchar(1024), %s varchar(1024)",
				(Object[]) DbCreateTable.ALL_TABLE_FIELDS);
		String dbcreateTableName = resourceQueryFactory.createTempTable(connection, DbCreateTable.DBCREATE_TEMPTABLE_NAME, tableColumns,
				DbCreateTable.TABLE_ID_FIELD);
		TempTableExecContextOperator.getTempTableExecContext().setDbcreateTableName(dbcreateTableName);

		String fieldColumns = String
				.format("%s varchar(1024), %s varchar(1024),%s varchar(1024), %s varchar(1024), %s integer, %s integer, %s char(1), %s char(1), %s varchar(1024)",
						(Object[]) DbCreateTableField.ALL_FIELDS);
		String dbcreateDetailTableName = resourceQueryFactory.createTempTable(connection, DbCreateTableField.DBCREATE_TEMPTABLE_NAME, fieldColumns,
				DbCreateTableField.FIELD_ID_FIELD);
		TempTableExecContextOperator.getTempTableExecContext().setDbcreateDetailTableName(dbcreateDetailTableName);

		List<Object[]> tableDataArrays = new ArrayList<Object[]>();
		List<Object[]> tableFieldDataArrays = new ArrayList<Object[]>();
		for (DbCreateTable dbCreateTable : dbCreateTableList) {
			List<DbCreateTableField> tableFieldList = dbCreateTable.getTableFieldList();
			for (DbCreateTableField dbCreateTableField : tableFieldList) {
				String tableFieldId = String.format("%s-%s", dbCreateTableField.getTableName(),
						dbCreateTableField.getFieldName());
				tableFieldDataArrays.add(new Object[] { tableFieldId, dbCreateTableField.getFieldName(),
						dbCreateTableField.getTableName(), dbCreateTableField.getFieldType(),
						dbCreateTableField.getFieldLength(), dbCreateTableField.getPrecise(),
						dbCreateTableField.isHasDefaultValue(), dbCreateTableField.isCanBeNull(), dbCreateTableField.getDbcreateFilePath() });
			}
			String tableId = String.format("%s-%s-%s", dbCreateTable.getModule(), dbCreateTable.getBusinessComponent(),
					dbCreateTable.getTableName());
			tableDataArrays.add(new Object[] { tableId, dbCreateTable.getTableName(), dbCreateTable.getModule(),
					dbCreateTable.getBusinessComponent(), dbCreateTable.getPrimaryKey() });
		}
		resourceQueryFactory.insertDataToTempTable(dbcreateTableName,
				Arrays.asList(DbCreateTable.ALL_TABLE_FIELDS), tableDataArrays, connection);
		resourceQueryFactory.insertDataToTempTable(dbcreateDetailTableName,
				Arrays.asList(DbCreateTableField.ALL_FIELDS), tableFieldDataArrays, connection);
		Logger.info("建库脚本初始化完成...");
	}

	@Override
	public String getIdentifier() {
		return CreatorConstants.DBCREATE;
	}

}
