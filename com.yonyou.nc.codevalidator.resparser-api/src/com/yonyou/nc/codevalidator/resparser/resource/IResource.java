package com.yonyou.nc.codevalidator.resparser.resource;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;

/**
 * Resource from resource manager.
 * 
 * @author mazhqa
 * 
 */
public interface IResource {

	// /**
	// * get which business component this resource belongs to.
	// * @return
	// */
	// BusinessComponent getBusinessComponent();

	/**
	 * get resource path.
	 * <p>
	 * the path may not relate to local file, db, middleware also need the path
	 * to represent resource.
	 * 
	 * @return
	 */
	String getResourcePath();

	ResourceType getResourceType();

//	enum ResourceType {
//		COMPOSITE, JAVA, METADATA, SCRIPT, PROPERTIES, UPM, XML
//	}

	/**
	 * Visitor mode to access resources.
	 * 
	 * @param resourceVisitor
	 */
	void accept(IResourceVisitor resourceVisitor) throws ResourceParserException;

}
