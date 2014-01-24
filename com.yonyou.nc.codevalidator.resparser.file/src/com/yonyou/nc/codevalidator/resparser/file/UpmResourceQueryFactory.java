package com.yonyou.nc.codevalidator.resparser.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.IUpmResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;

public class UpmResourceQueryFactory implements IUpmResourceQueryFactory {
	
	public static final IUpmResourceQueryFactory INSTANCE = new UpmResourceQueryFactory();
	
	@Override
	public ResourceType getType() {
		return ResourceType.UPM;
	}

	@Override 
	public List<UpmResource> getResource(UpmResourceQuery upmResourceQuery) {
		BusinessComponent businessComponent = upmResourceQuery.getBusinessComponent();
		String businessComponentPath = businessComponent.getBusinessComponentPath();
		File file = new File(businessComponentPath);
		@SuppressWarnings("rawtypes")
		Collection upmFiles = FileUtils.listFiles(file, new String[]{"upm"}, true);
		List<UpmResource> result = new ArrayList<UpmResource>();
		for (Object upmFileObject : upmFiles) {
			File upmFile = (File) upmFileObject;
			UpmResource upmResource = new UpmResource();
			upmResource.setResourcePath(upmFile.getAbsolutePath());
			result.add(upmResource);
		}
		return result;
	}

}
