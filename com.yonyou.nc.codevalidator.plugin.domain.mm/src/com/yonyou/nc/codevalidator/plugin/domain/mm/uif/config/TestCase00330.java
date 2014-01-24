package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.config;


import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * @since 1.0
 * @version 1.0.0.0
 * @author wangfra
 *
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "����֧�ֱ���������󻯹��ܡ�", coder = "wangfra", relatedIssueId = "330")
public class TestCase00330  extends AbstractXmlRuleDefinition{

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		return new XmlResourceQuery(functionNodes,ruleExecContext.getBusinessComponent());
	}

	@Override
	protected IRuleExecuteResult processScriptRules(
			IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		 ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for(XmlResource xmlResource : resources){
			check(result, xmlResource);
		}
		
		return result;
	}

	/**
	 * ���XML
	 * @param result
	 * @param xmlResource
	 */
	private void check(ResourceRuleExecuteResult result, XmlResource xmlResource) {
		// ��鿨Ƭ�������󻯰�ť
		Element billFormEditor = xmlResource.getElementById("billFormEditor");
		if (null == billFormEditor) {
			 result.addResultElement(xmlResource.getResourcePath(),"û���ҵ�billFormEditor");
		}
		Element bodyLineActionsElement = xmlResource.getChildPropertyElement(
				billFormEditor, "bodyLineActions");
		if (null == bodyLineActionsElement) {
			 result.addResultElement(xmlResource.getResourcePath(),"��billFormEditor��û���ҵ�bodyLineActions��ֻҪ�б����Ҫ�и�����");
		}
		List<Element> bodyLineActionsElementList = xmlResource
				.getChildElementsByTagName(bodyLineActionsElement, "list");
		boolean hasMaxAction = false;
		if (bodyLineActionsElementList != null
				&& bodyLineActionsElementList.size() > 0) {
			List<Element> bodyLineActionList = xmlResource
					.getChildElementsByTagName(
							bodyLineActionsElementList.get(0), "bean");
			for (Element element : bodyLineActionList) {
				String className = element.getAttribute("class");
				if ("nc.ui.pubapp.uif2app.actions.DefaultBodyZoomAction"
						.equals(className)) {
					hasMaxAction = true;
				}
			}
		}
		// ���û����󻯰�ť���򱨴�
		if (!hasMaxAction) {
			result.addResultElement(xmlResource.getResourcePath(),"��billFormEditor��û���ҵ�������󻯰�ťDefaultBodyZoomAction");
		}
	}
	
}
