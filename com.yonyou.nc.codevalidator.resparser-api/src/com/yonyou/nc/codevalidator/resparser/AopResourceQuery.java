package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * Aop资源查询
 * @author mazhqa
 * @since V2.1
 */
public class AopResourceQuery extends AbstractResourceQuery {
	
	private BusinessComponent businessComponent;
	/**
	 * 当设置fileName时，会以fileName进行过滤操作(不需要添加.aop后缀)
	 * 没有设置此属性时，会返回所有文件列表
	 */
	private String fileName;

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

	public void setBusinessComponent(BusinessComponent businessComponent) {
		this.businessComponent = businessComponent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getQueryString() {
		return fileName;
	}

}
