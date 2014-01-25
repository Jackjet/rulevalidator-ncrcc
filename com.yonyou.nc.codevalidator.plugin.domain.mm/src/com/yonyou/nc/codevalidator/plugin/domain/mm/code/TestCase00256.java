package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
 * 转单需要考虑主子表界面精度和主子拉平界面精度
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, description = "转单需要考虑主子表界面精度和主子拉平界面精度", relatedIssueId = "256", subCatalog = SubCatalogEnum.JC_CODECRITERION, coder = "gaojf")
public class TestCase00256 extends AbstractXmlRuleDefinition {

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
            String srcBillType = this.queryBillTypeId(xmlResource.getFuncNodeCode(), ruleExecContext);
            if (srcBillType == null) {
                result.addResultElement(xmlResource.getFuncNodeCode(), "未找到该节点对应的单据类型，请检查该节点是否为单据！\n");
                continue;
            }
            // 获得类型对应类
            List<String> listStyleClasses = this.getBillUI(srcBillType, ruleExecContext);
            Set<Class<?>> classLs = new HashSet<Class<?>>();
            List<String> xmlpaths = new ArrayList<String>();
            // 得到继承了SourceRefDlg的类
            for (String classname : listStyleClasses) {
                Class<?> clazz = null;
                try {
                    clazz =
                            ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                                    ruleExecContext.getBusinessComponent().getProjectName(), classname);
                    boolean isExtend =
                            ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                    ruleExecContext.getBusinessComponent().getProjectName(), classname,
                                    "nc.ui.pubapp.billref.src.view.SourceRefDlg");
                    if (!isExtend) {
                        continue;
                    }
                    classLs.add(clazz);
                    // 读源文件找到xml路径，只能处理方法返回一个字符串，不能是变量也不能是方法
                    JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
                    javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
                    javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
                    javaResourceQuery.setClassNameFilterList(Arrays.asList(classname));

                    List<JavaClassResource> javaResources = ResourceManagerFacade.getResource(javaResourceQuery);
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
                // 去掉"/","<"和">"
                xmlpath = xmlpath.startsWith("/") ? xmlpath.substring(1) : xmlpath;

                xmlpath = xmlpath.substring(1, xmlpath.length() - 1);

                SpringXmlDocument springDocument =
                        XmlUtils.parseSpringXml(ruleExecContext.getBusinessComponent().getProjectName(), xmlpath);
                if (springDocument == null) {
                    result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                            String.format("引用的资源：%s在当前工程的类路径下未加载到!", xmlpath));
                    continue;
                }

                List<Element> listElements = springDocument.getElementsByTagName("bean");

                List<String> lsClass = new ArrayList<String>();
                for (Element ele : listElements) {
                    lsClass.add(ele.getAttribute("class"));
                }
                List<String> refclasses = new ArrayList<String>();
                for (String classname : lsClass) {
                    boolean isExtends =
                            ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                    ruleExecContext.getBusinessComponent().getProjectName(), classname,
                                    "nc.ui.pubapp.billref.src.IRefPanelInit");
                    if (isExtends) {
                        refclasses.add(classname);
                    }
                }
                if (refclasses == null || refclasses.size() == 0) {
                    result.addResultElement(xmlResource.getFuncNodeCode(), "给节点未找到转单处理界面\n");
                    continue;
                }
                for (String classname : refclasses) {
                    String dirt = ruleExecContext.getBusinessComponent().getCodePath() + "/";
                    classname = classname.replace(".", "/") + ".java";
                    CompilationUnit cu = new CompilationUnit();
                    dirt = dirt.replace("//", "/");
                    if (new File(dirt + "client/" + classname).isFile()) {
                        try {
                            cu = JavaParser.parse(new File(dirt + "client/" + classname));
                        }
                        catch (ParseException e) {
                            Logger.error(e.getMessage(), e);
                        }
                        catch (IOException e) {
                            Logger.error(e.getMessage(), e);
                        }
                    }
                    else {
                        continue;
                    }
                    List<ImportDeclaration> impors = cu.getImports();
                    boolean hasScale = false;
                    for (ImportDeclaration im : impors) {
                        if (im.getName().toString().contains(".scale.")) {
                            hasScale = true;
                            break;
                        }
                    }
                    if (!hasScale) {
                        result.addResultElement(xmlResource.getFuncNodeCode(), "请检查转单精度处理类没有进行精度处理\n");
                    }
                }
            }
        }
        return result;
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
