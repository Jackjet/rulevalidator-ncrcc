package com.yonyou.nc.codevalidator.runtime.plugin.mde;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.datasource.IDataSourceService;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;

/**
 * 元数据插件的数据源服务
 * @author mazhqa
 * @since V1.0
 */
public class MdeDataSourceService implements IDataSourceService {

	@Override
	public DataSourceMeta getDefaultDataSourceMeta() throws ResourceParserException {
		try {
			return MdeConfigInfoUtils.getDataSourceMeta(DEFAULT_DATASOURCE_NAME);
		} catch (Exception e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public DataSourceMeta getDataSourceMetaByName(String dataSourceName) throws ResourceParserException {
		try {
			return MdeConfigInfoUtils.getDataSourceMeta(dataSourceName);
		} catch (RuleBaseException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public List<String> getDataSourceNames() throws ResourceParserException {
		try {
			return MdeConfigInfoUtils.getDataSourceNames();
		} catch (RuleBaseException e) {
			throw new ResourceParserException(e);
		}
	}

}
