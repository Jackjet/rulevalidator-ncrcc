package com.yonyou.nc.codevalidator.resparser.datasource;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;

/**
 * ����Դ���񣺸���NCHOME�е�����Դ���ƻ�ö�Ӧ����Դ����Ԫ����
 * @author mazhqa
 * @since V1.0
 */
public interface IDataSourceService {
	
	/**
	 * Ĭ�ϵ�����Դ����
	 */
	String DEFAULT_DATASOURCE_NAME = "design";
	
	/**
	 * �õ�Ĭ��:design ������Դ����
	 * @return
	 * @throws ResourceParserException
	 */
	DataSourceMeta getDefaultDataSourceMeta() throws ResourceParserException;
	
	/**
	 * ��������Դ���Ƶõ���Ӧ������Դ����
	 * @param dataSourceName
	 * @return
	 * @throws ResourceParserException
	 */
	DataSourceMeta getDataSourceMetaByName(String dataSourceName) throws ResourceParserException;
	
	/**
	 * �õ���ǰ����(nchome)�����е�����Դ�����б�
	 * @return
	 * @throws ResourceParserException
	 */
	List<String> getDataSourceNames() throws ResourceParserException;

}
