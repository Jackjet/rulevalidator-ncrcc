package com.yonyou.nc.codevalidator.resparser;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

public class JavaResourceQuery extends AbstractResourceQuery {

	/**
	 * Java资源访问权限
	 */
	private JavaResPrivilege resPrivilege = JavaResPrivilege.SRC;

	/**
	 * 业务组件(必须设置)
	 */
	private BusinessComponent businessComponent;

	/**
	 * 类名称过滤器列表(如果为null或空，不进行过滤)
	 */
	private List<String> classNameFilterList;
	
	/**
	 * 是否包含额外的包装器，即在src/public/下的代码还有额外的folder
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
