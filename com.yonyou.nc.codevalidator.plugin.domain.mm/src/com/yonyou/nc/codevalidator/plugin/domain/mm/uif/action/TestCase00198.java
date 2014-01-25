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
 * 提交、收回、审批、弃审等没有前台界面交互的按钮需要支持批操作（可选）
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "提交、收回、审批、弃审等没有前台界面交互的按钮需要支持批操作（可选）", coder = "qiaoyanga", relatedIssueId = "198")
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
            // 提交
            Element commitAction = xmlResource.getElementById("commitAction");
            if (commitAction == null) {
                noteBuilder.append("未找到正确的提交按钮（配置文件中按钮id应该为：commitAction）;\n");
            }
            else {
                String commitActionClassPath = commitAction.getAttribute("class");
                if (commitActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), commitActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("提交按钮未继承nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction类;\n");
                }
            }
            // 收回
            Element uncommitAction = xmlResource.getElementById("uncommitAction");
            if (uncommitAction == null) {
                noteBuilder.append("未找到正确的收回按钮（配置文件中按钮id应该为：uncommitAction）;\n");
            }
            else {
                String uncommitActionClassPath = uncommitAction.getAttribute("class");
                if (uncommitActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), uncommitActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("收回按钮未继承nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction类;\n");
                }
            }
            // 审批
            Element approveAction = xmlResource.getElementById("approveAction");
            if (approveAction == null) {
                noteBuilder.append("未找到正确的审批按钮（配置文件中按钮id应该为：approveAction）;\n");
            }
            else {
                String approveActionClassPath = approveAction.getAttribute("class");
                if (approveActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), approveActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("审批按钮未继承‘nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction’类;\n");
                }
            }
            // 弃审
            Element unapproveAction = xmlResource.getElementById("unapproveAction");
            if (unapproveAction == null) {
                noteBuilder.append("未找到正确的弃审按钮（配置文件中按钮id应该为：unapproveAction）;\n");
            }
            else {
                String unapproveActionClassPath = unapproveAction.getAttribute("class");
                if (unapproveActionClassPath != null
                        && !iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), unapproveActionClassPath,
                                "nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction")) {
                    noteBuilder.append("弃审按钮未继承nc.ui.pubapp.uif2app.actions.pflow.ScriptPFlowAction类;\n");
                }
            }
            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
