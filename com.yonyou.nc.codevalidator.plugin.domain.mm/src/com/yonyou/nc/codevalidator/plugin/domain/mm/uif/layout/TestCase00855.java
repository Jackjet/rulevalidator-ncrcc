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
 * 默认检查自由辅助属性，是否存在class为nc.ui.pubapp.uif2app.view.material.assistant.
 * MarAsstPreparator 的Bean
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "检查列表视图listView是否支物料自由辅助属性[可选]", relatedIssueId = "855", coder = "lijbe", solution = "检查列表视图listView是否支物料自由辅助属性，并且其实现类为:c.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator,而且"
        + "应该将其注入到listView中")
public class TestCase00855 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources) {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // 用来存放没有实现了物料辅助属性的节点编码
        List<String> funNodes = new ArrayList<String>();
        // 用来存放listView没有注物料辅助属性的节点
        List<String> funNodesTwo = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        Set<String> refValues = new HashSet<String>();
        for (XmlResource xmlResource : resources) {

            List<Element> marAsstElements =
                    xmlResource.getBeanElementByClass(MmUIFactoryConstants.MAR_ASST_PREPARATOR_CLASS);

            // 判断xml文件中是否包含以上2个bean
            if (MMValueCheck.isEmpty(marAsstElements)) {
                funNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            // 取得class为nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare的bean
            List<Element> billDataPrepareElements =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare");
            if (MMValueCheck.isEmpty(billDataPrepareElements)) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());
                continue;
            }
            // 判断bean里是否有marAsstPreparator
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
                        if ("marAsstPreparator".equals(refValue)) {
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
                    "功能编码为 ：%s 的节点配置文件中列表视图没有配置物料自由辅助属性类：%s \n", funNodes,
                    MmUIFactoryConstants.MAR_ASST_PREPARATOR_CLASS));
        }
        if (funNodesTwo.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s 的节点配置文件卡列表图没有引物料自由辅助属性：%s  \n", funNodesTwo, "marAsstPreparator"));
        }

        return result;
    }
}
