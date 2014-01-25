package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.RuntimeContext;

/**
 * 
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractResourceQuery implements IResourceQuery {

	protected String queryString;
	protected RuntimeContext runtimeContext;

	public String getQueryString() {
		return queryString;
	}

	public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

	public void setRuntimeContext(RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}
	
}
