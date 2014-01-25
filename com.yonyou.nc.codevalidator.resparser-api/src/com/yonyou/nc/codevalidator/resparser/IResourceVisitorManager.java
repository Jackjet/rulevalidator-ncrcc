package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public interface IResourceVisitorManager {

	/**
	 * Visitor mode to access resources in businessComponent.
	 * 
	 * @param businessComponent
	 * @param resourceVisitor
	 * @throws ResourceParserException
	 */
	void visitResource(BusinessComponent businessComponent, IResourceVisitor resourceVisitor)
			throws ResourceParserException;

}
