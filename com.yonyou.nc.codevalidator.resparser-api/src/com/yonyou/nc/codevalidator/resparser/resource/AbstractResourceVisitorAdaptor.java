package com.yonyou.nc.codevalidator.resparser.resource;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

public abstract class AbstractResourceVisitorAdaptor implements IResourceVisitor {

	@Override
	public void visit(JavaClassResource resource) throws ResourceParserException {
	}

	@Override
	public void visit(MetadataResource resource) throws ResourceParserException {
	}

	@Override
	public void visit(UpmResource resource) throws ResourceParserException {
	}

	@Override
	public void visit(XmlResource resource) throws ResourceParserException {
	}

	@Override
	public void visit(PropertiesResource propertiesResource) throws ResourceParserException {
		Logger.debug(String.format("resource :%s is scanned", propertiesResource.getResourcePath()));
	}

	@Override
	public final void visit(BusinessComponentResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

	protected final void visitComposite(CompositeResource compositeResource) throws ResourceParserException {
		List<IResource> subResources = compositeResource.getSubResources();
		for (IResource subResource : subResources) {
			subResource.accept(this);
		}
	}

}
