package com.yonyou.nc.codevalidator.plugin.domain.am.uif;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, description = "xml配置文件中注入的属性值不应该出现中文", relatedIssueId = "671",
		subCatalog = SubCatalogEnum.UF_CONFIGFILE, coder = "xiepch")
public class TestCase00671 extends AbstractXmlRuleDefinition {

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		XmlResourceQuery xmlResourceQuery = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
		return xmlResourceQuery;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (XmlResource xmlResource : resources) {
			StringBuffer noteString = new StringBuffer();
			for (Element e : xmlResource.getElementsByTagName("property")) {
				// 针对赋值的多种方式
				if (!e.getAttribute("value").equals("")) {
					if (StringUtils.containsChineseCharacter(e.getAttribute("value"))) {
						noteString.append("<property name=\"" + e.getAttribute("name") + "\"   value=\""
								+ e.getAttribute("value") + "\" />         \n");
					}
				} else {
					if (e.getElementsByTagName("value").getLength() > 0) {
						// 存放有中文的value
						List<String> info = new ArrayList<String>();
						NodeList nodeList = e.getElementsByTagName("value");
						for (int i = 0; i < nodeList.getLength(); i++) {
							String value = nodeList.item(i).getTextContent();
							if (StringUtils.containsChineseCharacter(value)) {
								info.add(value);
							}
						}
						if (info.size() > 0) {
							noteString.append("<property name=\"" + e.getAttribute("name") + "\"  />下属value        \n");
							for (String value : info) {
								noteString.append("       <value>" + value + "</value>         \n");
							}
						}
					}
				}
			}
			if (noteString.length() > 0) {
				result.addResultElement(xmlResource.getResourcePath(), noteString.toString() + "含有中文        \n");
			}
		}
		return result;
	}

}
