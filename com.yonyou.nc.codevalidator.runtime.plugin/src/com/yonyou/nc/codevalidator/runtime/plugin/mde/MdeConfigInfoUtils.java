package com.yonyou.nc.codevalidator.runtime.plugin.mde;

import java.io.File;
import java.util.List;

import nc.uap.mde.MDEPlugin;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseRuntimeException;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceMeta;
import com.yonyou.nc.codevalidator.sdk.datasource.DataSourceUtils;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 获取mde插件中的配置信息
 * 
 * @author luoweid
 * 
 */
public final class MdeConfigInfoUtils {

	public static final String MDE_FIELD_CLINET_IP = "FIELD_CLINET_IP";
	public static final String MDE_FIELD_CLINET_PORT = "FIELD_CLINET_PORT";
	public static final String MDE_FIELD_NCHOME = "FIELD_NC_HOME";

	private MdeConfigInfoUtils() {

	}

//	public static String getClientIp() {
//		return MDEPlugin.getDefault().getPreferenceStore().getString(MDE_FIELD_CLINET_IP);
//	}
//
//	public static String getClientPort() {
//		return MDEPlugin.getDefault().getPreferenceStore().getString(MDE_FIELD_CLINET_PORT);
//	}

	public static String getMdeNcHome() {
		String ncHome = MDEPlugin.getDefault().getPreferenceStore().getString(MDE_FIELD_NCHOME);
		return StringUtils.isNotBlank(ncHome) ? ncHome : MDEPlugin.getNCHome();
	}

	/**
	 * 根据数据源名称获得NCHOME下对应的数据源配置
	 * @param dataSourceName
	 * @return
	 * @throws Exception
	 */
	public static DataSourceMeta getDataSourceMeta(String dataSourceName) throws RuleBaseException {
		String ncHome = getMdeNcHome();
		String filename = String.format("%s/ierp/bin/prop.xml", ncHome);
		File file = new File(filename);
		if (!file.exists()) {
			throw new RuleBaseRuntimeException(String.format("%s 文件不存在", filename));
		}
		return DataSourceUtils.getDataSourceMeta(filename, dataSourceName);
	}
	
	public static List<String> getDataSourceNames() throws RuleBaseException{
		String ncHome = getMdeNcHome();
		String filename = String.format("%s/ierp/bin/prop.xml", ncHome);
		return DataSourceUtils.getDataSourceNameList(filename);
	}

}
