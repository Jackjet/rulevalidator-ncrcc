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
 * Ĭ�ϼ��������Ƿ����classΪ
 * nc.ui.uif2.editor.UserdefitemContainerListPreparator��bean
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "����б���ͼlistView�Ƿ�֧���Զ�����[��ѡ]", relatedIssueId = "854", coder = "lijbe", solution = "����б���ͼlistView�Ƿ�֧���Զ����������ʵ����Ϊ:nc.ui.uif2.editor.UserdefitemContainerListPreparator,����"
        + "Ӧ�ý���ע�뵽listView��")
public class TestCase00854 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources) {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // �������û��ʵ�����û��Զ�����Ľڵ����
        List<String> funNodes = new ArrayList<String>();
        // �������listViewû��ע�û��Զ�����Ľڵ�
        List<String> funNodesTwo = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }
        Set<String> refValues = new HashSet<String>();
        for (XmlResource xmlResource : resources) {

            List<Element> userDefCardElements =
                    xmlResource.getBeanElementByClass(MmUIFactoryConstants.LIST_USER_DEF_ITEM_PREPARATOR_CLASS);

            // �ж�xml�ļ����Ƿ��������2��bean
            if (MMValueCheck.isEmpty(userDefCardElements)) {
                funNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            // ȡ��classΪnc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare��bean
            List<Element> billDataPrepareElements =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare");
            if (MMValueCheck.isEmpty(billDataPrepareElements)) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());
                continue;
            }
            // �ж�bean���Ƿ���userdefitemPreparator
            for (Element element : billDataPrepareElements) {
                Element billDataPrepare = xmlResource.getChildPropertyElement(element, "billListDataPrepares");
                if (MMValueCheck.isEmpty(billDataPrepare)) {
                    funNodesTwo.add(xmlResource.getFuncNodeCode());
                    continue;
                }
                List<Element> billDataPrepares = xmlResource.getChildElementsByTagName(billDataPrepare, "list");
                for (Element billData : billDataPrepares) {
                    List<Element> beanList = xmlResource.getChildElementsByTagName(billData, "ref");
                    for (Element e : beanList) {
                        String refValue = e.getAttribute("bean");
                        if ("userdefitemlistPreparator".equals(refValue)) {
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
            if (refValues.isEmpty()) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());

            }
            refValues.clear();

        }

        if (funNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܱ���Ϊ ��%s �Ľڵ������ļ����б���ͼû�������Զ������ࣺ%s \n", funNodes,
                    MmUIFactoryConstants.LIST_USER_DEF_ITEM_PREPARATOR_CLASS));
        }
        if (funNodesTwo.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s �Ľڵ������ļ����б�ͼû�������Զ����%s  \n", funNodesTwo, "userdefitemlistPreparator"));
        }

        return result;
    }
}
