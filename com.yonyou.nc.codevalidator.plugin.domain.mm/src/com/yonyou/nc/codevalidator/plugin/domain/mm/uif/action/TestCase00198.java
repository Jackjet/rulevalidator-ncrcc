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
 * �ύ���ջء������������û��ǰ̨���潻���İ�ť��Ҫ֧������������ѡ��
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "�ύ���ջء������������û��ǰ̨���潻���İ�ť��Ҫ֧������������ѡ��", coder = "qiaoyanga", relatedIssueId = "198")
public class TestCase00198 extends AbstractXmlRuleDefinition {
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
            // �ύ
            Element commitAction = xmlResource.getElementById("commitAction");
            if (commitAction == null) {
                noteBuilder.append("δ�ҵ���ȷ���ύ��ť�������ļ��а�ťidӦ��Ϊ��commitAction��;\n");
            }
            else {
                String commitActionClassPath = commitAction.getAttribute("class");
                if (commitActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), commitActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("�ύ��ťδ�̳�nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction��;\n");
                }
            }
            // �ջ�
            Element uncommitAction = xmlResource.getElementById("uncommitAction");
            if (uncommitAction == null) {
                noteBuilder.append("δ�ҵ���ȷ���ջذ�ť�������ļ��а�ťidӦ��Ϊ��uncommitAction��;\n");
            }
            else {
                String uncommitActionClassPath = uncommitAction.getAttribute("class");
                if (uncommitActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), uncommitActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("�ջذ�ťδ�̳�nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction��;\n");
                }
            }
            // ����
            Element approveAction = xmlResource.getElementById("approveAction");
            if (approveAction == null) {
                noteBuilder.append("δ�ҵ���ȷ��������ť�������ļ��а�ťidӦ��Ϊ��approveAction��;\n");
            }
            else {
                String approveActionClassPath = approveAction.getAttribute("class");
                if (approveActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), approveActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("������ťδ�̳С�nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction����;\n");
                }
            }
            // ����
            Element unapproveAction = xmlResource.getElementById("unapproveAction");
            if (unapproveAction == null) {
                noteBuilder.append("δ�ҵ���ȷ������ť�������ļ��а�ťidӦ��Ϊ��unapproveAction��;\n");
            }
            else {
                String unapproveActionClassPath = unapproveAction.getAttribute("class");
                if (unapproveActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), unapproveActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("����ťδ�̳�nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction��;\n");
                }
            }
            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
