package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;

/**
 * 描述执行规则检查的业务组件
 * <p>
 * 当前规则检查的最小粒度为业务组件，
 * <p>
 * 模块级别也抽象为业务组件，只不过模块的业务组件有如下的特点:
 * <p>
 * <li>businessComp为空</li>
 * <li>可以得到所有业务组件列表getSubBusinessComponentList()</li>
 * 
 * @author mazhqa
 */
public class DefaultBusinessComponent implements BusinessComponent {

	/**
	 * 执行单元对应的产品
	 */
	private final String productCode;
	/**
	 * 工程所在的路径
	 */
	private final String projectPath;

	/**
	 * 业务组件所在模块的名称， 此模块名称是从module.xml中读取的
	 */
	private final String moduleName;

	/**
	 * 业务组件名称
	 */
	private final String businessComp;

	/**
	 * 模块所在文件夹名称
	 */
	private final String moduleDir;

	/**
	 * 在插件中使用工程名称来加载class
	 */
	private final String projectName;

	// /**
	// * 注：仅在模块而非业务组件中才能使用该属性！
	// */
	// private List<BusinessComponent> subBusinessComponentList = new
	// ArrayList<BusinessComponent>();

//	/**
//	 * 无前台工程名称，仅供后台使用
//	 * 
//	 * @param projectPath
//	 * @param moduleDir
//	 * @param moduleName
//	 * @param businessComp
//	 */
//	public DefaultBusinessComponent(String projectPath, String product, String moduleName, String moduleDir,
//			String businessComp) {
//		this.projectPath = projectPath;
//		this.productCode = product;
//		this.moduleName = moduleName;
//		this.moduleDir = moduleDir;
//		this.businessComp = businessComp;
//		this.projectName = "";
//	}

	/**
	 * 业务组件设置，前台使用
	 * 
	 * @param projectPath
	 * @param projectName
	 * @param moduleDir
	 * @param module
	 * @param businessComp
	 */
	public DefaultBusinessComponent(String projectPath, String projectName, String product, String moduleDir,
			String moduleName, String businessComp) {
		this.projectPath = projectPath;
		this.projectName = projectName;
		this.productCode = product;
		this.moduleName = moduleName;
		this.moduleDir = moduleDir;
		this.businessComp = businessComp;
	}

	// @Override
	// public void setProjectName(String projectName) {
	// this.projectName = projectName;
	// }

	@Override
	public String getModule() {
		return this.moduleName;
	}

	@Override
	public String getBusinessComp() {
		return this.businessComp;
	}

	@Override
	public String getProjectPath() {
		return this.projectPath;
	}

	@Override
	public String getProjectName() {
		return this.projectName;
	}

	@Override
	public String getDisplayBusiCompName() {
		return this.moduleName
				+ (this.businessComp != null && this.businessComp.trim().length() > 0 ? "-" + this.businessComp : "");
	}

	@Override
	public String getBusinessComponentPath() {
		return String.format("%s/%s/%s", this.projectPath, this.moduleDir, this.businessComp);
	}

	@Override
	public String getCodePath() {
		return String.format("%s/%s/%s/src", this.projectPath, this.moduleDir, this.businessComp);
	}

	/**
	 * Get each portion of the business component, for example public, private,
	 * client
	 * 
	 * @param codeFolder
	 * @return
	 */
	@Override
	public String getCodePath(String codeFolder) {
		return String.format("%s/%s/%s/src/%s", this.projectPath, this.moduleDir, this.businessComp, codeFolder);
	}

	// @Override
	// public String getResourcePath() {
	// return String.format("%s/%s/%s/resource", this.projectPath,
	// this.moduleDir, this.businessComp);
	// }

	// @Override
	// public String getMetadataPath() {
	// return String.format("%s/%s/%s/METADATA", this.projectPath,
	// this.moduleDir, this.businessComp);
	// }
	//
	// public String getMetainfPath() {
	// return String.format("%s/%s/%s/META-INF", this.projectPath,
	// this.moduleDir, this.businessComp);
	// }

	@Override
	public String toString() {
		return String.format("projectPath=%s, module=%s, businessComp=%s", this.projectPath, this.moduleDir,
				this.businessComp);
	}

	@Override
	public ExecuteLayer getExecuteLayer() {
		return ExecuteLayer.BUSICOMP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessComp == null) ? 0 : businessComp.hashCode());
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
		DefaultBusinessComponent other = (DefaultBusinessComponent) obj;
		if (businessComp == null) {
			if (other.businessComp != null)
				return false;
		} else if (!businessComp.equals(other.businessComp))
			return false;
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
