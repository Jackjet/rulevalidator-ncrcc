package com.web.pub.comp.combobox;

/**
 * 下拉框选项
 * 
 * @author:孟志昂
 * @date:2013-1-18 下午02:25:43
 */
public class ComboBoxItem implements IComboBoxItem {
	private String name;
	private String code;

	/**
	 * @author:孟志昂
	 * @date:2013-1-21 上午10:54:12
	 * @see com.web.pub.comp.combobox.IComboBoxItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @author:孟志昂
	 * @date:2013-1-21 上午10:54:12
	 * @see com.web.pub.comp.combobox.IComboBoxItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @author:孟志昂
	 * @date:2013-1-21 上午10:54:12
	 * @see com.web.pub.comp.combobox.IComboBoxItem#getCode()
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @author:孟志昂
	 * @date:2013-1-21 上午10:54:12
	 * @see com.web.pub.comp.combobox.IComboBoxItem#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		this.code = code;
	}

	public ComboBoxItem(String code, String name) {
		super();
		this.name = name;
		this.code = code;
	}
}
