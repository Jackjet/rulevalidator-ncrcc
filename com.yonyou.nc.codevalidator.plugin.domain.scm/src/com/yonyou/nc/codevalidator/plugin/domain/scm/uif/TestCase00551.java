package com.yonyou.nc.codevalidator.plugin.domain.scm.uif;

import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.UIFACTORY, coder = "hanlj3", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.DEPLOY, description = "普通标准界面的单据是否支持快捷方式查询", relatedIssueId = "551",
		subCatalog = SubCatalogEnum.UF_BTNOREVENT)
public class TestCase00551 extends AbstractXmlRuleDefinition {

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		String[] funcodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
		XmlResourceQuery result = new XmlResourceQuery(funcodes, ruleExecContext.getBusinessComponent());
		return result;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (XmlResource xmlResource : resources) {
			boolean flag = false;
			Element elementById = xmlResource.getElementById("InitDataListener");
			if (null == elementById) {
				result.addResultElement(xmlResource.getResourcePath(), "没有注册id为InitDataListener的bean");
				break;
			} else {
				List<Element> childrenEle = xmlResource.getChildElementsByTagName(elementById, "property");
				for (Element e : childrenEle) {
					String s = e.getAttribute("name");
					if (s.equalsIgnoreCase("queryAction")) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					result.addResultElement(xmlResource.getResourcePath(), "InitDataListener中的bean没有注册queryAction属性");
				}
			}
			// elementById.getAttribute("class");
			//
			// elementById.getChildNodes();
			//
			// Class<?> loadClass =
			// ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(ruleExecContext.getBusinessComponent().getProjectName(),
			// "nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener");

		}
		return result;
	}

}
