package com.yonyou.nc.codevalidator.resparser.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.IXmlResourceQueryFactory;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class XmlResourceQueryFactory implements IXmlResourceQueryFactory {

	public static final IXmlResourceQueryFactory INSTANCE = new XmlResourceQueryFactory();

	@Override
	public ResourceType getType() {
		return ResourceType.XML;
	}

	@Override
	public List<XmlResource> getResource(XmlResourceQuery resourceQuery) throws RuleBaseException {
		Map<String, String> configFiles = resourceQuery.getFuncNodeToConfigPathMap();
		List<XmlResource> result = new ArrayList<XmlResource>();
		for (Map.Entry<String, String> entry : configFiles.entrySet()) {
			String resourceRelativePath = entry.getValue();
			String funcNodeCode = entry.getKey();
			XmlResource xmlResource = new XmlResource(resourceRelativePath, funcNodeCode,resourceQuery.getBusinessComponent());
			result.add(xmlResource);
		}
		return result;
	}

}
