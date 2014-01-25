package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;

/**
 * Default access none of the resources in the business component.
 * <p>
 * If you want to access specific resource, you should add its parent resources.
 * Current the relation is:
 * <p>
 * <b>JavaClassSrcResource</b>
 * <li>--JavaClassPublicResource(contains JavaClassResource)
 * <li>--JavaClassClientResource(contains JavaClassResource and XmlResource)
 * <li>--JavaClassPrivateResource(contains JavaClassResource)
 * <p>
 * <b>MetadataFolderResource</b>(contains MetadataFolderResource)
 * <li>--MetadataFolderResource
 * <p>
 * <b>MetainfFolderResource</b>(contains UpmResource)
 * <li>--UpmResource
 * <p>
 * <b>ResourceFolderResource</b>(contains PropertiesResource)
 * <li>--PropertiesResource
 * 
 * @author mazhqa
 * 
 */
public class ResourceNoneVisitorAdaptor extends AbstractResourceVisitorAdaptor {

	@Override
	public void visit(JavaClassSrcResource resource) throws ResourceParserException {

	}

	@Override
	public void visit(JavaClassPublicResource resource) throws ResourceParserException {

	}

	@Override
	public void visit(JavaClassPrivateResource resource) throws ResourceParserException {

	}

	@Override
	public void visit(JavaClassClientResource resource) throws ResourceParserException {

	}

	@Override
	public void visit(MetadataFolderResource resource) throws ResourceParserException {

	}

	@Override
	public void visit(MetainfFolderResource resource) throws ResourceParserException {

	}

	@Override
	public void visit(ResourceFolderResource resource) throws ResourceParserException {

	}

}
