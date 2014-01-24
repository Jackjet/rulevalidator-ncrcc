package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * Aop��Դ��ѯ
 * @author mazhqa
 * @since V2.1
 */
public class AopResourceQuery extends AbstractResourceQuery {
	
	private BusinessComponent businessComponent;
	/**
	 * ������fileNameʱ������fileName���й��˲���(����Ҫ���.aop��׺)
	 * û�����ô�����ʱ���᷵�������ļ��б�
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
