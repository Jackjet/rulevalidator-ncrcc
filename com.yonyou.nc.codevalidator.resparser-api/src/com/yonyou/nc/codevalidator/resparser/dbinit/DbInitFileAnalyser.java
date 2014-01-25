package com.yonyou.nc.codevalidator.resparser.dbinit;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 预置脚本文件分析工具类
 * 
 * @author mazhqa
 * @since V2.7
 */
public class DbInitFileAnalyser {

	public static final String NCSCRIPT = "ncscript";
	private static final Pattern BUSICOMPPATTERN = Pattern.compile("(\\w+)\\\\(\\w+)");
	
	private static final Pattern INSERT_SQL_PATTERN = Pattern.compile("insert into (\\w+)\\(([\\w- :@,.]+)\\) values\\((['\\w->(,.=|_\\~\" ):;@[^\\x00-\\xff]]+)\\)");
	
	/**
	 * 由于预置脚本的复杂性，不能对每个字段都进行处理，所以仅处理主键字段,默认主键字段是第一个
	 */
	private static final Pattern FIELD_VALUE_SPLIT_PATTERN = Pattern.compile("'[\\w->(,~.=|_\\\" ):;@[^\\x00-\\xff]]+'|-?\\w+");

	private static String ENCODING = "UTF-8";
	public static final FileFilter SQLFILE_FILTER = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".sql");
		}
	};

	/**
	 * 分析sqlFolder中的所有.sql文件，并得出对应的init table对象列表
	 * @param sqlFolder
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<DbInitTableFieldValue> analyseDbInitTableFolder(File sqlFolder) throws RuleBaseException {
		String sqlFolderPath = sqlFolder.getAbsolutePath();
		String moduleCompPath = sqlFolderPath.substring(sqlFolderPath.indexOf(NCSCRIPT) + NCSCRIPT.length());
		Matcher matcher = BUSICOMPPATTERN.matcher(moduleCompPath);
		String module = "UNKNOWN";
		String businessComponent = "UNKNOWN";
		if (matcher.find()) {
			module = matcher.group(1);
			businessComponent = matcher.group(2);
		}

		List<DbInitTableFieldValue> result = new ArrayList<DbInitTableFieldValue>();
		File[] sqlFiles = sqlFolder.listFiles(SQLFILE_FILTER);
		for (File sqlFile : sqlFiles) {
			try {
				String fileOriginContent = FileUtils.readFileToString(sqlFile, ENCODING);
				String delimeterResult = fileOriginContent.replaceAll("\ngo", "");
				String[] eachLineString = delimeterResult.split("\n");
				for (String eachLine : eachLineString) {
					Matcher insertSqlMatcher = INSERT_SQL_PATTERN.matcher(eachLine);
					if (insertSqlMatcher.find()) {
						String tableName = insertSqlMatcher.group(1);
						String fieldNames = insertSqlMatcher.group(2);
						String fieldValues = insertSqlMatcher.group(3);
						
						String[] fieldNameArray = fieldNames.split(",");
						Matcher fieldValueMatcher = FIELD_VALUE_SPLIT_PATTERN.matcher(fieldValues);
						List<String> fieldValueArray = new ArrayList<String>();
						while (fieldValueMatcher.find()) {
							String group = fieldValueMatcher.group(0);
							fieldValueArray.add(group);
						}
						String primaryKey = fieldNameArray[0];
						String primaryValue = fieldValueArray.get(0);
						result.add(new DbInitTableFieldValue(tableName, module, businessComponent, primaryKey, primaryValue, eachLine));
					}
					
				}
			} catch (IOException e) {
				throw new RuleBaseException(e);
			}
		}
		return result;
	}

}
