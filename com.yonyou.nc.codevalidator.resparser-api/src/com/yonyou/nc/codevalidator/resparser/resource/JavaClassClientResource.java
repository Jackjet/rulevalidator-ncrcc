package com.yonyou.nc.codevalidator.resparser.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class JavaClassClientResource extends CompositeResource {

	public JavaClassClientResource(BusinessComponent businessComponent) {
		super(businessComponent, businessComponent.getCodePath("client"));
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public List<IResource> getSubResources() throws ResourceParserException {
		List<IResource> result = new ArrayList<IResource>();
		File clientSrcFolder = new File(resourcePath);
		@SuppressWarnings("rawtypes")
		Collection clientResources = FileUtils.listFiles(clientSrcFolder, new String[] { "java", "xml" }, true);
		for (Object clientResourceObject : clientResources) {
			File clientResourceFile = (File) clientResourceObject;
			if (clientResourceFile.getName().endsWith(".java")) {
				JavaClassResource javaResource = new JavaClassResource();
				javaResource.setResourcePath(clientResourceFile.getAbsolutePath());
				result.add(javaResource);
			} else if (clientResourceFile.getName().endsWith(".xml")) {
//				XmlResource xmlResource = new XmlResource(clientResourceFile.getAbsolutePath(), businessComponent);
//				xmlResource.setResourcePath(clientResourceFile.getAbsolutePath());
//				result.add(xmlResource);
			}
		}
		return result;
	}

}
