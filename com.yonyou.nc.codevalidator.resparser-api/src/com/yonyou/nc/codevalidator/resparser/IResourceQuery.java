package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.RuntimeContext;

/**
 * 资源查询接口
 * @author mazhqa
 * @since V1.0
 */
public interface IResourceQuery {
	
	/**
	 * 资源查询的string表示
	 * @return
	 */
	String getQueryString();
	 
	/**
	 * 当前规则执行的上下文，在执行查询时可注入执行的上下文信息
	 * @return
	 * @since V2.1
	 */
	RuntimeContext getRuntimeContext();
	
	void setRuntimeContext(RuntimeContext runtimeContext);
}
