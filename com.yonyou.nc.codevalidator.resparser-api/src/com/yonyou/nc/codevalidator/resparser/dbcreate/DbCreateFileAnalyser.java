package com.yonyou.nc.codevalidator.resparser.dbcreate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 预置脚本文件的分析器
 * 
 * @author mazhqa
 * @since V2.3
 */
public final class DbCreateFileAnalyser {

	private static final String ENCODING = "UTF-8";

	private static final String EXCLUDE_STYPE_TEXT = "\\s+\\/\\*([[^\\x00-\\xff]_[\\d][\\w]]+)+\\*\\/,\\s+";

	private static final Pattern FIELD_PATTERN = Pattern
			.compile("(\\w+) (\\w+)(\\((\\d+)(,(\\d+))?\\))? (default '~' )?(not )?null;");
	private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("create table (\\w+)");
	private static final Pattern TABLE_PRIMARY_KEY_PATTERN = Pattern.compile("primary key \\((\\w+)\\)");
	private static final Pattern BUSICOMPPATTERN = Pattern.compile("(\\w+)\\\\(\\w+)");

	private DbCreateFileAnalyser() {

	}

	/**
	 * 从数据库预置文件中生成对应的数据表对象
	 * @param dbCreateFile
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<DbCreateTable> generateDbCreateTables(File dbCreateFile) throws RuleBaseException {
		try {
			List<DbCreateTable> result = new ArrayList<DbCreateTable>();
			String fileOriginContent = FileUtils.readFileToString(dbCreateFile, ENCODING);
			String analyseContent = fileOriginContent.replaceAll(EXCLUDE_STYPE_TEXT, ";");
			String[] splitTableStrings = analyseContent.split("\\s\\/\\s");
			String absolutePath = dbCreateFile.getAbsolutePath();
			int start = absolutePath.indexOf(DbCreateResourceCreator.NCSCRIPT) + DbCreateResourceCreator.NCSCRIPT.length();
			int end = absolutePath.indexOf("dbcreate");
			String moduleCompPath = absolutePath.substring(start, end);
			Matcher busiCompMatcher = BUSICOMPPATTERN.matcher(moduleCompPath);
			String module = "UNKNOWN";
			String busiComp = "UNKNOWN";
			if(busiCompMatcher.find()) {
				module = busiCompMatcher.group(1);
				busiComp = busiCompMatcher.group(2);
			}
			
			for (String splitTableCreateSql : splitTableStrings) {
				Matcher tableHeaderMatcher = TABLE_NAME_PATTERN.matcher(splitTableCreateSql);
				Matcher tablePrimaryKeyMatcher = TABLE_PRIMARY_KEY_PATTERN.matcher(splitTableCreateSql);
				if (tableHeaderMatcher.find() && tablePrimaryKeyMatcher.find()) {
					DbCreateTable dbCreateTable = new DbCreateTable();
					String tableName = tableHeaderMatcher.group(1);
					String primaryKey = tablePrimaryKeyMatcher.group(1);
					dbCreateTable.setTableName(tableName);
					dbCreateTable.setPrimaryKey(primaryKey);
					dbCreateTable.setModule(module);
					dbCreateTable.setBusinessComponent(busiComp);
					
					Matcher fieldMatcher = FIELD_PATTERN.matcher(splitTableCreateSql);
					List<DbCreateTableField> dbCreateTableFields = new ArrayList<DbCreateTableField>();
					while (fieldMatcher.find()) {
						String fieldName = fieldMatcher.group(1);
						String fieldType = fieldMatcher.group(2);
						String fieldLength = fieldMatcher.group(4);
						String precise = fieldMatcher.group(6);
						boolean hasDefaultValue = fieldMatcher.group(7) == null;
						boolean canBeNull = fieldMatcher.group(8) != null;
						DbCreateTableField tableField = new DbCreateTableField();
						tableField.setFieldName(fieldName);
						tableField.setTableName(tableName);
						tableField.setFieldType(fieldType);
						tableField.setFieldLength(fieldLength == null ? 0 : Integer.parseInt(fieldLength));
						tableField.setPrecise(precise == null ? 0 : Integer.parseInt(precise));
						tableField.setCanBeNull(canBeNull);
						tableField.setHasDefaultValue(hasDefaultValue);
						tableField.setDbcreateFilePath(dbCreateFile.getAbsolutePath());
						dbCreateTableFields.add(tableField);
					}
					dbCreateTable.setTableFieldList(dbCreateTableFields);
					result.add(dbCreateTable);
				}
			}
			return result;
		} catch (IOException e) {
			throw new RuleBaseException(e);
		}
	}

	public static void main(String[] args) throws IOException, RuleBaseException {
		File file = new File(
				"D:\\Develop\\nc-product\\20130318\\ncscript\\am\\aim\\dbcreate\\ORACLE\\00001\\tb_aim_equip.sql");
		List<DbCreateTable> tables = DbCreateFileAnalyser.generateDbCreateTables(file);
		 System.out.println(tables);
	}
}
