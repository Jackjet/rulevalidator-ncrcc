package com.yonyou.nc.codevalidator.resparser.function.utils;

/**
 * 用于保存NC可执行功能（即FuncRegisterVO中isfunctype为"N"的）的部分信息
 * 
 * @author zhangwch1
 * 
 */
public class NCFunnodeVO {

	private final String key;
	private final String funname;
	private final String funcode;
	private final String parentcode;
	
	public NCFunnodeVO(String key, String funname, String funcode, String parentcode) {
		super();
		this.key = key;
		this.funname = funname;
		this.funcode = funcode;
		this.parentcode = parentcode;
	}

	public String getKey() {
		return key;
	}

	public String getFunname() {
		return funname;
	}

	public String getFuncode() {
		return funcode;
	}

	public String getParentcode() {
		return parentcode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funcode == null) ? 0 : funcode.hashCode());
		result = prime * result + ((funname == null) ? 0 : funname.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((parentcode == null) ? 0 : parentcode.hashCode());
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
		NCFunnodeVO other = (NCFunnodeVO) obj;
		if (funcode == null) {
			if (other.funcode != null)
				return false;
		} else if (!funcode.equals(other.funcode))
			return false;
		if (funname == null) {
			if (other.funname != null)
				return false;
		} else if (!funname.equals(other.funname))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (parentcode == null) {
			if (other.parentcode != null)
				return false;
		} else if (!parentcode.equals(other.parentcode))
			return false;
		return true;
	}
	
}
