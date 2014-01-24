package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.utils.SpringXmlDocument;
import com.yonyou.nc.codevalidator.sdk.utils.XmlUtils;

/**
 * 转单支持主子表和主子拉平两种方式，并且单据模板单独配置
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, description = "转单支持主子表和主子拉平两种方式，并且单据模板单独配置", memo = "", solution = "转单支持主子表和主子拉平两种方式，并且单据模板单独配置", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "253", coder = "gaojf")
public class TestCase00253 extends AbstractXmlRuleDefinition {

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
            // 找到节点单据类型
            String srcBillType = this.queryBillTypeId(xmlResource.getFuncNodeCode(), ruleExecContext);

            if (srcBillType == null) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                        String.format("功能节点%s中未找到该节点对应的单据类型，请检查该节点是否为单据!", xmlResource.getFuncNodeCode()));
                continue;
            }
            // 获得类型对应转单List
            List<String> exchangelist = this.getBillUI(srcBillType, ruleExecContext);

            Set<String> classLs = new HashSet<String>();
            List<String> xmlpaths = new ArrayList<String>();

            if (exchangelist.isEmpty() || exchangelist.size() == 0) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                        String.format("功能节点%s中未找到转单单据或该功能节点并没有对应相应转单单据!", xmlResource.getFuncNodeCode()));
                continue;
            }
            // 得到继承了SourceRefDlg的类
            for (String classname : exchangelist) {
                try {
                    boolean isExtend =
                            ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                    ruleExecContext.getBusinessComponent().getProjectName(), classname,
                                    "nc.ui.pubapp.billref.src.view.SourceRefDlg");
                    if (!isExtend) {
                        continue;
                    }
                    classLs.add(classname);
                    // 读源文件找到xml路径，只能处理方法返回一个字符串，不能是变量也不能是方法
                    JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
                    javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
                    javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
                    javaResourceQuery.setClassNameFilterList(Arrays.asList(classname));

                    List<JavaClassResource> javaResources = ResourceManagerFacade.getResource(javaResourceQuery);
                    if (javaResources == null || javaResources.isEmpty()) {
                        boolean isExit =
                                classname
                                        .contains("." + ruleExecContext.getBusinessComponent().getBusinessComp() + ".");
                        if (!isExit) {
                            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(), String
                                    .format("节点%s不在该组件%s下，请选择正确的组件重新检查该节点\n ", xmlResource.getFuncNodeCode(),
                                            ruleExecContext.getBusinessComponent().getBusinessComp()));
                            break;
                        }
                        else {
                            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(), String
                                    .format("没有查到%s，请检查是否该文件在%s组件下!\n ", classname, ruleExecContext
                                            .getBusinessComponent().getBusinessComp()));
                            break;
                        }
                    }
                    if (javaResources.size() == 1) {
                        JavaClassResource javaClassResource = javaResources.get(0);
                        RuleContext ruleContext =
                                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(),
                                        new AbstractJavaRule() {
                                            @Override
                                            public Object visit(ASTMethodDeclaration node, Object data) {
                                                List<ASTMethodDeclarator> methodDeclarators =
                                                        node.findChildrenOfType(ASTMethodDeclarator.class);
                                                if (methodDeclarators.size() == 1) {
                                                    if ("getRefBillInfoBeanPath".equals(methodDeclarators.get(0)
                                                            .getImage())) {
                                                        List<ASTReturnStatement> returnStmts =
                                                                node.findDescendantsOfType(ASTReturnStatement.class);
                                                        if (returnStmts.size() == 1) {
                                                            List<ASTLiteral> literals =
                                                                    returnStmts.get(0).findDescendantsOfType(
                                                                            ASTLiteral.class);
                                                            if (literals.size() == 1) {
                                                                String xmlPath = literals.get(0).getImage();
                                                                RuleContext ruleContext = (RuleContext) data;
                                                                ruleContext.setAttribute("xmlPath", xmlPath);
                                                            }
                                                        }
                                                    }
                                                }
                                                return super.visit(node, data);
                                            }
                                        });
                        Object attribute = ruleContext.getAttribute("xmlPath");
                        if (attribute instanceof String) {
                            String xmlPath = (String) attribute;
                            xmlpaths.add(xmlPath);
                        }
                    }
                    else {
                        Logger.error("只能处理一个");
                        continue;
                    }

                }
                catch (RuleClassLoadException e) {
                    Logger.error(e.getMessage(), e);
                }
                catch (FileNotFoundException e) {
                    Logger.error(e.getMessage(), e);
                }
            }

            if (classLs.isEmpty() || classLs.size() == 0) {
                result.addResultElement(xmlResource.getFuncNodeCode(), "没有找到转单界面！\n");
                continue;
            }

            for (String xmlpath : xmlpaths) {
                this.check(xmlpath, xmlResource.getFuncNodeCode(), result, ruleExecContext);
            }

        }
        return result;
    }

    /**
     * 检查功能节点中的转单配置
     * 
     * @param xmlpath
     * @param funnode
     * @param result
     * @param ruleExecContext
     */
    private void check(String xmlpath, String funnode, ResourceRuleExecuteResult result,
            IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        // 去掉"/","<"和">"
        xmlpath = xmlpath.startsWith("/") ? xmlpath.substring(1) : xmlpath;
        xmlpath = xmlpath.substring(1, xmlpath.length() - 1);
        SpringXmlDocument springDocument;

        springDocument = XmlUtils.parseSpringXml(ruleExecContext.getBusinessComponent().getProjectName(), xmlpath);

        if (springDocument == null) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                    String.format("引用的资源：%s在当前工程的类路径下未加载到!", xmlpath));
            return;
        }
        // 转单必须实现nc.ui.pubapp.billref.src.RefInfo
        List<Element> elements = springDocument.getBeanElementByClass("nc.ui.pubapp.billref.src.RefInfo");
        if (elements.size() == 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                    String.format("在%s中没有找到转单参照单据，转单的配置文件需要注入nc.ui.pubapp.billref.src.RefInfo!", xmlpath));
            return;
        }
        Element element = elements.get(0);
        // 主子表单据模板
        Element e = springDocument.getDirectChildPropertyElement(element, "billNodeKey");
        if (e == null) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                    String.format("在节点%s的%s配置文件中没有支持主子表!", funnode, xmlpath));
            return;
        }
        Set<String> listpram = new HashSet<String>();
        // 主子表nodekey
        String billvalue = e.getAttribute("value");
        listpram.add(billvalue);
        // 单表单据模板
        e = springDocument.getDirectChildPropertyElement(element, "billViewNodeKey");
        if (e == null) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                    String.format("在节点%s的%s配置文件中没有支持单表!", funnode, xmlpath));
            return;
        }
        // 单表nodekey
        String singlebillvalue = e.getAttribute("value");

        listpram.add(singlebillvalue);

        List<String> templateIds = this.getTemplateId(listpram, funnode, result, ruleExecContext);

        if (templateIds.size() == 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                    String.format("在节点%s的%s配置文件中没有单独配置单据模板!", funnode, xmlpath));
        }

    }

    /**
     * 获的该节点对应的转单单据模板ID
     * 
     * @param params
     * @param funnode
     * @param result
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private List<String> getTemplateId(Set<String> params, String funnode, ResourceRuleExecuteResult result,
            IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        StringBuffer sb = new StringBuffer();

        for (String param : params) {
            sb.append("'");
            sb.append(param);
            sb.append("',");
        }
        String s = sb.substring(0, sb.length() - 1);
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.templateid,t.tempstyle from pub_systemplate_base t where funnode='");
        sql.append(funnode);
        sql.append("'and nodekey in ( ");
        sql.append(s);
        sql.append(") and (dr = 0 or dr is null)");
        DataSet dataSet = null;

        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());

        List<String> res = new ArrayList<String>();

        for (DataRow dataRow : dataSet.getRows()) {
            String rs = (String) dataRow.getValue("templateid");
            if (rs != null) {
                res.add(rs);
            }
        }
        return res;
    }

    /**
     * 根据节点号找到对应的单据类型id
     * 
     * @param funcode
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private String queryBillTypeId(String funcode, IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        StringBuilder sql = new StringBuilder();
        sql.append(" select t.pk_billtypecode from bd_billtype t where  t.nodecode = '");
        sql.append(funcode);
        sql.append("' and t.pk_group = '~'");

        DataSet dataSet = null;

        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());

        if (MMValueCheck.isEmpty(dataSet) || dataSet.getRows().size() == 0) {
            return null;
        }
        return (String) dataSet.getRow(0).getValue("pk_billtypecode");
    }

    /**
     * 根据单据类型找到相应的类
     * 
     * @param srcBillType
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private List<String> getBillUI(String srcBillType, IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        StringBuilder sql = new StringBuilder();
        sql.append("select src_billui from pub_vochange where src_billtype  ='");
        sql.append(srcBillType);
        sql.append("'");
        DataSet dataSet = null;

        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());

        if (MMValueCheck.isEmpty(dataSet) || dataSet.getRows().size() == 0) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        for (DataRow dataRow : dataSet.getRows()) {
            String s = (String) dataRow.getValue("src_billui");
            if (s != null) {
                result.add(s);
            }
        }
        return result;
    }
}
