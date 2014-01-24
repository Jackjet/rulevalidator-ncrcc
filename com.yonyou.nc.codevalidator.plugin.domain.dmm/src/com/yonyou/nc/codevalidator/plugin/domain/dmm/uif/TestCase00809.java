package com.yonyou.nc.codevalidator.plugin.domain.dmm.uif;

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
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, coder = "muxh", description = "����Ҫ�ϼ�����Ҫ�������ļ��н�������", 
relatedIssueId = "809", subCatalog = SubCatalogEnum.UF_CONFIGFILE)
/**
 * ��ѡ�˹������ڼ�⿨Ƭ�Ƿ������˲���ʾ�ͼ��У���û��ʾ���ã����״�����ʾ��
 */
public class TestCase00809 extends AbstractXmlRuleDefinition{

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


		if (resources == null || resources.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "��������Ĺ��ܱ����Ƿ���ȷ��\n");
			return result;
		}

		for (XmlResource xmlResource : resources) {
//			List unlawfulNodes = new ArrayList();
			List<Element> billForm = xmlResource
					.getBeanElementByClass("nc.ui.uif2.editor.BillForm");						
			if(billForm==null||billForm.isEmpty()){
//				unlawfulNodes.add(xmlResource.getFuncNodeCode());
				continue;
			}
			for(Element ele:billForm){
				Element isShowTotal=xmlResource.getChildPropertyElement(ele, "showTotalLine");
				//���û���ã���Ϊtrue.�׳�����쳣
				if(isShowTotal==null || isShowTotal.getAttribute("value").equals("true")){
//					unlawfulNodes.add(xmlResource.getFuncNodeCode());
//				}
//				else if(){
//					unlawfulNodes.add(xmlResource.getFuncNodeCode());
					result.addResultElement(xmlResource.getResourcePath(), "����Ҫ�ϼ�����Ҫ�������ļ��н�������");
				}
			}
		}
//		if (unlawfulNodes.size() > 0) {
//			result.addResultElement(ruleExecContext.getBusinessComponent()
//					.getDisplayBusiCompName(), String.format(
//					"���ܱ���Ϊ��%s �Ľڵ������ļ���,��Ҫ����ʾ�ϼ��У�������isShowTotalLineΪfalse\n", new Object[] {
//							unlawfulNodes }));
//		}
		return result;

	}

}
