package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * UPM×ÊÔ´²éÑ¯
 * @author mazhqa
 * @since V1.0
 */
public class UpmResourceQuery extends AbstractResourceQuery {

	private BusinessComponent businessComponent;
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
