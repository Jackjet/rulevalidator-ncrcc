package com.yonyou.nc.codevalidator.resparser.resource;

import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;

public class ScriptDataSetResource extends AbstractQueryResource {

	public static final String DEFAULTDATASETNAME = "defaultDataSet";

	public ScriptDataSetResource(Map<String, Object> resourceObjectMap) {
		super(resourceObjectMap);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.SCRIPT;
	}

	public DataSet getDataSet() {
		return (DataSet) getResourceObjectMap().get(DEFAULTDATASETNAME);
	}
}
