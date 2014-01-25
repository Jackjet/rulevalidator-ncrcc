package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.yonyou.nc.codevalidator.runtime.plugin.Activator;

/**
 * 移除所有结果action
 * @author mazhqa
 * @since V2.5
 */
public class RemoveAllResultsAction extends Action {

	private RuleResultDisplayComposite ruleResultDisplayComposite;

	public RemoveAllResultsAction(RuleResultDisplayComposite ruleResultDisplayComposite) {
		this.ruleResultDisplayComposite = ruleResultDisplayComposite;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("/images/removeall.gif");
	}

	@Override
	public void run() {
		ruleResultDisplayComposite.removeAllResults();
	}
	
}
