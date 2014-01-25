package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

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
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;

/**
 * ���水ť֧�ֲ���VO
 * 
 * @author qiaoyanga
 */
@RuleDefinition(
		executePeriod = ExecutePeriod.DEPLOY,
		catalog = CatalogEnum.JAVACODE,
		description = "���水ť֧�ֲ���VO",
		memo = "",
		solution = "���ù��ܽڵ�Ų������������õĽڵ���ҵ���Ӧ�������ļ���"
				+ "�ٽ��������ļ��ҳ�ʵ�ֲ���VO����'nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction'�������࣬��������ļ���δ�ҵ�ʵ�ֲ���VO��������ʾ������Ϣ",
		subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "191", coder = "qiaoyanga")
public class TestCase00191 extends AbstractXmlRuleDefinition {

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext) {
		XmlResourceQuery xmlResourceQuery = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
		xmlResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
		return xmlResourceQuery;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		IClassLoaderUtils iClassLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (XmlResource xmlResource : resources) {
			StringBuilder errorBuilder = new StringBuilder();
			List<Element> listActions = xmlResource.getCardActions();
			boolean hasSaveAction = false;
			for (Element element : listActions) {
				String classPath = element.getAttribute("class");

				// ��ȡ���水ť
				if (iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent().getProjectName(),
						classPath, "nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction")) {
					hasSaveAction = true;
					break;
				}

			}
			if (!hasSaveAction) {
				errorBuilder.append("û���ҵ���ȷ�ı��水ť��(nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction�ı����������)\n");
			}
			if (errorBuilder.toString().length() > 0) {
				result.addResultElement(xmlResource.getResourcePath(), errorBuilder.toString());
			}
		}
		return result;
	}
}
