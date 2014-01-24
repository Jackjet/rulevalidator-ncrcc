package com.yonyou.nc.codevalidator.resparser.db;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.IMetaDataResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 
 * @author mazhqa
 * @deprecated - 当前使用文件而非数据库的资源查询工厂类
 */
public class MetaDataResourceQueryFactory implements IMetaDataResourceQueryFactory{

	@Override
	public ResourceType getType() {
		return ResourceType.METADATA;
	}

	@Override
	public List<MetadataResource> getResource(MetadataResourceQuery resourceQuery) throws RuleBaseException {
		
		return null;
	}

}
