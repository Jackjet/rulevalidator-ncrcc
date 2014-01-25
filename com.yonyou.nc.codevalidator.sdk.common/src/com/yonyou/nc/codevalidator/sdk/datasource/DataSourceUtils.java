package com.yonyou.nc.codevalidator.sdk.datasource;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 数据源工具类，用于解析对应的数据源配置文件prop.xml
 * @author mazhqa
 * @since V1.0
 */
public final class DataSourceUtils {
	
	private DataSourceUtils(){
		
	}

	/**
	 * 从对应的prop.xml文件根据数据源名称得到对应的数据源元配置
	 * @param file
	 * @param dataSourceName
	 * @return 如果数据源未查询到，返回null
	 * @throws RuleBaseException
	 */
	public static DataSourceMeta getDataSourceMeta(String file, String dataSourceName) throws RuleBaseException {
		DataSourceMeta[] metas = loadPropInfo(file).getDataSource();
		for (DataSourceMeta meta : metas) {
			if (dataSourceName.equals(meta.getDataSourceName())){
				return meta;
			}
		}
//		for (int i = 0; i < metas.length; i++) {
//			DataSourceMeta meta = metas[i];
//		}
		return null;
//		DataSourceMeta metaswithdesign[] = new DataSourceMeta[metas.length + 1];
//		System.arraycopy(metas, 0, metaswithdesign, 1, metas.length);
//		metaswithdesign[0] = new DataSourceMeta();
//		return metaswithdesign[0];
	}

	private static PropInfo loadPropInfo(String propfile) throws RuleBaseException  {
		PropInfo prop = (PropInfo) XMLToObject.getJavaObjectFromFile(propfile, PropInfo.class, true);
		return prop;
	}
	
	public static List<String> getDataSourceNameList(String file) throws RuleBaseException{
		DataSourceMeta[] metas = loadPropInfo(file).getDataSource();
		List<String> result = new ArrayList<String>();
		for (DataSourceMeta dataSourceMeta : metas) {
			result.add(dataSourceMeta.getDataSourceName());
		}
		return result;
	}

}
