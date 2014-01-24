package com.yonyou.nc.codevalidator.runtime.plugin.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.yonyou.nc.codevalidator.runtime.plugin.Activator;

public class RuleConfigBusiCompAction extends Action {

	@Override
	public void run() {
		super.run();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("/images/editconfig.gif");
	}

}
