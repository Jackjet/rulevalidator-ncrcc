package com.yonyou.nc.codevalidator.resparser;

public final class ResourceManagerFactory {

	private ResourceManagerProxy resourceParser;

	private static final ResourceManagerFactory INSTANCE = new ResourceManagerFactory();

	private ResourceManagerFactory() {
		resourceParser = new ResourceManagerProxy();
	}

	public static ResourceManagerFactory getInstance() {
		return INSTANCE;
	}

	public IResourceManager getResourceManager() {
		return resourceParser;
	}

}
