package com.yonyou.nc.codevalidator.resparser.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.yonyou.nc.codevalidator.resparser.AopResourceQuery;
import com.yonyou.nc.codevalidator.resparser.IAopResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.resource.AopResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * aop��Դ��ѯ�����࣬Ĭ�ϲ�ѯ�����е�aop�ļ�
 * @author mazhqa
 * @since V2.1
 */
public class AopResourceQueryFactory implements IAopResourceQueryFactory {

	@Override
	public ResourceType getType() {
		return ResourceType.AOP;
	}

	@Override
	public List<AopResource> getResource(AopResourceQuery resourceQuery) throws RuleBaseException {
		BusinessComponent businessComponent = resourceQuery.getBusinessComponent();
		String fileName = resourceQuery.getFileName();
		String businessComponentPath = businessComponent.getBusinessComponentPath();
		File file = new File(businessComponentPath);
		@SuppressWarnings("rawtypes")
		Collection aopFiles = FileUtils.listFiles(file, new String[]{"aop"}, true);
		List<AopResource> result = new ArrayList<AopResource>();
		for (Object aopFileObject : aopFiles) {
			File aopFile = (File) aopFileObject;
			AopResource aopResource = new AopResource();
			aopResource.setResourcePath(aopFile.getAbsolutePath());
			if(fileName != null && aopResource.getResourceFileName().equalsIgnoreCase(String.format("%s.aop", fileName))){
				result.add(aopResource);
			}
		}
		return result;
	}

}
