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
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "����׷�ݴ�ʱ������ģ̬���桾��nc.ui.pubapp.uif2app.actions.LinkQueryAction������openMode = 1����", coder = "wangfra", relatedIssueId = "206")
public class TestCase00206 extends AbstractXmlRuleDefinition {

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		return new XmlResourceQuery(functionNodes,
				ruleExecContext.getBusinessComponent());
	}

	@Override
	protected IRuleExecuteResult processScriptRules(
			IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (XmlResource xmlResource : resources) {
			List<Element> list = xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.actions.LinkQueryAction");
			if (list == null || list.size() == 0) {
				result.addResultElement(xmlResource.getResourcePath(),"xmlδ����-LinkQueryAction");
			}
			for (Element element : list) {
				Element e = xmlResource.getChildPropertyElement(element,"openMode");
				if (e == null || !"1".equals(e.getAttribute("value"))) {// ���û��ע�����openMode.����ֵ����1,���ǵ���׷��,�������鵥��
					result.addResultElement(xmlResource.getResourcePath(),"����׷�ݰ�ťû��ע�����openMode,ֵΪ1 \n");
				}
			}
		}
		return result;
	}
}
