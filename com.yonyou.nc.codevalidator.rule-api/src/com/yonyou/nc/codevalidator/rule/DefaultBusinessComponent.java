package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;

/**
 * ����ִ�й������ҵ�����
 * <p>
 * ��ǰ���������С����Ϊҵ�������
 * <p>
 * ģ�鼶��Ҳ����Ϊҵ�������ֻ����ģ���ҵ����������µ��ص�:
 * <p>
 * <li>businessCompΪ��</li>
 * <li>���Եõ�����ҵ������б�getSubBusinessComponentList()</li>
 * 
 * @author mazhqa
 */
public class DefaultBusinessComponent implements BusinessComponent {

	/**
	 * ִ�е�Ԫ��Ӧ�Ĳ�Ʒ
	 */
	private final String productCode;
	/**
	 * �������ڵ�·��
	 */
	private final String projectPath;

	/**
	 * ҵ���������ģ������ƣ� ��ģ�������Ǵ�module.xml�ж�ȡ��
	 */
	private final String moduleName;

	/**
	 * ҵ���������
	 */
	private final String businessComp;

	/**
	 * ģ�������ļ�������
	 */
	private final String moduleDir;

	/**
	 * �ڲ����ʹ�ù�������������class
	 */
	private final String projectName;

	// /**
	// * ע������ģ�����ҵ������в���ʹ�ø����ԣ�
	// */
	// private List<BusinessComponent> subBusinessComponentList = new
	// ArrayList<BusinessComponent>();

//	/**
//	 * ��ǰ̨�������ƣ�������̨ʹ��
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
	 * ҵ��������ã�ǰ̨ʹ��
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
