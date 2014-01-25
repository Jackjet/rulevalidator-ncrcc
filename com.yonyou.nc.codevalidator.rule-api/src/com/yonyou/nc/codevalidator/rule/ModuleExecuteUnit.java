package com.yonyou.nc.codevalidator.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;

/**
 * 执行规则检查的模块层，本部分配置的模块层规则会在当前模块执行
 * 
 * @author mazhqa
 * @since V2.7
 */
public class ModuleExecuteUnit implements BusinessComponent, ICompositeExecuteUnit {

	private final String projectName;
	private final String projectPath;
	private final String moduleName;
	private final String moduleDir;
	private final String productCode;

	private List<BusinessComponent> subBusinessComponentList = new ArrayList<BusinessComponent>();

	public ModuleExecuteUnit(String projectName, String projectPath, String productCode, String moduleName,
			String moduleDir) {
		super();
		this.projectName = projectName;
		this.projectPath = projectPath;
		this.productCode = productCode;
		this.moduleName = moduleName;
		this.moduleDir = moduleDir;
	}

	@Override
	public String getModule() {
		return moduleName;
	}

	@Override
	public String getBusinessComp() {
		return "";
	}

	@Override
	public String getProjectPath() {
		return projectPath;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public String getDisplayBusiCompName() {
		return moduleName;
	}

	@Override
	public String getBusinessComponentPath() {
		return String.format("%s/%s", projectPath, moduleDir);
	}

	@Override
	public String getCodePath() {
		return String.format("%s/%s/src", projectPath, moduleDir);
	}

	@Override
	public String getCodePath(String codeFolder) {
		return getCodePath() + "/" + codeFolder;
	}

	@Override
	public List<BusinessComponent> getSubBusinessComponentList() {
		return Collections.unmodifiableList(this.subBusinessComponentList);
	}

	@Override
	public void addSubBusinessComponentList(BusinessComponent businessComponent) {
		this.subBusinessComponentList.add(businessComponent);
	}

	@Override
	public ExecuteLayer getExecuteLayer() {
		return ExecuteLayer.MODULE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleDir == null) ? 0 : moduleDir.hashCode());
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + ((projectPath == null) ? 0 : projectPath.hashCode());
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
		ModuleExecuteUnit other = (ModuleExecuteUnit) obj;
		if (moduleDir == null) {
			if (other.moduleDir != null)
				return false;
		} else if (!moduleDir.equals(other.moduleDir))
			return false;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (projectPath == null) {
			if (other.projectPath != null)
				return false;
		} else if (!projectPath.equals(other.projectPath))
			return false;
		return true;
	}

	@Override
	public String getProduct() {
		return productCode;
	}

}
