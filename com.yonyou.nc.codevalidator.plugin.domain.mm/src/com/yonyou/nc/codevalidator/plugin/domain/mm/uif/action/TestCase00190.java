package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

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

/**
 * 删除按钮支持批操作
 * 
 * @since 6.0
 * @version 2013-9-5 下午8:10:31
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "删除按钮支持批操作", solution = "检查删除按钮是否正确使用了指定的代理类，即按钮是否配置了singleBillService属性和multiBillService属性，二者具备其一即可。【删除按钮Bean的ID必须为:deleteAction】  ", coder = "zhongcha", relatedIssueId = "190")
public class TestCase00190 extends AbstractXmlRuleDefinition {

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
        for (XmlResource xmlResource : resources) {
            Element deleteAction = xmlResource.getElementById("deleteAction");
            if (deleteAction != null) {
                if (MMValueCheck.isEmpty(xmlResource.getChildPropertyElement(deleteAction, "singleBillService"))
                        && MMValueCheck.isEmpty(xmlResource.getChildPropertyElement(deleteAction, "multiBillService"))) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能节点编码:%s 的删除按钮未配置了singleBillService属性或multiBillService属性.\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
        }
        return result;
    }

}
