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
 * ��ѯ��ť�б��������ϲ�ѯ�����Ի����ʼ����������
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "��ѯ��ť�б��������ϲ�ѯ�����Ի����ʼ����������", coder = "qiaoyanga", relatedIssueId = "195")
public class TestCase00195 extends AbstractXmlRuleDefinition {
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
            boolean hasAction = false;
            for (Element checkAction : listActions) {
                String classPath = checkAction.getAttribute("class");
                // ��ȡ��ť
                if (classPath != null
                        && iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), classPath, "nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction")) {
                    hasAction = true;
                    Element qryCondDLGInitializer =
                            xmlResource.getChildPropertyElement(checkAction, "qryCondDLGInitializer");
                    if (qryCondDLGInitializer == null) {
                        noteBuilder.append(xmlResource.getResourcePath() + "��ѯ��ť��û�������ϲ�ѯ�����Ի����ʼ���������� \n");
                        break;
                    }
                    String ref = qryCondDLGInitializer.getAttribute("ref");
                    if ("".equals(ref)) {
                        noteBuilder.append(xmlResource.getResourcePath() + "��ť" + checkAction.getAttribute("id") + "��qryCondDLGInitializer����û���ҵ�ref \n");
                        break;
                    }
                    Element qryCondDLGBean = xmlResource.getElementById(ref);
                    if (qryCondDLGBean == null) {
                        noteBuilder.append(xmlResource.getResourcePath() + "û���ҵ�" + ref + "��Bean \n" );
                        break;
                    }
                }
            }
            if (!hasAction) {
                noteBuilder.append(xmlResource.getResourcePath() + "û���ҵ���ȷ�Ĳ�ѯ��ť�� \n");
            }
            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
