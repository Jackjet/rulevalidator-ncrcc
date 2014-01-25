package com.yonyou.nc.codevalidator.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;

/**
 * 在全局执行规则的过程中会使用到该全局执行单元
 * <P>
 * 
 * @author mazhqa
 * @since V2.7
 */
public class GlobalExecuteUnit implements BusinessComponent, ICompositeExecuteUnit {

	public static final String MESSAGE = "在全局执行时，无模块或业务组件，此方法暂时不可用!";
	
	public static final String DEFAULT_GLOBAL_NAME = "global";

	private final String projectName;
	private final String productCode;

	private List<BusinessComponent> subBusinessComponentList = new ArrayList<BusinessComponent>();

	public GlobalExecuteUnit(String projectName) {
		this.projectName = projectName;
		this.productCode = DEFAULT_GLOBAL_NAME;
	}
	
	public GlobalExecuteUnit(String projectName, String productCode) {
		this.projectName = projectName;
		this.productCode = productCode;
	}

	// public void setProjectName(String projectName) {
	// this.projectName = projectName;
	// }

	@Override
	public String getModule() {
//		throw new UnsupportedOperationException(MESSAGE);
		return "";
	}

	@Override
	public String getBusinessComp() {
//		throw new UnsupportedOperationException(MESSAGE);
		return "";
	}

	@Override
	public String getProjectPath() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public String getDisplayBusiCompName() {
		return productCode;
	}

	@Override
	public String getBusinessComponentPath() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getCodePath() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getCodePath(String codeFolder) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public List<BusinessComponent> getSubBusinessComponentList() {
		return Collections.unmodifiableList(subBusinessComponentList);
	}

	@Override
	public void addSubBusinessComponentList(BusinessComponent businessComponent) {
		this.subBusinessComponentList.add(businessComponent);
	}

	@Override
	public ExecuteLayer getExecuteLayer() {
		return ExecuteLayer.GLOBAL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GlobalExecuteUnit other = (GlobalExecuteUnit) obj;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		return true;
	}

	@Override
	public String getProduct() {
		return productCode;
	}

}
