package com.yonyou.nc.codevalidator.resparser.resource.utils;

/**
 * ���ݿ�����Ϣ���������ݱ����ƣ����ͣ����ȣ��Ƿ����������
 * 
 * @author zhenglq
 * @since V2.7
 */
public class TableColumn {

	private String columnName;
	private String columnType;
	private Integer length;
	private boolean isPrimarykey;
	private boolean isForign;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public boolean isPrimarykey() {
		return isPrimarykey;
	}

	public void setPrimarykey(boolean isPrimarykey) {
		this.isPrimarykey = isPrimarykey;
	}

	public boolean isForign() {
		return isForign;
	}

	public void setForign(boolean isForign) {
		this.isForign = isForign;
	}

}
