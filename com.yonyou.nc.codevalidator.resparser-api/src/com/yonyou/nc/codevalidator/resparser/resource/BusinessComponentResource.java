package com.yonyou.nc.codevalidator.resparser.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class BusinessComponentResource extends CompositeResource {

	public BusinessComponentResource(BusinessComponent businessComponent) {
		super(businessComponent, businessComponent.getBusinessComponentPath());
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public List<IResource> getSubResources() {
		List<IResource> result = new ArrayList<IResource>();
		File busiCompFolder = new File(resourcePath);
		if (new File(busiCompFolder, "src").exists()) {
			result.add(new JavaClassSrcResource(businessComponent));
		}
		if (new File(busiCompFolder, "META-INF").exists()) {
			result.add(new MetainfFolderResource(businessComponent));
		}
		if (new File(busiCompFolder, "METADATA").exists()) {
			result.add(new MetadataFolderResource(businessComponent));
		}
		if (new File(busiCompFolder, "resources").exists()) {
			result.add(new ResourceFolderResource(businessComponent));
		}
		return result;
	}

}
