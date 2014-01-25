package com.yonyou.nc.codevalidator.resparser;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ResourceManagerProxy implements IResourceManager {

	private static Map<ResourceType, IResourceQueryFactory> resFactory = new HashMap<ResourceType, IResourceQueryFactory>();
	
	public ResourceManagerProxy(){
		
	}

	public void addResourceQueryFactory(IResourceQueryFactory resourceQueryFactory) {
		resFactory.put(resourceQueryFactory.getType(), resourceQueryFactory);
	}

	@Override
	public String getIdentifier() {
		return this.getClass().getName();
	}

	@Override
	public IResourceQueryFactory getResourceQueryFactory(ResourceType resourceType) {
		return resFactory.get(resourceType);
	}

}
