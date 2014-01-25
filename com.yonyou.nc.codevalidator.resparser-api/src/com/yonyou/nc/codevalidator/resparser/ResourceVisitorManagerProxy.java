package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class ResourceVisitorManagerProxy implements IResourceVisitorManager {

	private static IResourceVisitorManager resourceVisitorManager;

	public void setResourceVisitorManager(IResourceVisitorManager resourceVisitorManager) {
		ResourceVisitorManagerProxy.resourceVisitorManager = resourceVisitorManager;
	}

	@Override
	public void visitResource(BusinessComponent businessComponent, IResourceVisitor resourceVisitor)
			throws ResourceParserException {
		resourceVisitorManager.visitResource(businessComponent, resourceVisitor);
	}

}
