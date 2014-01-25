package com.yonyou.nc.codevalidator.resparser.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class MetainfFolderResource extends CompositeResource {

	public MetainfFolderResource(BusinessComponent businessComponent) {
		super(businessComponent, String.format("%s/META-INF", businessComponent.getBusinessComponentPath()));
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public List<IResource> getSubResources() {
		List<IResource> result = new ArrayList<IResource>();
		@SuppressWarnings("rawtypes")
		Collection upmFiles = FileUtils.listFiles(new File(resourcePath), new String[]{"upm"}, true);
		for (Object upmFileObject : upmFiles) {
			File upmFile = (File) upmFileObject;
			UpmResource upmResource = new UpmResource();
			upmResource.setResourcePath(upmFile.getAbsolutePath());
			result.add(upmResource);
		}
		return result;
	}

}
