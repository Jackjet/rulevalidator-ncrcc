package com.yonyou.nc.codevalidator.resparser.dbinit;

/**
 * 
 * @author mazhqa
 * @since V2.7
 */
public class DbInitTableFieldValue {

	public static final String DBINIT_TEMPTABLE_NAME = "dbinit_table";
	public static final String TABLE_ID_FIELD = "id";
	public static final String TABLE_NAME_FIELD = "tableName";
	public static final String MODULE_FIELD = "module";
	public static final String BUSICOMP_FIELD = "businessComponent";
	public static final String PRIMARY_KEY_FIELD = "primaryKey";
	public static final String PRIMARY_VALUE_FIELD = "primaryValue";
	public static final String INSERT_CONTENT_FIELD = "insertContent";
//	public static final String FIELD_VALUE_FIELD = "fieldValue";
	public static final String PARENT_FIELD_NAME_FIELD = "parentFieldName";
	public static final String PARENT_FIELD_VALUE_FIELD = "parentFieldValue";

	public static final String[] ALL_TABLE_FIELDS = new String[] { TABLE_ID_FIELD, TABLE_NAME_FIELD, MODULE_FIELD,
			BUSICOMP_FIELD, PRIMARY_KEY_FIELD, PRIMARY_VALUE_FIELD, INSERT_CONTENT_FIELD };

	private final String tableName;
	private final String module;
	private final String businessComponent;
	private final String primaryKey;
	private final String primaryValue;
	private final String insertContent;
	/**
	 * 对于主子表关系的表内容，需要设置其外键的字段名称以及字段值
	 */
	private String parentFieldName;
	private String parentFieldValue;
	
	public DbInitTableFieldValue(String tableName, String module, String businessComponent, String primaryKey,
			String primaryValue, String insertContent) {
		super();
		this.tableName = tableName;
		this.module = module;
		this.businessComponent = businessComponent;
		this.primaryKey = primaryKey;
		this.primaryValue = primaryValue;
		this.insertContent = insertContent;
	}

	public String getTableName() {
		return tableName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getModule() {
		return module;
	}

	public String getBusinessComponent() {
		return businessComponent;
	}

	public String getPrimaryValue() {
		return primaryValue;
	}

	public String getInsertContent() {
		return insertContent;
	}

	public String getParentFieldName() {
		return parentFieldName;
	}

	public void setParentFieldName(String parentFieldName) {
		this.parentFieldName = parentFieldName;
	}

	public String getParentFieldValue() {
		return parentFieldValue;
	}

	public void setParentFieldValue(String parentFieldValue) {
		this.parentFieldValue = parentFieldValue;
	}

}
