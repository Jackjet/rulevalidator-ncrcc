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

@RuleDefinition(catalog = CatalogEnum.UIFACTORY, coder = "muxh", description = "�����ļ�queryAction��������nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel�����򣨲�ѯ�����Ϣ����", relatedIssueId = "810", subCatalog = SubCatalogEnum.UF_LAYOUT)
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
					.getDisplayBusiCompName(), "��������Ĺ��ܱ����Ƿ���ȷ��\n");
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
			//TODO ���ж��Ƿ���nc.ui.uif2.tangramlayout.CardLayoutToolbarPanel��bean����׼ȷ�������ơ�
//			Element container=containers.get(0);
//			Element tangramLayoutRoot=xmlResource.getChildPropertyElement(container, "tangramLayoutRoot");
//			xmlResource.getChildElementsByTagName(container, "property");
//			for(Element ele:queryinfo){
//				String name=ele.getNodeName();
//				String id=ele.getAttribute("id");
//				//����ref��ʽ��
//				if(name.equals("bean")){			
//					xmlResource.getChildPropertyElement(container, id);
//					Element cNode=this.getQueryInfoArea(container);
//				}
//				//ֱ������������ TODO
//				else if(name.equals("property")){
//					
//				}
//			}
		}
		if (unlawfulNodes.size() > 0) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), String.format(
					"���ܱ���Ϊ��%s �Ľڵ������ļ���û������[��ѯ�����Ϣ��]��%s \n", new Object[] {
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
