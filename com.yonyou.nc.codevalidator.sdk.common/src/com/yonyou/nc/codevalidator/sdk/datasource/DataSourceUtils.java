package com.yonyou.nc.codevalidator.sdk.datasource;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����Դ�����࣬���ڽ�����Ӧ������Դ�����ļ�prop.xml
 * @author mazhqa
 * @since V1.0
 */
public final class DataSourceUtils {
	
	private DataSourceUtils(){
		
	}

	/**
	 * �Ӷ�Ӧ��prop.xml�ļ���������Դ���Ƶõ���Ӧ������ԴԪ����
	 * @param file
	 * @param dataSourceName
	 * @return �������Դδ��ѯ��������null
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
