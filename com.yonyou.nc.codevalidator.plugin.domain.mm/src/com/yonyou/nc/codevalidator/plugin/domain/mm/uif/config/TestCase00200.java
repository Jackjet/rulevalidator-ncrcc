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
 * �����д�ӡ���ȵ�����printProcessor�������ӡ��Ԥ�������ʱ�����ݾ���
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "�����д�ӡ���ȵ�����printProcessor�������ӡ��Ԥ�������ʱ�����ݾ���", coder = "qiaoyanga", relatedIssueId = "200", solution = "���ù��ܽڵ�Ų������������õĽڵ���ҵ���Ӧ�������ļ����ٽ��������ļ��ҵ���ӡ��Ԥ���������ť�ġ�beforePrintDataProcess�����ԣ��жϴ������Ƿ��Ӧ���Ƿ�ʵ��IBeforePrintDataProcess�ӿ�")
public class TestCase00200 extends AbstractXmlRuleDefinition {
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
            if (listActions == null || listActions.size() == 0) {
                listActions = xmlResource.getCardActions();
            }
            for (Element checkAction : listActions) {
                String classPath = checkAction.getAttribute("class");
                // ��ȡ��ť
                if (classPath != null
                        && (iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), classPath, "nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction") || iClassLoaderUtils
                                .isExtendsParentClass(ruleExecContext.getBusinessComponent().getProjectName(),
                                        classPath, "nc.ui.pubapp.uif2app.actions.OutputAction"))) {
                    Element beforePrintDataProcess =
                            xmlResource.getChildPropertyElement(checkAction, "beforePrintDataProcess");
                    if (beforePrintDataProcess == null) {
                        noteBuilder.append(xmlResource.getResourcePath() + ","+checkAction.getAttribute("id") + "��ť��û��ʹ��beforePrintDataProcess\n");
                        continue;
                    }
                    String ref = beforePrintDataProcess.getAttribute("ref");
                    if ("".equals(ref)) {
                        noteBuilder
                                .append(xmlResource.getResourcePath() + "��ť" + checkAction.getAttribute("id") + "��beforePrintDataProcess����û���ҵ�ref\n");
                        continue;
                    }
                    Element printProcessor = xmlResource.getElementById(ref);
                    if (printProcessor == null) {
                        noteBuilder.append(xmlResource.getResourcePath() + "��ť" + checkAction.getAttribute("id") + "��beforePrintDataProcess����û��ֵ\n");
                        continue;
                    }
                    String className = printProcessor.getAttribute("class");
                    if (className == null || "".equals(className)) {
                        noteBuilder.append(xmlResource.getResourcePath() + ","+printProcessor.getAttribute("id") + "û�ж���class \n");
                        continue;
                    }

                    if (!iClassLoaderUtils.isExtendsParentClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), className,
                            "nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction$IBeforePrintDataProcess")) {
                        noteBuilder.append(xmlResource.getResourcePath() +"��ӡ�����ť���beforePrintDataProcessӦ��ʵ��IBeforePrintDataProcess�ӿ�\n");
                        continue;
                    }
                }
            }
            if (noteBuilder.length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
            }
        }
        return result;
    }
}
