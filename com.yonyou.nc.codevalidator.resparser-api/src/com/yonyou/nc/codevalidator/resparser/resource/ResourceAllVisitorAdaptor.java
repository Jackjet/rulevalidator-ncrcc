package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;

/**
 * Visit all resources below the component, you can specify each type to visit it.
 * @author mazhqa
 *
 */
public class ResourceAllVisitorAdaptor extends AbstractResourceVisitorAdaptor {

	@Override
	public void visit(JavaClassSrcResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

	@Override
	public void visit(JavaClassPublicResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

	@Override
	public void visit(JavaClassPrivateResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

	@Override
	public void visit(JavaClassClientResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

	@Override
	public void visit(MetadataFolderResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

	@Override
	public void visit(MetainfFolderResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

	@Override
	public void visit(ResourceFolderResource resource) throws ResourceParserException {
		visitComposite(resource);
	}

}
