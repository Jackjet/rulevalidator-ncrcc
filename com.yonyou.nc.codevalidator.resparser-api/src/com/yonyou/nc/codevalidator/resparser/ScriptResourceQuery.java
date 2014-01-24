package com.yonyou.nc.codevalidator.resparser;


/**
 * 用于脚本查询的query对象，可以配置
 * <li>查询sql</li>
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
//	 * 仅能在Facade中设置datasource
//	 * @param dataSourceName
//	 */
//	void setDataSourceName(String dataSourceName) {
//		this.dataSourceName = dataSourceName;
//	}
}
