package com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * Ĭ�ϵ��ı��༭��celleditor
 * @author mazhqa
 * @since V2.3
 */
public class TextCellEditorDescriptor extends AbstractCellEditorDescriptor {

	@Override
	public CellEditor getCellEditor(TableViewer tableViewer) {
		return new TextCellEditor(tableViewer.getTable());
	}

}
