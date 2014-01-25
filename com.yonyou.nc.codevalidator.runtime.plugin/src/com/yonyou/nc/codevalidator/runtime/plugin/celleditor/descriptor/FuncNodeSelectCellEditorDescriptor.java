package com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor;

import com.yonyou.nc.codevalidator.runtime.plugin.celleditor.IDialogComposite;

/**
 * NC功能节点选择的celleditor
 * @author mazhqa
 *
 */
public class FuncNodeSelectCellEditorDescriptor extends AbstractRefPaneCellEditorDescriptor {

	@Override
	protected IDialogComposite getDialogComposite() {
		return new FuncNodeSelectDialogComposite();
	}


}
