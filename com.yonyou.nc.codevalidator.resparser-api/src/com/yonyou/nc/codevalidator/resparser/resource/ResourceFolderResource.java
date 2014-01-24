package com.yonyou.nc.codevalidator.resparser.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * NC resources usually contain multilanguage resources.
 * @author mazhqa
 *
 */
public class ResourceFolderResource extends CompositeResource {

	public ResourceFolderResource(BusinessComponent businessComponent) {
		super(businessComponent, String.format("%s/resources", businessComponent.getBusinessComponentPath()));
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public List<IResource> getSubResources() {
		List<IResource> result = new ArrayList<IResource>();
		@SuppressWarnings("rawtypes")
		Collection resources = FileUtils.listFiles(new File(resourcePath), new String[]{"properties"}, true);
		for (Object propertyResourceObj : resources) {
			File propertyResource = (File) propertyResourceObj;
			PropertiesResource propertiesResource = new PropertiesResource();
			propertiesResource.setResourcePath(propertyResource.getAbsolutePath());
			result.add(propertiesResource);
		}
		return result;
	}

}
