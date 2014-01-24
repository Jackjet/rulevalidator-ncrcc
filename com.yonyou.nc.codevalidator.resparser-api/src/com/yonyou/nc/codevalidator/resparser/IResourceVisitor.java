package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.resparser.resource.BusinessComponentResource;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassClientResource;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassPrivateResource;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassPublicResource;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassSrcResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataFolderResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetainfFolderResource;
import com.yonyou.nc.codevalidator.resparser.resource.PropertiesResource;
import com.yonyou.nc.codevalidator.resparser.resource.ResourceFolderResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;

public interface IResourceVisitor {
	
	//detail resource
	
	void visit(JavaClassResource resource) throws ResourceParserException;
	
	void visit(MetadataResource resource) throws ResourceParserException;
	
	void visit(UpmResource resource) throws ResourceParserException;
	
	void visit(XmlResource resource) throws ResourceParserException;
	
	void visit(PropertiesResource propertiesResource) throws ResourceParserException;
	
	//composite resource

	void visit(BusinessComponentResource resource) throws ResourceParserException;

	void visit(JavaClassSrcResource resource) throws ResourceParserException;
	
	void visit(JavaClassPublicResource resource) throws ResourceParserException;
	
	void visit(JavaClassPrivateResource resource) throws ResourceParserException;
	
	void visit(JavaClassClientResource resource) throws ResourceParserException;
	
	void visit(MetadataFolderResource resource) throws ResourceParserException;
	
	void visit(MetainfFolderResource resource) throws ResourceParserException;
	
	void visit(ResourceFolderResource resource) throws ResourceParserException;
	

}
