package com.yonyou.nc.codevalidator.resparser.executeresult;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;

/**
 * 脚本形式的结果(DataSet)导出策略
 * 
 * @author mazhqa
 * @since V2.5
 */
public interface IScriptExportStrategy {
	
	/**
	 * 默认的导出策略，只打印出dataSet的toString显示
	 */
	static final IScriptExportStrategy DEFAULT_EXPORT_STRATEGY = new IScriptExportStrategy() {
		
		@Override
		public String exportDataSet(DataSet dataSet) {
			return dataSet.toString();
		}
	};

	/**
	 * 将结果集导出成对应的字符串显示
	 * @param dataSet
	 * @return
	 */
	String exportDataSet(DataSet dataSet);

}
