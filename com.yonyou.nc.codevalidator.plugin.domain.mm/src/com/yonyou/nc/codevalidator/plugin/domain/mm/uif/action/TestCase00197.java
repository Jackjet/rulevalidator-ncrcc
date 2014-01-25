package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

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
 * �б��е�ˢ�°�ť�Ϳ�Ƭ�е�ˢ�°�ťӦ��֧�ֲ�ͬ����Ϊ
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "�б��е�ˢ�°�ť�Ϳ�Ƭ�е�ˢ�°�ťӦ��֧�ֲ�ͬ����Ϊ��ʹ�õ�ˢ�°�ť��Ӧ�ò�һ����", coder = "qiaoyanga", relatedIssueId = "197")
public class TestCase00197 extends AbstractXmlRuleDefinition {
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
		StringBuffer noteBuilder = new StringBuffer();
		IClassLoaderUtils iClassLoaderUtils = ClassLoaderUtilsFactory
				.getClassLoaderUtils();
		for (XmlResource xmlResource : resources) {
			boolean hasRefreshAction = false;
			boolean hasCardRefreshAction = false;
			List<Element> listActions = xmlResource.getListActions();
			for (Element checkAction : listActions) {
				String classPath = checkAction.getAttribute("class");

				// ��ȡ��ť
				if (classPath != null
						&& iClassLoaderUtils
								.isExtendsParentClass(ruleExecContext
										.getBusinessComponent()
										.getProjectName(), classPath,
										"nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction")) {
					hasRefreshAction = true;
				}

			}

			List<Element> cardActions = xmlResource.getCardActions();
			for (Element checkAction : cardActions) {
				String classPath = checkAction.getAttribute("class");

				// ��ȡ��ť
				if (classPath != null
						&& iClassLoaderUtils
								.isExtendsParentClass(ruleExecContext
										.getBusinessComponent()
										.getProjectName(), classPath,
										"nc.ui.pubapp.uif2app.actions.RefreshSingleAction")) {
					hasCardRefreshAction = true;
				}

			}
			if (!hasRefreshAction) {
				noteBuilder.append(xmlResource.getResourcePath()
						+ "û���ҵ��б�ˢ�°�ť��refreshAction \n");
			}
			if (!hasCardRefreshAction) {
				noteBuilder.append(xmlResource.getResourcePath()
						+ "û���ҵ���Ƭˢ�°�ť��refreshCardAction \n");
			}

			if (noteBuilder.length() > 0) {
				result.addResultElement(xmlResource.getResourcePath(),
						noteBuilder.toString());
			}
		}
		return result;
	}
}
