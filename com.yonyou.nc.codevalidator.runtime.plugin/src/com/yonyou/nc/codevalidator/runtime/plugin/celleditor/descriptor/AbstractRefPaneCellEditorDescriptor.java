package com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.IDialogComposite;
import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.RefPaneCellEditor;

/**
 * 默认的参照celleditor抽象类
 * @author mazhqa
 * @since V2.3
 */
public abstract class AbstractRefPaneCellEditorDescriptor extends AbstractCellEditorDescriptor {

	@Override
	public final CellEditor getCellEditor(TableViewer tableViewer) {
		RefPaneCellEditor refPaneCellEditor = new RefPaneCellEditor(tableViewer.getTable(), SWT.NONE, getPropertyValue(), getDialogComposite());
		
		return refPaneCellEditor;
	}
	
	protected abstract IDialogComposite getDialogComposite();

}
