package com.yonyou.nc.codevalidator.resparser.dbcreate;

/**
 * 建库脚本 - 表中的字段field
 * 
 * @author mazhqa
 * @since V2.3
 */
public class DbCreateTableField {
	
	public static final String DBCREATE_TEMPTABLE_NAME = "dbcreate_field";
	public static final String FIELD_ID_FIELD = "id";
	public static final String FIELD_NAME_FIELD = "fieldName";
	public static final String FIELD_TABLENAME_FIELD = "fieldTableName";
	public static final String FIELD_TYPE_FIELD = "fieldType";
	public static final String FIELD_LENGTH_FIELD = "fieldLength";
	public static final String PRECISE_FIELD = "precise";
	public static final String CANBENULL_FIELD = "canbenull";
	public static final String HASDEFAULTVALUE_FIELD = "hasdefaultvalue";
	public static final String DBCREATE_FILE_PATH = "dbcreatefilepath";
	
	public static final String[] ALL_FIELDS = new String[] {DbCreateTableField.FIELD_ID_FIELD, DbCreateTableField.FIELD_NAME_FIELD,
		DbCreateTableField.FIELD_TABLENAME_FIELD, DbCreateTableField.FIELD_TYPE_FIELD,
		DbCreateTableField.FIELD_LENGTH_FIELD, DbCreateTableField.PRECISE_FIELD,
		DbCreateTableField.HASDEFAULTVALUE_FIELD, DbCreateTableField.CANBENULL_FIELD, DBCREATE_FILE_PATH};

	private String tableName;
	private String fieldName;
	private String fieldType;
	private int fieldLength;
	/**
	 * 精度，仅在number类型中使用
	 */
	private int precise;
	private boolean canBeNull;
	private boolean hasDefaultValue;
	
	private String dbcreateFilePath;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	public boolean isCanBeNull() {
		return canBeNull;
	}

	public void setCanBeNull(boolean canBeNull) {
		this.canBeNull = canBeNull;
	}

	public int getPrecise() {
		return precise;
	}

	public void setPrecise(int precise) {
		this.precise = precise;
	}

	public boolean isHasDefaultValue() {
		return hasDefaultValue;
	}

	public void setHasDefaultValue(boolean hasDefaultValue) {
		this.hasDefaultValue = hasDefaultValue;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDbcreateFilePath() {
		return dbcreateFilePath;
	}

	public void setDbcreateFilePath(String dbcreateFilePath) {
		this.dbcreateFilePath = dbcreateFilePath;
	}
	
}
