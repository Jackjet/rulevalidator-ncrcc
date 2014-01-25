package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

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
 * 打印按钮组应该至少包含打印、预览、输出三个按钮
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "打印按钮组应该至少包含打印、预览、输出三个按钮", coder = "qiaoyanga", relatedIssueId = "199")
public class TestCase00199 extends AbstractXmlRuleDefinition {
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
        IClassLoaderUtils iClassLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
        for (XmlResource xmlResource : resources) {
            Element printMenuAction = xmlResource.getElementById("printMenuAction");
            if (printMenuAction == null) {
                noteBuilder.append("没有找到printMenuAction \n");
            }
            boolean hasPrintAction = false;
            boolean hasPreviewAction = false;
            boolean hasOutputAction = false;
            Element groupActions = xmlResource.getChildPropertyElement(printMenuAction, "actions");
            List<Element> beanList = xmlResource.getChildElementsByTagName(groupActions, "list");
            if (beanList != null && beanList.size() == 1) {
                List<Element> groupRefList = xmlResource.getChildElementsByTagName(beanList.get(0), "ref");
                for (Element element : groupRefList) {
                    Element groupActionBean = xmlResource.getElementById(element.getAttribute("bean"));
                    String classPath = groupActionBean.getAttribute("class");
                    // 打印和预览按钮
                    if (classPath != null
                            && iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                    .getProjectName(), classPath,
                                    "nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction")) {
                        Element preview = xmlResource.getChildPropertyElement(groupActionBean, "preview");
                        if (preview != null) {
                            if ("false".equals(preview.getAttribute("value"))) {
                                hasPrintAction = true;
                            }
                            else if ("true".equals(preview.getAttribute("value"))) {
                                hasPreviewAction = true;
                            }
                        }
                    }
                    // 输出按钮
                    if (classPath != null
                            && iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                    .getProjectName(), classPath, "nc.ui.pubapp.uif2app.actions.OutputAction")) {
                        hasOutputAction = true;
                    }
                }
            }
            if (!hasPrintAction) {
                noteBuilder.append("打印按钮组里没有打印按钮\n");
            }
            if (!hasPreviewAction) {
                noteBuilder.append("打印按钮组里没有预览按钮\n");
            }
            if (!hasOutputAction) {
                noteBuilder.append("打印按钮组里没有输出按钮\n");
            }
            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
