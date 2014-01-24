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
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;

/**
 * @since 1.0
 * @version 1.0.0.0
 * @author wangfra
 *
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY,catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "节点关闭时必须支持编辑态的提示功能ClosingListener【bean ID名为ClosingListener】。", coder = "wangfra", relatedIssueId = "207")
public class TestCase00207  extends AbstractXmlRuleDefinition{

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
            Element element = xmlResource.getElementById("ClosingListener");
            if (element == null) {
            	result.addResultElement(xmlResource.getResourcePath(),"xml未配置-节点关闭时必须支持编辑态的提示功能ClosingListener.\n");
            	return result;
            }
            String className = element.getAttribute("class");
            if (!"nc.ui.uif2.FunNodeClosingHandler".equals(className)) {
            	IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
            	String projectName = ruleExecContext.getBusinessComponent().getProjectName();
            	if(!classLoaderUtils.isParentClass(projectName, className, "nc.ui.uif2.FunNodeClosingHandler")||!classLoaderUtils.isImplementedInterface(projectName, className, "nc.ui.uif2.IFunNodeClosingListener")){
            		result.addResultElement(xmlResource.getResourcePath(),"xml文件未支持ClosingListener");
            	}
            }
		}
		return result;
	}
}
