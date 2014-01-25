package com.yonyou.nc.codevalidator.plugin.domain.mm.other;

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
 * @since 1.0
 * @version 1.0.0.0
 * @author wangfra
 * 
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "参考本领域主子孙实现统一方案。该方案的特征很多，这里只检查了：展示控件（list/card）应该继承自nc.ui.uif2.components.grand.GrandPanelComposite。", coder = "wangfra", relatedIssueId = "326")
public class TestCase00326 extends AbstractXmlRuleDefinition {

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
			boolean hasList = false;
			boolean hasCard = false;
			List<Element> actions = xmlResource
					.getBeanElementByClass("nc.ui.uif2.actions.StandAloneToftPanelActionContainer");
			for (Element element : actions) {
				if ("actionsOfList".equals(element.getAttribute("id"))) {
					hasList = true;
					this.check(ruleExecContext, element, xmlResource, result);
				}
				if ("actionsOfCard".equals(element.getAttribute("id"))) {
					hasCard = true;
					this.check(ruleExecContext, element, xmlResource, result);
				}
			}
			if (!hasList) {
				result.addResultElement(xmlResource.getResourcePath(),
						xmlResource.getResourceFileName()
								+ "中没有定义列表按钮的容器Bean:actionsOfList \n");
			}
			if (!hasCard) {
				result.addResultElement(xmlResource.getResourcePath(),
						xmlResource.getResourceFileName()
								+ "中没有定义卡片按钮的容器Bean:actionsOfCard \n");
			}
		}
		return result;
	}

	private void check(IRuleExecuteContext ruleExecContext, Element element,
			XmlResource xmlResource, ResourceRuleExecuteResult result)
			throws RuleBaseException {
		// xml解析类
		List<Element> constructorArg = xmlResource.getChildElementsByTagName(
				element, "constructor-arg");
		if (constructorArg == null || constructorArg.size() == 0) {
			result.addResultElement(xmlResource.getResourcePath(),
					element.getAttribute("id") + "没有配constructor-arg");
		}
		if (constructorArg.size() > 1) {
			result.addResultElement(xmlResource.getResourcePath(),
					element.getAttribute("id") + "的constructor-arg有多个");
		}
		String ref = constructorArg.get(0).getAttribute("ref");
		if ("".equals(ref)) {
			result.addResultElement(xmlResource.getResourcePath(), "按钮"
					+ element.getAttribute("id") + "的constructor-arg属性没有找到ref");
		}
		Element componentBean = xmlResource.getElementById(ref);
		if (componentBean == null) {
			result.addResultElement(xmlResource.getResourcePath(), "没有找到" + ref
					+ "的Bean");
		}
		String classPath = componentBean.getAttribute("class");
		if (classPath == null || "".equals(classPath)) {
			result.addResultElement(xmlResource.getResourcePath(), "没有找到类"
					+ classPath + "的class属性");
		}

		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory
				.getClassLoaderUtils();
		String projectName = ruleExecContext.getBusinessComponent()
				.getProjectName();
		if (!classLoaderUtils.isParentClass(projectName, classPath,
				"nc.ui.pubapp.uif2app.components.grand.GrandPanelComposite")) {
			result.addResultElement(xmlResource.getResourcePath(),
					"xml文件未支持InitDataListener");
		}

	}
}
