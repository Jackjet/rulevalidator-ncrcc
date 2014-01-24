package com.yonyou.nc.codevalidator.resparser.resource;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * The resource collection, has sub resources below it.
 * 
 * @author mazhqa
 * 
 */
public abstract class CompositeResource extends AbstractFileResource {

	protected BusinessComponent businessComponent;

	public CompositeResource(BusinessComponent businessComponent, String resourcePath) {
		this.businessComponent = businessComponent;
		this.resourcePath = resourcePath;
	}

	public abstract List<IResource> getSubResources() throws ResourceParserException;

	@Override
	public ResourceType getResourceType() {
		return ResourceType.COMPOSITE;
	}

}
