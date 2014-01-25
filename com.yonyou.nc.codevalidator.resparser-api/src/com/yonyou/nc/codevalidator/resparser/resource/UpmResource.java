package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.sdk.upm.UpmModuleVO;
import com.yonyou.nc.codevalidator.sdk.upm.UpmOperateException;
import com.yonyou.nc.codevalidator.sdk.upm.UpmUtils;

public class UpmResource extends AbstractFileResource {

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.UPM;
	}

	public UpmModuleVO getUpmModuleVo() throws ResourceParserException {
		try {
			return UpmUtils.loadUpmFile(resourcePath);
		} catch (UpmOperateException e) {
			throw new ResourceParserException(e);
		}
	}

}
