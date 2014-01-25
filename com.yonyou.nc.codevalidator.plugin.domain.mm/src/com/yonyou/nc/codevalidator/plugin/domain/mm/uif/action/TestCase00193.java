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
 * ���ư�ťʹ��CopyActionProcessor������Ҫ��յ��ֶ�
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "���ư�ťʹ��CopyActionProcessor������Ҫ��յ��ֶ�", coder = "qiaoyanga", relatedIssueId = "193")
public class TestCase00193 extends AbstractXmlRuleDefinition {
    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        return new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        // ���ؽ��
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
                // ��ȡ��ť
                if (classPath != null
                        && iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), classPath, "nc.ui.pubapp.uif2app.actions.CopyAction")) {
                    hasAction = true;
                    Element copyActionProcessor =
                            xmlResource.getChildPropertyElement(checkAction, "copyActionProcessor");
                    if (copyActionProcessor == null) {
                        noteBuilder.append("���ư�ť��û��ʹ��CopyActionProcessor \n");
                        break;
                    }
                    List<Element> beanList = xmlResource.getChildElementsByTagName(copyActionProcessor, "bean");
                    if (beanList == null || beanList.size() == 0) {
                        noteBuilder.append("���ư�ť���CopyActionProcessorû��bean����  \n");
                        break;
                    }
                    String className = beanList.get(0).getAttribute("class");
                    if (className == null || "".equals(className)) {
                        noteBuilder.append("���ư�ť���CopyActionProcessor��beanû��class���� \n");
                        break;
                    }

                    if (!iClassLoaderUtils.isExtendsParentClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), className,
                            "nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor")) {
                        noteBuilder
                                .append("���ư�ť���CopyActionProcessorӦ��ʵ��nc.ui.pubapp.uif2app.actions.intf.ICopyActionProcessor \n");
                        break;
                    }
                }
            }
            if (!hasAction) {
                noteBuilder.append("û���ҵ���ȷ�ĸ��ư�ť�� \n");
            }
            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
