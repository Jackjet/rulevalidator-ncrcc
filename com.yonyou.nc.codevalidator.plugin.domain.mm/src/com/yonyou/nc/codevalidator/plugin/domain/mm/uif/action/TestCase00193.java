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
 * 复制按钮使用CopyActionProcessor处理需要清空的字段
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "复制按钮使用CopyActionProcessor处理需要清空的字段", coder = "qiaoyanga", relatedIssueId = "193")
public class TestCase00193 extends AbstractXmlRuleDefinition {
    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        return new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        // 返回结果
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuffer noteBuilder = new StringBuffer();
        IClassLoaderUtils iClassLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
        for (XmlResource xmlResource : resources) {
            List<Element> listActions = xmlResource.getListActions();
            if (listActions == null) {
                listActions = xmlResource.getCardActions();
            }
            boolean hasAction = false;
            for (Element checkAction : listActions) {
                String classPath = checkAction.getAttribute("class");
                // 获取按钮
                if (classPath != null
                        && iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), classPath, "nc.ui.pubapp.uif2app.actions.CopyAction")) {
                    hasAction = true;
                    Element copyActionProcessor =
                            xmlResource.getChildPropertyElement(checkAction, "copyActionProcessor");
                    if (copyActionProcessor == null) {
                        noteBuilder.append("复制按钮类没有使用CopyActionProcessor \n");
                        break;
                    }
                    List<Element> beanList = xmlResource.getChildElementsByTagName(copyActionProcessor, "bean");
                    if (beanList == null || beanList.size() == 0) {
                        noteBuilder.append("复制按钮类的CopyActionProcessor没有bean属性  \n");
                        break;
                    }
                    String className = beanList.get(0).getAttribute("class");
                    if (className == null || "".equals(className)) {
                        noteBuilder.append("复制按钮类的CopyActionProcessor的bean没有class属性 \n");
                        break;
                    }

                    if (!iClassLoaderUtils.isExtendsParentClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), className,
                            "nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor")) {
                        noteBuilder
                                .append("复制按钮类的CopyActionProcessor应该实现nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor \n");
                        break;
                    }
                }
            }
            if (!hasAction) {
                noteBuilder.append("没有找到正确的复制按钮类 \n");
            }
            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
