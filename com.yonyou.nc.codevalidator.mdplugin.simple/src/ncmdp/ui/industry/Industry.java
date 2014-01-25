package ncmdp.ui.industry;

/**
 * 行业实体
 * 
 * @author dingxm
 */
public class Industry implements Cloneable {
	private String code;
	private String name;

	public Industry(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object clone() {
		Industry st = new Industry("", "");
		st.setCode(this.getCode());
		st.setName(this.getName());
		return st;
	}

	@Override
	public String toString() {
		return getName();
	}
}
