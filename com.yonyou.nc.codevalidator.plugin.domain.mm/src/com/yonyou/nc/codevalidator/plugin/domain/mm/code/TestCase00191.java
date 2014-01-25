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
 * 保存按钮支持差异VO
 * 
 * @author qiaoyanga
 */
@RuleDefinition(
		executePeriod = ExecutePeriod.DEPLOY,
		catalog = CatalogEnum.JAVACODE,
		description = "保存按钮支持差异VO",
		memo = "",
		solution = "配置功能节点号参数，根据配置的节点号找到对应的配置文件，"
				+ "再解析配置文件找出实现差异VO的类'nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction'或其子类，如果配置文件中未找到实现差异VO的类则提示错误信息",
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

				// 获取保存按钮
				if (iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent().getProjectName(),
						classPath, "nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction")) {
					hasSaveAction = true;
					break;
				}

			}
			if (!hasSaveAction) {
				errorBuilder.append("没有找到正确的保存按钮类(nc.ui.pubapp.uif2app.actions.DifferentVOSaveAction的本身或者子类)\n");
			}
			if (errorBuilder.toString().length() > 0) {
				result.addResultElement(xmlResource.getResourcePath(), errorBuilder.toString());
			}
		}
		return result;
	}
}
