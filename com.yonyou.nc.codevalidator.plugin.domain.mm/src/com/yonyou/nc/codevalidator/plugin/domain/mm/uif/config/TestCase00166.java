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
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * ɾ��ʹ�ö����Ĵ���deleteProxy���̳���ISingleBillService
 * 
 * @author qiaoyang
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "ɾ��ʹ�ö����Ĵ���deleteProxy���̳���ISingleBillService", solution = "", coder = "qiaoyang", relatedIssueId = "166")
public class TestCase00166 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResourceQuery = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources) {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();

        for (XmlResource xmlResource : resources) {
            final StringBuilder errorBuilder = new StringBuilder();
            try {
                Element element = xmlResource.getElementById("deleteProxy");
                if (null == element) {
                    errorBuilder.append("û������ɾ��ʹ�ö����Ĵ���deleteProxy��");
                    Logger.debug("û������ɾ��ʹ�ö����Ĵ���deleteProxy��");
                    continue;
                }
                String className = element.getAttribute("class");
                boolean isImplementedInterface =
                        classLoaderUtils.isImplementedInterface(
                                ruleExecContext.getBusinessComponent().getProjectName(), className,
                                "nc.ui.pubapp.pub.task.ISingleBillService");
                if (!isImplementedInterface) {
                    errorBuilder.append("����deleteProxy:" + className + "û�м̳�nc.ui.pubapp.pub.task.ISingleBillService");
                }
            }
            catch (RuleClassLoadException e) {
                errorBuilder.append("��δ���ص�: " + e.getMessage() + "\n");
            }
            finally {
                if (errorBuilder.toString().trim().length() > 0) {
                    result.addResultElement(xmlResource.getResourcePath(), errorBuilder.toString());
                }
            }
        }

        return result;
    }

}
