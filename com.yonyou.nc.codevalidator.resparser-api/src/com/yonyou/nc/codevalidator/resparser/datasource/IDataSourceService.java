package com.yonyou.nc.codevalidator.resparser.datasource;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;

/**
 * 数据源服务：根据NCHOME中的数据源名称获得对应数据源配置元数据
 * @author mazhqa
 * @since V1.0
 */
public interface IDataSourceService {
	
	/**
	 * 默认的数据源名称
	 */
	String DEFAULT_DATASOURCE_NAME = "design";
	
	/**
	 * 得到默认:design 的数据源配置
	 * @return
	 * @throws ResourceParserException
	 */
	DataSourceMeta getDefaultDataSourceMeta() throws ResourceParserException;
	
	/**
	 * 根据数据源名称得到对应的数据源配置
	 * @param dataSourceName
	 * @return
	 * @throws ResourceParserException
	 */
	DataSourceMeta getDataSourceMetaByName(String dataSourceName) throws ResourceParserException;
	
	/**
	 * 得到当前环境(nchome)中所有的数据源名称列表
	 * @return
	 * @throws ResourceParserException
	 */
	List<String> getDataSourceNames() throws ResourceParserException;

}
