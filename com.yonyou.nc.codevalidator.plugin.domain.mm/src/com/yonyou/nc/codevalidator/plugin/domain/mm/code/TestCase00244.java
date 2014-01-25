package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;

/**
 * ��Ƭ����̳�ShowUpableBillForm��
 * @author gaojf
 *
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod =ExecutePeriod.DEPLOY,catalog = CatalogEnum.JAVACODE, specialParamDefine = {
	    ""
	},description = "��⿨Ƭ�����Ƿ�̳�ShowUpableBillForm�� ", relatedIssueId = "244", subCatalog = SubCatalogEnum.JC_CODECRITERION, coder = "gaojf")
public class TestCase00244 extends AbstractXmlRuleDefinition {

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
		
		if (MMValueCheck.isEmpty(resources)) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "��������Ĺ��ܱ����Ƿ���ȷ��\n");
			return result;
		}
		//�ҵ��̳�ShowUpableBillForm���࣬����̳���ͨ��������ͨ����
		for (XmlResource xmlResource : resources) {
			boolean isExtends = false;
			List<Element> eles = xmlResource.getElementsByTagName("bean");
			for(Element e:eles){	
				String className = e.getAttribute("class");
				className = className.trim();
				if(className==""||className==null){
					continue;
				}			
				isExtends = ClassLoaderUtilsFactory.getClassLoaderUtils().
						isExtendsParentClass(ruleExecContext.getBusinessComponent().getProjectName()
								, className, "nc.ui.pubapp.uif2app.view.ShowUpableBillForm");
				
				if(isExtends) {				
					break;
				}
			}
			if(!isExtends){
				result.addResultElement(ruleExecContext.getBusinessComponent()
						.getDisplayBusiCompName(),String.format(
								"���ܱ���Ϊ��%s �������ļ��У��б���û�м̳�ShowUpableBillForm. \n",
								xmlResource.getFuncNodeCode()));
			}				
		}
		return result;
	}

	

}
