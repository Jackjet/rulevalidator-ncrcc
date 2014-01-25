package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.config;

import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.ResourceResultElement;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ����ģ�������ļ���templateContainer�����û�ж������ģ�壬��Ҫ����nodeKeiesѡ�� �ο�
 * nc.test.mm.autotest.bill.testcase.xmlbased.client.templet.TestCase000094
 * 
 * @author luoweid
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "����ģ�������ļ���templateContainer�����û�ж������ģ�壬��Ҫ����nodeKeiesѡ��", solution = "", relatedIssueId = "168", coder = "qiaoyanga")
public class TestCase00168 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResourceQuery = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        final StringBuilder errorBuilder = new StringBuilder();
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        for (XmlResource xmlResource : resources) {
            Logger.debug("com.yonyou.nc.codevalidator.plugin.xml.rule.templete.XmlTaskDefinition_001��"
                    + "���������ļ���nc.ui.uif2.editor.TemplateContainer��");
            List<Element> elementList = xmlResource.getAllBeanClass("nc.ui.uif2.editor.TemplateContainer");
            Logger.debug("com.yonyou.nc.codevalidator.plugin.xml.rule.templete.XmlTaskDefinition_001��"
                    + "��ɼ��������ļ���nc.ui.uif2.editor.TemplateContainer��");
            if (null == elementList || elementList.size() == 0) {
                errorBuilder.append(xmlResource.getResourcePath() + ":û������nc.ui.uif2.editor.TemplateContainer��\n");
                continue;
            }
            Element propery = xmlResource.getChildPropertyElement(elementList.get(0), "nodeKeies");
            if (null != propery && this.getBillTemplateCount(xmlResource.getFuncNodeCode(), ruleExecContext) == 1) {
                errorBuilder.append(xmlResource.getResourcePath() + ":ֻ��һ��ģ��ʱ����Ҫ����nodeKeies��\n");
            }
            if (errorBuilder.toString().trim().length() > 0) {
                result.addResultElement(new ResourceResultElement(xmlResource.getResourcePath(), errorBuilder
                        .toString()));
            }
        }
        return result;
    }

    private int getBillTemplateCount(String funcode, IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        StringBuilder sb = new StringBuilder();
        sb.append("select pk_billtypecode from pub_billtemplet where nodecode='");
        sb.append(funcode);
        sb.append("' and isnull(dr,0)=0 and pk_corp='@@@@'");

        ScriptResourceQuery scriptResourceQuery = new ScriptResourceQuery(sb.toString());
        scriptResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
        List<ScriptResource> resultList = ResourceManagerFacade.getResource(scriptResourceQuery);

        if (null == resultList) {
            return 0;
        }
        return resultList.size();
    }

}
