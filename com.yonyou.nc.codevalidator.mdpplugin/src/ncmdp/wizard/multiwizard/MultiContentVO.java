package ncmdp.wizard.multiwizard;

import ncmdp.tool.basic.StringUtil;

/**
 * 多语资源信息 实体
 * @author dingxm   2010-8-6
 *
 */
public class MultiContentVO {
	private String resid = "";

	private String value = "";

	public MultiContentVO(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getResid() {
		return resid;
	}

	public void setResid(String resid) {
		this.resid = resid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MultiContentVO) {
			if (!StringUtil.isEmptyWithTrim(getValue())) {
				return getValue().equals(((MultiContentVO) obj).getValue());
			} else {
				if (StringUtil.isEmptyWithTrim(((MultiContentVO) obj).getValue())) { return true; }
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (StringUtil.isEmptyWithTrim(getValue())) { return 0; }
		return getValue().hashCode();
	}

}
