package com.yonyou.nc.codevalidator.resparser;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

public class JavaResourceQuery extends AbstractResourceQuery {

	/**
	 * Java��Դ����Ȩ��
	 */
	private JavaResPrivilege resPrivilege = JavaResPrivilege.SRC;

	/**
	 * ҵ�����(��������)
	 */
	private BusinessComponent businessComponent;

	/**
	 * �����ƹ������б�(���Ϊnull��գ������й���)
	 */
	private List<String> classNameFilterList;
	
	/**
	 * �Ƿ��������İ�װ��������src/public/�µĴ��뻹�ж����folder
	 * @since V2.3
	 */
	private boolean containExtraWrapper = false;

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

	public void setBusinessComponent(BusinessComponent businessComponent) {
		this.businessComponent = businessComponent;
	}

	public JavaResPrivilege getResPrivilege() {
		return resPrivilege;
	}

	public void setResPrivilege(JavaResPrivilege resPrivilege) {
		this.resPrivilege = resPrivilege;
	}

	public List<String> getClassNameFilterList() {
		return classNameFilterList;
	}

	public void setClassNameFilterList(List<String> classNameFilterList) {
		this.classNameFilterList = classNameFilterList;
	}

	public boolean isContainExtraWrapper() {
		return containExtraWrapper;
	}

	public void setContainExtraWrapper(boolean containExtraWrapper) {
		this.containExtraWrapper = containExtraWrapper;
	}
	
}
