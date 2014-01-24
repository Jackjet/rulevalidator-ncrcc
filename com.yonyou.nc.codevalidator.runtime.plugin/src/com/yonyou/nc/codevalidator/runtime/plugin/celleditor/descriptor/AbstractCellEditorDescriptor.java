package com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor;

import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.ICellEditorDescriptor;

public abstract class AbstractCellEditorDescriptor implements ICellEditorDescriptor {
	
	protected String propertyValue;

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
}
