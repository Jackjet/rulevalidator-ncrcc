package com.yonyou.nc.codevalidator.resparser.resource;

import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.ResourceType;

public class ScriptResource extends AbstractQueryResource {

	public ScriptResource(Map<String, Object> resourceObjectMap) {
		super(resourceObjectMap);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.SCRIPT;
	}

}
