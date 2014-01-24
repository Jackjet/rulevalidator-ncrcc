package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.yonyou.nc.codevalidator.runtime.plugin.Activator;

/**
 * 刷新结果的相关操作
 * @author mazhqa
 * @since V1.0
 */
public class RefreshResultAction extends Action {

	private RuleResultDisplayComposite ruleResultDisplayComposite;

	public RefreshResultAction(RuleResultDisplayComposite ruleResultDisplayComposite) {
		this.ruleResultDisplayComposite = ruleResultDisplayComposite;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("/images/refresh.gif");
	}

	@Override
	public void run() {
		ruleResultDisplayComposite.refreshTreeViewerData();
	}

}
