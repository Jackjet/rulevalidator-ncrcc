package com.yonyou.nc.codevalidator.resparser.lang;

/**
 * 多语资源类型
 * 
 * @author mazhqa
 * @since V2.3
 */
public enum I18nType {

	SIMP(1), TRAD(2), ENGLISH(0);

	private int index;

	I18nType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
