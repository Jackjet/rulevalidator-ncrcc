package com.yonyou.nc.codevalidator.runtime;

/**
 * 规则的运行态执行抽象接口
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleRuntimeExecutor {
	//
	// /**
	// * 执行规则验证
	// *
	// * @param ncHome
	// * - 指定的NCHome地址
	// * @param codePath
	// * - nc mde工程路径
	// * @param dataSourceName
	// * - 数据源名称
	// */
	// @Deprecated
	// void execute(String ncHome, String codePath, String dataSourceName);

	/**
	 * 执行规则验证，自定义规则级别
	 * 
	 * @param ncHome
	 * @param codePath
	 * @param dataSourceName
	 * @param executePerid
	 * @param productCode
	 */
	void execute(String ncHome, String codePath, String dataSourceName, String executePeriod, String productCode);

}
