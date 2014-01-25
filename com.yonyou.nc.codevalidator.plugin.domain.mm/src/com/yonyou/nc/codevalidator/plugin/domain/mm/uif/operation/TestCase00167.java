package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.operation;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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

/**
 * ��ѯʹ�ö����Ĵ���queryProxy,���Ҽ̳���IQueryService
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "��ѯʹ�ö����Ĵ���queryProxy,���Ҽ̳���IQueryService", relatedIssueId = "167", coder = "lijbe", solution = "��ѯʹ�ö����Ĵ���queryProxy,����ʵ����nc.ui.pubapp.uif2app.query2.model.IQueryService")
public class TestCase00167 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }
        List<String> funCodes = new ArrayList<String>();
        // û��ʵ��nc.ui.pubapp.uif2app.query2.model.IQueryService�ӿڵĽڵ�
        List<String> funCodes2 = new ArrayList<String>();
        for (XmlResource xmlResource : resources) {
            Element element = xmlResource.getElementById("queryProxy");
            // û�в�ѯ����Bean��������������ΪqueryProxy
            if (null == element) {
                funCodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            String clazzName = element.getAttribute("class");
            boolean isImplement =
                    ClassLoaderUtilsFactory.getClassLoaderUtils().isImplementedInterface(
                            xmlResource.getBusinessComponent().getProjectName(), clazzName,
                            "nc.ui.pubapp.uif2app.query2.model.IQueryService");

            if (!isImplement) {
                funCodes2.add(xmlResource.getFuncNodeCode());
            }
        }

        if (!funCodes.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ��%s �Ľڵ������ļ���û�����ò�ѯ�������%s \n", funCodes, "queryProxy"));

        }
        if (!funCodes2.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܱ���Ϊ��%s �Ľڵ������ļ��в�ѯ�������û��ʵ�ֽӿڣ�%s \n", funCodes, "nc.ui.pubapp.uif2app.query2.model.IQueryService"));

        }
        return result;
    }

}
