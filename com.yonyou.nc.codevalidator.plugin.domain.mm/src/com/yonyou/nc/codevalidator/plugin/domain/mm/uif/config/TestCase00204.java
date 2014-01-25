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
 * ����������ʱmodule�е�������������ݵ�ͬ������billBodySortMediator
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "��������ʱmodule�е�������������ݵ�ͬ������billBodySortMediator", coder = "qiaoyanga", relatedIssueId = "204")
public class TestCase00204 extends AbstractXmlRuleDefinition {
    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        return new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuffer noteBuilder = new StringBuffer();
        for (XmlResource xmlResource : resources) {
            Element element = xmlResource.getElementById("billBodySortMediator");
            if (element == null) {
                noteBuilder.append("�ļ�δ֧��billBodySortMediator\n");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }
            String className = element.getAttribute("class");
            IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
            String projectName = ruleExecContext.getBusinessComponent().getProjectName();
            if (!classLoaderUtils.isParentClass(projectName, className,
                    "nc.ui.pubapp.uif2app.model.BillBodySortMediator")) {
                noteBuilder.append("�ļ�δ�̳�nc.ui.pubapp.uif2app.model.BillBodySortMediator��\n");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }
        }
        return result;
    }
}
