package com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor;

import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.IDialogComposite;

/**
 * NC���ܽڵ�ѡ���celleditor
 * @author mazhqa
 *
 */
public class FuncNodeSelectCellEditorDescriptor extends AbstractRefPaneCellEditorDescriptor {

	@Override
	protected IDialogComposite getDialogComposite() {
		return new FuncNodeSelectDialogComposite();
	}


}
