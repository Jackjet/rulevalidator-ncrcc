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
 * 查询使用独立的代理queryProxy,并且继承自IQueryService
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "查询使用独立的代理queryProxy,并且继承自IQueryService", relatedIssueId = "167", coder = "lijbe", solution = "查询使用独立的代理queryProxy,并且实现自nc.ui.pubapp.uif2app.query2.model.IQueryService")
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
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        List<String> funCodes = new ArrayList<String>();
        // 没有实现nc.ui.pubapp.uif2app.query2.model.IQueryService接口的节点
        List<String> funCodes2 = new ArrayList<String>();
        for (XmlResource xmlResource : resources) {
            Element element = xmlResource.getElementById("queryProxy");
            // 没有查询代理Bean，或者是命名不为queryProxy
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
                    String.format("功能编码为：%s 的节点配置文件中没有配置查询代理服务：%s \n", funCodes, "queryProxy"));

        }
        if (!funCodes2.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "功能编码为：%s 的节点配置文件中查询代理服务没有实现接口：%s \n", funCodes, "nc.ui.pubapp.uif2app.query2.model.IQueryService"));

        }
        return result;
    }

}
