package com.yonyou.nc.codevalidator.resparser.resource;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;

/**
 * Query script based resource
 * 
 * @author mazhqa
 * 
 */
public abstract class AbstractQueryResource implements IResource {

	protected Map<String, Object> resourceObjectMap = new HashMap<String, Object>();

	public AbstractQueryResource(Map<String, Object> resourceObjectMap) {
		this.resourceObjectMap = resourceObjectMap;
	}

	@Override
	public String getResourcePath() {
		return null;
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		throw new UnsupportedOperationException();
	}

	public Map<String, Object> getResourceObjectMap() {
		return resourceObjectMap;
	}

}
