package com.web.rule.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rv_execute_unit")
public class ExecuteUnitVO implements Serializable, IEntityIdentifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8589410288970291965L;

	@Id
	@Column(name = "unitid", length = 50)
	private String unitId;
	/**
	 * 产品编码
	 */
	@Column(name = "prod_code", length = 20)
	private String prodCode;
	/**
	 * 产品名称
	 */
	@Column(name = "prod_name", length = 20)
	private String prodName;
	/**
	 * 模块编码
	 */
	@Column(name = "module_code", length = 20)
	private String moduleCode;
	/**
	 * 模块名称
	 */
	@Column(name = "module_name", length = 20)
	private String moduleName;

	/**
	 * 业务组件编码
	 */
	@Column(name = "comp_code", length = 20)
	private String compCode;
	/**
	 * 组件名称
	 */
	@Column(name = "comp_name", length = 20)
	private String compName;
	/**
	 * 文件
	 */
	@Column(name = "related_file", length = 20)
	private String file;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
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
		ExecuteUnitVO other = (ExecuteUnitVO) obj;
		if (unitId == null) {
			if (other.unitId != null)
				return false;
		} else if (!unitId.equals(other.unitId))
			return false;
		return true;
	}

	@Override
	public String getId() {
		return unitId;
	}

}
