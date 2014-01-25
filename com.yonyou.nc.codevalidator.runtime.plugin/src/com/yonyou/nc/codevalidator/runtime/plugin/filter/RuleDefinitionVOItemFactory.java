package com.yonyou.nc.codevalidator.runtime.plugin.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IMemento;

public class RuleDefinitionVOItemFactory implements IFilteredItemPersistenceFactory {

	private static final String ID = "id";

	private Map<String, RuleDefinitionVOItem> id2VoDefMap = new HashMap<String, RuleDefinitionVOItem>();

	@Override
	public IFilteredItem createElement(IMemento memento) {
		String compId = memento.getString(ID);
		return id2VoDefMap.get(compId);
	}

	@Override
	public void saveState(IFilteredItem item, IMemento memento) {
		if (item instanceof RuleDefinitionVOItem) {
			RuleDefinitionVOItem ruleDefinitionVoItem = (RuleDefinitionVOItem) item;
			memento.putString(ID, ruleDefinitionVoItem.getIdentifier());
		}
	}

	public void addFilteredItem(List<IFilteredItem> filteredItems) {

		if (filteredItems != null && filteredItems.size() > 0) {
			for (IFilteredItem filteredItem : filteredItems) {
				if (filteredItem instanceof RuleDefinitionVOItem) {
					RuleDefinitionVOItem ruleDefinitionVoItem = (RuleDefinitionVOItem) filteredItem;
					id2VoDefMap.put(filteredItem.getIdentifier(), ruleDefinitionVoItem);
				}
			}
		}
	}

}
