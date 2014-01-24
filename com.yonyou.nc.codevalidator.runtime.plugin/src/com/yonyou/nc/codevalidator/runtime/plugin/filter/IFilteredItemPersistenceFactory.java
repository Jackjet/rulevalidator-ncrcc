package com.yonyou.nc.codevalidator.runtime.plugin.filter;

import java.util.List;

import org.eclipse.ui.IMemento;

/**
 * 保存到xml 从xml还原成IFilteredItem
 */
public interface IFilteredItemPersistenceFactory {
	
	IFilteredItem createElement(IMemento memento);

	void saveState(IFilteredItem item, IMemento memento);
	
	void addFilteredItem(List<IFilteredItem> filteredItems);
}
