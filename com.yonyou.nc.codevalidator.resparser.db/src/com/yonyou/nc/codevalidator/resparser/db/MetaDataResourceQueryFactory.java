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
 * @deprecated - ��ǰʹ���ļ��������ݿ����Դ��ѯ������
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
