package com.yonyou.nc.codevalidator.runtime.plugin.celleditor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;

public interface ICellEditorDescriptor {
	
	CellEditor getCellEditor(TableViewer tableViewer);
	
	String getPropertyValue();
	
	void setPropertyValue(String propertyValue);

}
