package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.layout;

import java.util.ArrayList;
import java.util.List;

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
 * 加载用户自定义项容器userdefitemContainer（可选）
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "配置文件中有加载用户自定义项容器userdefitemContainer【可选】", relatedIssueId = "170", coder = "lijbe", solution = "配置文件中有加载用户自定义项容器userdefitemContainer【可选】,即存在class为：nc.ui.uif2.userdefitem.UserDefItemContainer的Bean")
public class TestCase00170 extends AbstractXmlRuleDefinition {

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
        // 用来存放配置文件中卡片视图名字不叫billFormEditor的功能节点
        List<String> unlawfulNodes = new ArrayList<String>();

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }

        for (XmlResource xmlResource : resources) {
            List<Element> userDefContainerElement =
                    xmlResource.getBeanElementByClass(MmUIFactoryConstants.USER_DEF_CONTAINER);
            if (MMValueCheck.isEmpty(userDefContainerElement)) {
                unlawfulNodes.add(xmlResource.getFuncNodeCode());
            }
        }
        if (unlawfulNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的节点配置文件中没有配置用户自定义项容器：%s \n", unlawfulNodes, "userdefitemContainer"));
        }
        return result;
    }

}
