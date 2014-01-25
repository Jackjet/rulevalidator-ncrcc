package com.yonyou.nc.codevalidator.resparser.datasource;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.IExecutorContextHelper;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;

/**
 * 
 * @author 何冠宇
 * 
 * @date 2009-3-30
 */
public class DataSourceMetaMgr {

	private static DataSourceMetaMgr mgr = new DataSourceMetaMgr();

	private static IDataSourceService dataSourceService;

	public void setDataSourceService(IDataSourceService dataSourceService) {
		DataSourceMetaMgr.dataSourceService = dataSourceService;
	}

	public static DataSourceMetaMgr getInstance() {
		return mgr;
	}

	public String getOIDMark(String dataSourceName) {
		DataSourceMeta meta = getDataSourceMeta(dataSourceName);
		return meta == null ? "AA" : meta.getOIDMark();
	}

	public DataSourceMeta getDataSourceMeta(String dataSourceName) {
		try {
			return dataSourceService.getDataSourceMetaByName(dataSourceName);
		} catch (ResourceParserException e) {
			throw new RuleBaseRuntimeException("Data source not configed or can't get datasource meta");
		}
	}
	
	/**
	 * 得到当前数据源设置上
	 * @return
	 */
	public DataSourceMeta getCurrentDataSourceMeta() {
		IExecutorContextHelper executorContextHelper = ExecutorContextHelperFactory.getExecutorContextHelper();
		try {
			String dataSourceName = executorContextHelper.getCurrentRuntimeContext().getDataSource();
			return dataSourceService.getDataSourceMetaByName(dataSourceName);
		} catch (RuleBaseException e) {
			throw new RuleBaseRuntimeException("current datasource is not found, you can't invocate this method.");
		}
	}

	public DataSourceMeta getDefaultDataSourceMeta() {
		try {
			return dataSourceService.getDefaultDataSourceMeta();
		} catch (ResourceParserException e) {
			throw new RuleBaseRuntimeException("Data source not configed or can't get datasource meta");
		}
	}

}
