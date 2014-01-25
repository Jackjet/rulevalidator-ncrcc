package com.yonyou.nc.codevalidator.resparser.function.utils;

/**
 * 用于保存NC模块vo（即NC的ModuleVO）以及功能分类（即FuncRegisterVO中isfunctype为"Y"的）的部分信息
 * 
 * @author zhangwch1
 * 
 */
public class NCModuleVO {

	private final String key;
	private final String modulename;
	private final String modulecode;
	private final String parentmodulecode;

	public NCModuleVO(String key, String modulename, String modulecode, String parentmodulecode) {
		super();
		this.key = key;
		this.modulename = modulename;
		this.modulecode = modulecode;
		this.parentmodulecode = parentmodulecode;
	}

	public String getModulename() {
		return modulename;
	}

	public String getModulecode() {
		return modulecode;
	}

	public String getParentmodulecode() {
		return parentmodulecode;
	}

	public String getKey() {
		return key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((modulecode == null) ? 0 : modulecode.hashCode());
		result = prime * result + ((modulename == null) ? 0 : modulename.hashCode());
		result = prime * result + ((parentmodulecode == null) ? 0 : parentmodulecode.hashCode());
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
		NCModuleVO other = (NCModuleVO) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (modulecode == null) {
			if (other.modulecode != null)
				return false;
		} else if (!modulecode.equals(other.modulecode))
			return false;
		if (modulename == null) {
			if (other.modulename != null)
				return false;
		} else if (!modulename.equals(other.modulename))
			return false;
		if (parentmodulecode == null) {
			if (other.parentmodulecode != null)
				return false;
		} else if (!parentmodulecode.equals(other.parentmodulecode))
			return false;
		return true;
	}

}
