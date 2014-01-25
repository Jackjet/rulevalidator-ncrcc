package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.IIdentifier;

/**
 * Resource parser
 * @author mazhqa
 *
 */
public interface IResourceManager extends IIdentifier {

	
	
	@SuppressWarnings("rawtypes")
	IResourceQueryFactory getResourceQueryFactory(ResourceType resourceType);
	
	/*IJavaResourceQueryFactory getJavaResourceQueryFactory();
	
	IUpmResourceQueryFactory getUpmResourceQueryFactory();
	
	IScriptResourceQueryFactory getScriptResourceQueryFactory();*/
	

}
