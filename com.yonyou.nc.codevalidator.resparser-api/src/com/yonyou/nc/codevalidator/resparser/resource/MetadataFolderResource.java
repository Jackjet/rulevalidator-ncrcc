package com.yonyou.nc.codevalidator.resparser.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class MetadataFolderResource extends CompositeResource {

	public MetadataFolderResource(BusinessComponent businessComponent) {
		super(businessComponent, String.format("%s/METADATA", businessComponent.getBusinessComponentPath()));
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public List<IResource> getSubResources() {
		List<IResource> result = new ArrayList<IResource>();
		@SuppressWarnings("rawtypes")
		Collection bmfFiles = FileUtils.listFiles(new File(resourcePath), new String[] { "bmf" }, true);
		for (Object bmfFileObject : bmfFiles) {
			File bmfFile = (File) bmfFileObject;
			MetadataResource metaResource = new MetadataResource();
			metaResource.setResourcePath(bmfFile.getAbsolutePath());
			result.add(metaResource);
		}
		return result;
	}

}
