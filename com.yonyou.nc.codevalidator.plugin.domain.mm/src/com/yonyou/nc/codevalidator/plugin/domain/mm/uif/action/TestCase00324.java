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
 * �޸ĺ�ɾ����ť��Ҫ֧������Ȩ��
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "�޸ĺ�ɾ����ť��Ҫ֧������Ȩ��", coder = "qiaoyanga", relatedIssueId = "324", solution = "���ù��ܽڵ�Ų������������õĽڵ���ҵ���Ӧ�������ļ����ٽ��������ļ��ҵ��޸ġ�ɾ����ť���жϰ�ť�Ƿ�������operateCode")
public class TestCase00324 extends AbstractXmlRuleDefinition {

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
            List<Element> listActions = xmlResource.getListActions();
            boolean hasDeleteAction = false;
            for (Element checkAction : listActions) {
                String classPath = checkAction.getAttribute("class");
                // ��ȡ��ť
                if (classPath != null
                        && (iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), classPath, "nc.ui.pubapp.uif2app.actions.DeleteAction") || iClassLoaderUtils
                                .isExtendsParentClass(ruleExecContext.getBusinessComponent().getProjectName(),
                                        classPath, "nc.ui.pubapp.uif2app.actions.EditAction"))) {
                    hasDeleteAction = true;
                    Element operateCodeProperty = xmlResource.getChildPropertyElement(checkAction, "operateCode");
                    if (operateCodeProperty == null) {
                        noteBuilder.append("��ť" + checkAction.getAttribute("id") + "û��operateCode����,δ֧������Ȩ��\n");
                        break;
                    }
                    String operateCodevalue = operateCodeProperty.getAttribute("value");
                    if ("".equals(operateCodevalue)) {
                        noteBuilder.append("��ť" + checkAction.getAttribute("id") + "��operateCode����û���ҵ�value,δ֧������Ȩ��\n");
                        break;
                    }
                    Element resourceCodeProperty = xmlResource.getChildPropertyElement(checkAction, "resourceCode");
                    if (resourceCodeProperty == null) {
                        noteBuilder.append("��ť" + checkAction.getAttribute("id") + "û��resourceCode����,δ֧������Ȩ��\n");
                        break;
                    }
                    String resourceCodevalue = resourceCodeProperty.getAttribute("value");
                    if ("".equals(resourceCodevalue)) {
                        noteBuilder
                                .append("��ť" + checkAction.getAttribute("id") + "��resourceCode����û���ҵ�value,δ֧������Ȩ��\n");
                        break;
                    }
                }
            }
            if (!hasDeleteAction) {
                noteBuilder.append("û���ҵ���ȷ��ɾ�������޸İ�ť��\n");
            }

            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }

}
