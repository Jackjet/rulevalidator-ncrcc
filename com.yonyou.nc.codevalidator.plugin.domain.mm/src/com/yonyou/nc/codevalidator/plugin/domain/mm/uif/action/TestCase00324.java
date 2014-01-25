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
 * 修改和删除按钮都要支持数据权限
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "修改和删除按钮都要支持数据权限", coder = "qiaoyanga", relatedIssueId = "324", solution = "配置功能节点号参数，根据配置的节点号找到对应的配置文件，再解析配置文件找到修改、删除按钮，判断按钮是否有属性operateCode")
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
                // 获取按钮
                if (classPath != null
                        && (iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), classPath, "nc.ui.pubapp.uif2app.actions.DeleteAction") || iClassLoaderUtils
                                .isExtendsParentClass(ruleExecContext.getBusinessComponent().getProjectName(),
                                        classPath, "nc.ui.pubapp.uif2app.actions.EditAction"))) {
                    hasDeleteAction = true;
                    Element operateCodeProperty = xmlResource.getChildPropertyElement(checkAction, "operateCode");
                    if (operateCodeProperty == null) {
                        noteBuilder.append("按钮" + checkAction.getAttribute("id") + "没有operateCode属性,未支持数据权限\n");
                        break;
                    }
                    String operateCodevalue = operateCodeProperty.getAttribute("value");
                    if ("".equals(operateCodevalue)) {
                        noteBuilder.append("按钮" + checkAction.getAttribute("id") + "的operateCode属性没有找到value,未支持数据权限\n");
                        break;
                    }
                    Element resourceCodeProperty = xmlResource.getChildPropertyElement(checkAction, "resourceCode");
                    if (resourceCodeProperty == null) {
                        noteBuilder.append("按钮" + checkAction.getAttribute("id") + "没有resourceCode属性,未支持数据权限\n");
                        break;
                    }
                    String resourceCodevalue = resourceCodeProperty.getAttribute("value");
                    if ("".equals(resourceCodevalue)) {
                        noteBuilder
                                .append("按钮" + checkAction.getAttribute("id") + "的resourceCode属性没有找到value,未支持数据权限\n");
                        break;
                    }
                }
            }
            if (!hasDeleteAction) {
                noteBuilder.append("没有找到正确的删除或者修改按钮类\n");
            }

            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }

}
