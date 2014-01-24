package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.yonyou.nc.codevalidator.runtime.plugin.Activator;

/**
 * 移除当前选择的结果
 * @author mazhqa
 * @since V2.5
 */
public class RemoveSelectResultAction extends Action {
	
//	private RuleResultDisplayComposite ruleResultDisplayComposite;
//
//	public RemoveSelectResultAction(RuleResultDisplayComposite ruleResultDisplayComposite) {
//		this.ruleResultDisplayComposite = ruleResultDisplayComposite;
//	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("/images/deletesingle.gif");
	}

	@Override
	public void run() {
//		ruleResultDisplayComposite.removeSelectionResult();
	}

}
