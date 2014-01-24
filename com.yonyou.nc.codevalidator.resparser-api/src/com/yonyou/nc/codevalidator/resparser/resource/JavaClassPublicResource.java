package com.yonyou.nc.codevalidator.resparser.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class JavaClassPublicResource extends CompositeResource {

	public JavaClassPublicResource(BusinessComponent businessComponent) {
		super(businessComponent, businessComponent.getCodePath("public"));
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public List<IResource> getSubResources() {
		List<IResource> result = new ArrayList<IResource>();
		@SuppressWarnings("rawtypes")
		Collection publicResources = FileUtils.listFiles(new File(resourcePath), new String[]{"java"}, true);
		for (Object publicResourceObject : publicResources) {
			File publicResource = (File) publicResourceObject;
			JavaClassResource javaResource = new JavaClassResource();
			javaResource.setResourcePath(publicResource.getAbsolutePath());
			result.add(javaResource);
		}
		return result;
	}

}
