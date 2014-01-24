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
 * 必须有打印精度的配置printProcessor，处理打印、预览、输出时的数据精度
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "必须有打印精度的配置printProcessor，处理打印、预览、输出时的数据精度", coder = "qiaoyanga", relatedIssueId = "200", solution = "配置功能节点号参数，根据配置的节点号找到对应的配置文件，再解析配置文件找到打印，预览，输出按钮的’beforePrintDataProcess‘属性，判断此属性是否对应类是否实现IBeforePrintDataProcess接口")
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
                // 获取按钮
                if (classPath != null
                        && (iClassLoaderUtils.isExtendsParentClass(ruleExecContext.getBusinessComponent()
                                .getProjectName(), classPath, "nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction") || iClassLoaderUtils
                                .isExtendsParentClass(ruleExecContext.getBusinessComponent().getProjectName(),
                                        classPath, "nc.ui.pubapp.uif2app.actions.OutputAction"))) {
                    Element beforePrintDataProcess =
                            xmlResource.getChildPropertyElement(checkAction, "beforePrintDataProcess");
                    if (beforePrintDataProcess == null) {
                        noteBuilder.append(xmlResource.getResourcePath() + ","+checkAction.getAttribute("id") + "按钮类没有使用beforePrintDataProcess\n");
                        continue;
                    }
                    String ref = beforePrintDataProcess.getAttribute("ref");
                    if ("".equals(ref)) {
                        noteBuilder
                                .append(xmlResource.getResourcePath() + "按钮" + checkAction.getAttribute("id") + "的beforePrintDataProcess属性没有找到ref\n");
                        continue;
                    }
                    Element printProcessor = xmlResource.getElementById(ref);
                    if (printProcessor == null) {
                        noteBuilder.append(xmlResource.getResourcePath() + "按钮" + checkAction.getAttribute("id") + "的beforePrintDataProcess属性没有值\n");
                        continue;
                    }
                    String className = printProcessor.getAttribute("class");
                    if (className == null || "".equals(className)) {
                        noteBuilder.append(xmlResource.getResourcePath() + ","+printProcessor.getAttribute("id") + "没有定义class \n");
                        continue;
                    }

                    if (!iClassLoaderUtils.isExtendsParentClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), className,
                            "nc.ui.pubapp.uif2app.actions.MetaDataBasedPrintAction$IBeforePrintDataProcess")) {
                        noteBuilder.append(xmlResource.getResourcePath() +"打印输出按钮类的beforePrintDataProcess应该实现IBeforePrintDataProcess接口\n");
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
