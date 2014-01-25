package com.yonyou.nc.codevalidator.plugin.domain.dmm.uif;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

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

@RuleDefinition(catalog = CatalogEnum.UIFACTORY, coder = "muxh", description = "配置文件queryAction必须配置nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel检测规则（查询结果信息栏）", relatedIssueId = "810", subCatalog = SubCatalogEnum.UF_LAYOUT)
public class TestCase00810 extends AbstractXmlRuleDefinition {

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes,
				ruleExecContext.getBusinessComponent());
		return xmlResQry;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(
			IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		List<String> unlawfulNodes = new ArrayList<String>();

		if (resources == null || resources.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "请检查输入的功能编码是否正确！\n");
			return result;
		}

		for (XmlResource xmlResource : resources) {
			List<Element> queryinfo = xmlResource
					.getBeanElementByClass("nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel");
			List<Element> containers = xmlResource
					.getBeanElementByClass("nc.ui.uif2.TangramContainer");
			
			if(queryinfo==null||queryinfo.isEmpty()||containers==null||containers.size()!=1){
				unlawfulNodes.add(xmlResource.getFuncNodeCode());
			}
			//TODO 简单判断是否有nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel的bean并不准确，待完善。
//			Element container=containers.get(0);
//			Element tangramLayoutRoot=xmlResource.getChildPropertyElement(container, "tangramLayoutRoot");
//			xmlResource.getChildElementsByTagName(container, "property");
//			for(Element ele:queryinfo){
//				String name=ele.getNodeName();
//				String id=ele.getAttribute("id");
//				//配置ref形式。
//				if(name.equals("bean")){			
//					xmlResource.getChildPropertyElement(container, id);
//					Element cNode=this.getQueryInfoArea(container);
//				}
//				//直接配置了属性 TODO
//				else if(name.equals("property")){
//					
//				}
//			}
		}
		if (unlawfulNodes.size() > 0) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), String.format(
					"功能编码为：%s 的节点配置文件中没有配置[查询结果信息栏]：%s \n", new Object[] {
							unlawfulNodes, "CardLayoutToolbarPanel" }));
		}
		return result;
	}

//	private Element getQueryInfoArea(Element container) {
//		
//		container.getAttributeNode("component");
//		container.getElementsByTagName("tabs");
//		return null;
//	}

}
