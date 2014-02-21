package com.web.pub.comp.combobox;

import java.util.ArrayList;
import java.util.List;

import com.web.pub.comp.common.BaseModel;

/**
 * 下拉框模型
 * 
 * @author:孟志昂
 * @date:2013-1-18 下午02:25:40
 */
public class ComboBoxModel extends BaseModel implements IComboBoxModel {
	private List<IComboBoxItem> items = new ArrayList<IComboBoxItem>();

	/** 
	 * @author:孟志昂
	 * @date:2013-1-21 上午11:11:48 
	 * @see com.web.pub.comp.combobox.IComboBoxModel#getItems()
	 */
	@Override
	public List<IComboBoxItem> getItems() {
		return items;
	}

	/** 
	 * @author:孟志昂
	 * @date:2013-1-21 上午11:11:48 
	 * @see com.web.pub.comp.combobox.IComboBoxModel#setItems(java.util.List)
	 */
	@Override
	public void setItems(List<IComboBoxItem> items) {
		this.items = items;
	}

	/** 
	 * @author:孟志昂
	 * @date:2013-1-21 上午11:11:48 
	 * @see com.web.pub.comp.combobox.IComboBoxModel#addItem(com.web.pub.comp.combobox.IComboBoxItem)
	 */
	@Override
	public void addItem(IComboBoxItem item) {
		items.add(item);
	}
}
