package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.code.util.MmScaleHelper;
import com.yonyou.nc.codevalidator.plugin.domain.mm.code.util.MmScaleImportVisitor;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * 卡片界面、列表界面、打印精度使用一套精度处理类
 * 目前只针对主子表进行处理，孙表不进行处理
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, description = "卡片界面、列表界面、打印精度使用一套精度处理类", memo = "", solution = "卡片界面、列表界面、打印精度使用一套精度处理类", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "251", coder = "gaojf")
public class TestCase00251 extends AbstractXmlRuleDefinition {

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

        for (XmlResource xmlResource : resources) {

            List<Element> scalebeans = new ArrayList<Element>();

            List<Element> printbeans = this.getCardBillPrintList(scalebeans, xmlResource, ruleExecContext, result);
            if (printbeans != null) {
                String dirt = ruleExecContext.getBusinessComponent().getCodePath() + "/";
                dirt = dirt.replace("//", "/");
                for (Element p : printbeans) {
                    scalebeans.add(p);
                    this.check(scalebeans, ruleExecContext, xmlResource, result, dirt);
                    scalebeans.remove(p);
                }
            }

        }

        return result;
    }

    /**
     * 获得配置文件中的卡片，列表和打印精度类，返回需要处理的卡片和列表元素
     * 
     * @param cardbeans
     * @param billbeans
     * @param printbeans
     * @param xmlResource
     * @param ruleExecContext
     * @param result
     */
    private List<Element> getCardBillPrintList(List<Element> scalebeans, XmlResource xmlResource,
            IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result) {
        List<Element> eles = xmlResource.getElementsByTagName("bean");
        // 卡片精度
        List<Element> cardbeans = new ArrayList<Element>();
        // 列表精度
        List<Element> billbeans = new ArrayList<Element>();
        List<Element> printbeans = new ArrayList<Element>();
        // 处理时是否发生异常
        boolean flagException = false;
        for (Element e : eles) {
            if (e.getAttribute("class") == null || e.getAttribute("class").equals("")) {
                continue;
            }
            try {
                boolean isExtend =
                        ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                ruleExecContext.getBusinessComponent().getProjectName(),
                                e.getAttribute("class").trim(), "nc.ui.pubapp.uif2app.view.ShowUpableBillForm");
                if (isExtend) {
                    cardbeans.add(e);
                }

                boolean isExtendBill =
                        ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                ruleExecContext.getBusinessComponent().getProjectName(),
                                e.getAttribute("class").trim(), "nc.ui.pubapp.uif2app.view.ShowUpableBillListView");
                if (isExtendBill) {
                    billbeans.add(e);
                }
            }
            catch (RuleClassLoadException e2) {
                Logger.error(e2.getMessage(), e2);
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能编码为：%s 的配置文件中，加载%s或者加载nc.ui.pubapp.uif2app.view.ShowUpableBillForm或者"
                                + "加载nc.ui.pubapp.uif2app.view.ShowUpableBillListView失败. \n",
                                xmlResource.getFuncNodeCode(), e.getAttribute("class")));
                flagException = true;
                break;
            }

            if (e.getAttribute("class").indexOf(".print.") != -1) {
                printbeans.add(e);

            }
        }
        if (flagException) {
            return null;
        }
        if (null == cardbeans || cardbeans.size() == 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的配置文件中，卡片类不存在或者没有继承ShowUpableBillForm. \n", xmlResource.getFuncNodeCode()));
        }
        else {
            // Element ele = cardbeans.get(0);
            // if(cardbeans.size()>1){
            // for(Element e:cardbeans){
            // String s1 =
            // ele.getAttribute("class").substring(ele.getAttribute("class").lastIndexOf(".")+1,ele.getAttribute("class").length());
            // String s2 =
            // e.getAttribute("class").substring(e.getAttribute("class").lastIndexOf(".")+1,e.getAttribute("class").length());
            // //主子孙会判断出很多结果，分为主子表精度和孙表精度，为了找到主子表，取名字较短的做为主子表。
            // ele = s1.length()<=s2.length()?ele:e;
            // }
            // }
            scalebeans.addAll(cardbeans);
            // scalebeans.add(ele);
        }
        if (null == billbeans || billbeans.size() == 0) {

            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的配置文件中，列表类不存在或者没有继承ShowUpableBillListView. \n",
                            xmlResource.getFuncNodeCode()));
        }
        else {
            // Element ele = billbeans.get(0);
            // if(billbeans.size()>1){
            // for(Element e:billbeans){
            // String s1 =
            // ele.getAttribute("class").substring(ele.getAttribute("class").lastIndexOf(".")+1,ele.getAttribute("class").length());
            // String s2 =
            // e.getAttribute("class").substring(e.getAttribute("class").lastIndexOf(".")+1,e.getAttribute("class").length());
            // //主子孙会判断出很多结果，分为主子表精度和孙表精度，为了找到主子表，取名字较短的做为主子表。
            // ele = s1.length()<=s2.length()?ele:e;
            // }
            // }
            scalebeans.addAll(billbeans);
            // scalebeans.add(ele);

        }
        if (null == printbeans || printbeans.size() == 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的配置文件中，没有找到打印精度类. \n", xmlResource.getFuncNodeCode()));
        }
        return printbeans;
    }

    private void check(List<Element> scalebeans, IRuleExecuteContext ruleExecContext, XmlResource xmlResource,
            ResourceRuleExecuteResult result, String dirt) throws RuleBaseException {
        List<String> elementPaths = new ArrayList<String>();
        for (Element e : scalebeans) {
            elementPaths.add(e.getAttribute("class"));
        }
        String dirM = this.getMaxSameString(elementPaths);
        for (Element ele : scalebeans) {

            String dir = ele.getAttribute("class");
            dir = dir.replace(".", "/") + ".java";
            CompilationUnit cu;
            try {
                if (new File(dirt + "client/" + dir).isFile()) {
                    cu = JavaParser.parse(new File(dirt + "client/" + dir));
                }
                else {
                    continue;
                }

                String scaleFile = null;
                Set<String> scaleFiles = new HashSet<String>();

                List<JavaClassResource> printList = MmScaleHelper.getInstance().getPrintListFile(ruleExecContext, dirM);

                List<String> printResults = new ArrayList<String>();
                for (JavaClassResource javaClassResource : printList) {
                    printResults.add(javaClassResource.getResourcePath());
                }
                new MmScaleImportVisitor(printResults).visit(cu, scaleFiles);
                if (scaleFiles.size() == 0) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为：%s 的配置文件中，%s没有进行精度处理. \n", xmlResource.getFuncNodeCode(),
                                    ele.getAttribute("class")));
                    continue;
                }
                else if (scaleFiles.size() > 2) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为：%s 的配置文件中，%s中精度处理逻辑不是指向一个文件! \n", xmlResource.getFuncNodeCode(),
                                    ele.getAttribute("class")));
                    continue;
                }
                String name = scaleFiles.toArray(new String[0])[0];
                if (scaleFile == null) {
                    scaleFile = name;
                }
                if (!scaleFile.equals(name)) {
                    result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为：%s 的配置文件中，卡片界面、列表界面、打印精度使用一套精度处理类! \n", xmlResource.getFuncNodeCode()));
                }
            }
            catch (ParseException e) {
                Logger.error(e.getMessage(), e);
            }
            catch (IOException e) {
                Logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 获得最大相同字符串
     * 
     * @param params
     * @return
     */
    private String getMaxSameString(List<String> params) {

        String s = null;
        String[] initStrings = params.toArray(new String[params.size()]);
        if (initStrings.length == 0 || initStrings.length == 1) {
            return "";
        }
        s = initStrings[0];
        for (int i = 1; i < initStrings.length; i++) {
            char[] cString1 = s.toCharArray();
            char[] cString2 = initStrings[i].toCharArray();
            int k = 0;
            for (int j = 0; j < cString1.length && j < cString2.length; j++) {
                if (cString1[j] != cString2[j]) {
                    break;
                }
                else {
                    k = j;
                }
            }

            s = new String(cString1).substring(0, k);
        }
        return s;
    }
}
