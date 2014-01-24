package com.yonyou.nc.codevalidator.resparser;


/**
 * ���ڽű���ѯ��query���󣬿�������
 * <li>��ѯsql</li>
 * @author mazhqa
 * @since V1.0
 */
public class ScriptResourceQuery extends AbstractResourceQuery {

	private final String sqlQueryString;
//	private String dataSourceName;
	
	public ScriptResourceQuery(String sqlQueryString) {
		this.sqlQueryString = sqlQueryString;
	}
	
	@Override
	public String getQueryString() {
		return sqlQueryString;
	}
	
//	public String getDataSourceName() {
//		return dataSourceName;
//	}
//
//	/**
//	 * ������Facade������datasource
//	 * @param dataSourceName
//	 */
//	void setDataSourceName(String dataSourceName) {
//		this.dataSourceName = dataSourceName;
//	}
}
