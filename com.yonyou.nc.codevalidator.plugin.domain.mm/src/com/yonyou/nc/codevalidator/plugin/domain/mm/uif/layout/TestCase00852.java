package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.MmUIFactoryConstants;
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

/**
 * Ĭ�ϼ������������ɸ������ԣ��������Ҫ��鸨�����ԣ�����������룺checkMar=N���������и������Լ��.�Ƿ����classΪ
 * nc.ui.uif2.editor.UserdefitemContainerListPreparator
 * ��nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "��鿨Ƭ��ͼbillFormEditor�Ƿ�֧���Զ�����[��ѡ]", relatedIssueId = "852", coder = "lijbe", solution = "��鿨Ƭ��ͼbillFormEditorӦ��֧���Զ����������ʵ����Ϊnc.ui.uif2.editor.UserdefitemContainerListPreparator,����"
        + "Ӧ�ý���ע�뵽billFormEditor��")
public class TestCase00852 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources) {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // �������û��ʵ�����û��Զ�����������ϸ������ԵĽڵ����
        List<String> funNodes = new ArrayList<String>();
        // �������billFormEditorû��ע������2������
        List<String> funNodesTwo = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }
        Set<String> refValues = new HashSet<String>();
        for (XmlResource xmlResource : resources) {

            List<Element> userDefCardElements =
                    xmlResource.getBeanElementByClass(MmUIFactoryConstants.CARD_USER_DEF_ITEM_PREPARATOR_CLASS);

            // �ж�xml�ļ����Ƿ��������2��bean
            if (MMValueCheck.isEmpty(userDefCardElements)) {
                funNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            // ȡ��classΪnc.ui.pubapp.uif2app.view.CompositeBillDataPrepare��bean
            List<Element> billDataPrepareElements =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare");
            if (MMValueCheck.isEmpty(billDataPrepareElements)) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());
                continue;
            }
            // �ж�bean���Ƿ���userdefitemPreparator��marAsstPreparator
            for (Element element : billDataPrepareElements) {
                Element billDataPrepare = xmlResource.getChildPropertyElement(element, "billDataPrepares");
                if (MMValueCheck.isEmpty(billDataPrepare)) {
                    funNodesTwo.add(xmlResource.getFuncNodeCode());
                    continue;
                }
                List<Element> billDataPrepares = xmlResource.getChildElementsByTagName(billDataPrepare, "list");
                for (Element billData : billDataPrepares) {
                    List<Element> beanList = xmlResource.getChildElementsByTagName(billData, "ref");
                    for (Element e : beanList) {
                        String refValue = e.getAttribute("bean");
                        if ("userdefitemPreparator".equals(refValue)) {
                            refValues.add(refValue);
                        }
                        if (refValues.size() == 1) {
                            break;
                        }
                    }
                    if (refValues.size() == 1) {
                        break;
                    }
                }
            }
            if (refValues.size() < 1) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());

            }
            refValues.clear();

        }

        if (funNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܱ���Ϊ %s �Ľڵ������ļ��п�Ƭ��ͼû�����ö������ࣺ%s \n", funNodes,
                    MmUIFactoryConstants.CARD_USER_DEF_ITEM_PREPARATOR_CLASS));
        }
        if (funNodesTwo.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ %s �Ľڵ������ļ���Ƭ��ͼû�������Զ����%s  \n", funNodesTwo, "userdefitemPreparator"));
        }

        return result;
    }
}
