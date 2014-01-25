package com.yonyou.nc.codevalidator.runtime.plugin.filter;

import java.util.List;

import org.eclipse.ui.IMemento;

/**
 * ���浽xml ��xml��ԭ��IFilteredItem
 */
public interface IFilteredItemPersistenceFactory {
	
	IFilteredItem createElement(IMemento memento);

	void saveState(IFilteredItem item, IMemento memento);
	
	void addFilteredItem(List<IFilteredItem> filteredItems);
}
