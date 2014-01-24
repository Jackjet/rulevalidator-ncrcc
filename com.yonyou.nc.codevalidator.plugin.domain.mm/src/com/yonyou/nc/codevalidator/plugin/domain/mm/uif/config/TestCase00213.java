package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.config;


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

/**
 * @since 1.0
 * @version 1.0.0.0
 * @author wangfra
 *
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "��ͬ�ڵ�ı����ֶ�û����ȷΨһ�����������Բ���ȡ����ģ�����жϡ�����ȡ�õ�ǰ�ڵ��xml�ж����Ƿ������˽ڵ�򿪼�����֧�ֳ����ӡ�", coder = "wangfra", relatedIssueId = "213")
public class TestCase00213  extends AbstractXmlRuleDefinition{

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		return new XmlResourceQuery(functionNodes,ruleExecContext.getBusinessComponent());
	}

	@Override
	protected IRuleExecuteResult processScriptRules(
			IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		 ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for(XmlResource xmlResource : resources){
			List<Element> listenerList = xmlResource.getAllBeanClass("nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener");
			if (listenerList == null || listenerList.size() == 0) {
				 result.addResultElement(xmlResource.getResourcePath(),"�Ҳ����ڵ�򿪼���:DefaultFuncNodeInitDataListener \n");
			}
			if (listenerList.size() > 1) {
				 result.addResultElement(xmlResource.getResourcePath(),"���ڶ���ڵ�򿪼���DefaultFuncNodeInitDataListener��Ӧ��ֻ��һ�� \n");
			}
		}
		
		return result;
	}
}
