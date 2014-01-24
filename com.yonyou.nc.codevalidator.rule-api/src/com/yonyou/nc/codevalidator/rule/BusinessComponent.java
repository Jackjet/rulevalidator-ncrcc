package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;

/**
 * 执行规则检查的粒度，根据不同执行环境，执行的粒度其实现会有所调整
 * 
 * 注意：应该将此类进行重构(ExecuteUnit)，但使用者众多，无法进行修改
 * @author mazhqa
 * @since V2.7
 */
public interface BusinessComponent {

	/**
	 * 执行单元对应的产品
	 * @return
	 */
	String getProduct();
	/**
	 * 所在模块名称（全局规则执行不包含此名称，此名称是从module.xml中读取的）
	 * @return
	 */
	String getModule();

	/**
	 * 所在业务组件名称（全局执行规则以及模块执行规则不包含此名称）
	 * @return
	 */
	String getBusinessComp();

	/**
	 * 工程所在路径
	 * @return
	 */
	String getProjectPath();

	/**
	 * 工程的名称，用于ClassLoader在执行规则验证时，去加载类来帮助进行类加载工作
	 * @return
	 */
	String getProjectName();
	
//	void setProjectName(String projectName);

	/**
	 * 业务组件的显示名称，如果为全局显示global，如果不是全局，显示module-busicomp
	 * @return
	 */
	String getDisplayBusiCompName();

	/**
	 * 执行单元所在顶层文件夹
	 * @return
	 */
	String getBusinessComponentPath();

	/**
	 * 得到代码相关路径
	 * @return
	 */
	String getCodePath();

	/**
	 * 根据代码所在级别得到代码路径
	 * 
	 * @param codeFolder
	 * @return
	 */
	String getCodePath(String codeFolder);
	
	/**
	 * 得到该规则的执行层次
	 * @return
	 */
	ExecuteLayer getExecuteLayer();

//	/**
//	 * 得到资源文件夹路径
//	 * @return
//	 */
//	String getResourcePath();
//
//	/**
//	 * 得到元数据文件夹所在路径
//	 * @return
//	 */
//	String getMetadataPath();

}
