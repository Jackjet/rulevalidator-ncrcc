package com.yonyou.nc.codevalidator.resparser.dbcreate;

import java.util.ArrayList;
import java.util.List;

/**
 * 建库脚本-数据库表
 * 
 * @author mazhqa
 * @since V2.3
 */
public class DbCreateTable {
	
	public static final String DBCREATE_TEMPTABLE_NAME = "dbcreate_table";
	public static final String TABLE_ID_FIELD = "id";
	public static final String TABLE_NAME_FIELD = "tableName";
	public static final String MODULE_FIELD = "module";
	public static final String BUSICOMP_FIELD = "businessComponent";
	public static final String PRIMARY_KEY_FIELD = "primaryKey";
	
	public static final String[] ALL_TABLE_FIELDS = new String[] {TABLE_ID_FIELD, TABLE_NAME_FIELD, MODULE_FIELD, BUSICOMP_FIELD, PRIMARY_KEY_FIELD};
	
	private String tableName;
	private String module;
	private String businessComponent;
	private String primaryKey;

	private List<DbCreateTableField> tableFieldList = new ArrayList<DbCreateTableField>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<DbCreateTableField> getTableFieldList() {
		return tableFieldList;
	}

	public void setTableFieldList(List<DbCreateTableField> tableFieldList) {
		this.tableFieldList = tableFieldList;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getBusinessComponent() {
		return businessComponent;
	}

	public void setBusinessComponent(String businessComponent) {
		this.businessComponent = businessComponent;
	}

}
