package com.yonyou.nc.codevalidator.resparser.file;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.IResourceVisitorManager;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.resource.BusinessComponentResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class FileResourceVisitorManager implements IResourceVisitorManager {

	@Override
	public void visitResource(BusinessComponent businessComponent, IResourceVisitor resourceVisitor)
			throws ResourceParserException {
		resourceVisitor.visit(new BusinessComponentResource(businessComponent));
	}

}
