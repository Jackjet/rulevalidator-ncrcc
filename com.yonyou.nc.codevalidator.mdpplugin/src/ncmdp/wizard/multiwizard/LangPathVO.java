package ncmdp.wizard.multiwizard;

import ncmdp.tool.basic.StringUtil;
import ncmdp.wizard.multiwizard.util.MultiUtils;

public class LangPathVO {
	private String filePath;

	private String langType;

	private String charsetName;

	private boolean isDefault;

	public LangPathVO(String filePath, String langType, String charsetName, boolean isDefault) {
		this.filePath = filePath;
		this.langType = langType;
		this.isDefault = isDefault;
		if (!StringUtil.isEmptyWithTrim(charsetName)) {
			this.charsetName = charsetName;
		} else {
			if (MultiUtils.SIMP_TAG.equalsIgnoreCase(langType)) {
				this.charsetName = "GBK";
			} else if (MultiUtils.TRAD_TAG.equalsIgnoreCase(langType)) {
				this.charsetName = "UTF-16";
			} else if (MultiUtils.ENG_TAG.equalsIgnoreCase(langType)) {
				this.charsetName = "GBK";
			}
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLangType() {
		return langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return langType + ":" + charsetName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof LangPathVO)) { return false; }
		if (StringUtil.isEmptyWithTrim(langType)) { return false; }
		return langType.equals(((LangPathVO) obj).getLangType());
	}

	@Override
	public int hashCode() {
		return langType.hashCode();
	}
}
