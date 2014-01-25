package com.yonyou.nc.codevalidator.runtime.plugin.editor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

/**
 * �ձ��༭��֧�֣�����Ϊ��editorSupport���в��ܽ��б༭����
 * @author mazhqa
 * @since V2.3
 */
public class EmptyEditingSupport extends EditingSupport {

	public EmptyEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		return false;
	}

	@Override
	protected Object getValue(Object element) {
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {

	}

}
