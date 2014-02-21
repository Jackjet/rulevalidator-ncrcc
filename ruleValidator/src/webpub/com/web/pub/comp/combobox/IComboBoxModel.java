package com.web.pub.comp.combobox;

import java.util.List;

public interface IComboBoxModel {

	public abstract List<IComboBoxItem> getItems();

	public abstract void setItems(List<IComboBoxItem> items);

	public abstract void addItem(IComboBoxItem item);


}