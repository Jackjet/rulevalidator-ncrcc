package com.yonyou.nc.codevalidator.runtime;

import java.io.File;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.datasource.IDataSourceService;
import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceUtils;

/**
 * 在Server服务端的数据源服务
 * 
 * @author mazhqa
 * @since V2.0
 */
public class DataSourceServiceImpl implements IDataSourceService {

	@Override
	public DataSourceMeta getDefaultDataSourceMeta() throws ResourceParserException {
		return getDataSourceMetaByName(DEFAULT_DATASOURCE_NAME);
	}

	@Override
	public DataSourceMeta getDataSourceMetaByName(String dataSourceName) throws ResourceParserException {
		try {
			RuntimeContext runtimeContext = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext();
			String ncHome = runtimeContext.getNcHome();
			File ncHomeFolder = new File(ncHome);
			File customePropFile = new File(ncHomeFolder, RuntimeConstant.DATASOURCE_FILE);
			return DataSourceUtils.getDataSourceMeta(customePropFile.getAbsolutePath(), dataSourceName);
		} catch (RuleBaseException e) {
			throw new ResourceParserException(e);
		}
	}

	@Override
	public List<String> getDataSourceNames() throws ResourceParserException {
		try {
			RuntimeContext runtimeContext = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext();
			String ncHome = runtimeContext.getNcHome();
			File ncHomeFolder = new File(ncHome);
			File customePropFile = new File(ncHomeFolder, RuntimeConstant.DATASOURCE_FILE);
			return DataSourceUtils.getDataSourceNameList(customePropFile.getAbsolutePath());
		} catch (RuleBaseException e) {
			throw new ResourceParserException(e);
		}
	}

}
