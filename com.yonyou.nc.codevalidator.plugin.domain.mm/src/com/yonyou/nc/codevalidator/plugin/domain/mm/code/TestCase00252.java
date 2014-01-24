package com.yonyou.nc.codevalidator.plugin.domain.mm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
 * ת�����Ӧ��ʹ�ñ�����ͨ�õĿ��
 * ת����������Ҫ�̳�DefaultBillReferQuery
 * ʵ�ֽӿ�IRefPanelInit��IRefQueryService
 * ��RefBillModelʵ�����еļ̳����е�û�̳У�û���жϱ�׼�����Է�����û�н����жϣ�
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, description = "ת�����Ӧ��ʹ�ñ�����ͨ�õĿ��", memo = "", solution = "ת�����Ӧ��ʹ�ñ�����ͨ�õĿ��ת���ģ�������Ҫ�̳�DefaultBillReferQuery��ʵ�ֽӿ�IRefPanelInit��IRefQueryService", subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "252", coder = "gaojf")
public class TestCase00252 extends AbstractXmlRuleDefinition {

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
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }

        for (XmlResource xmlResource : resources) {
            String srcBillType = this.queryBillTypeId(xmlResource.getFuncNodeCode(), ruleExecContext);
            // ������Ͷ�Ӧ��
            List<String[]> list = this.getBillUI(srcBillType, ruleExecContext);
            if (srcBillType == null) {
                result.addResultElement(xmlResource.getFuncNodeCode(), "δ�ҵ��ýڵ��Ӧ�ĵ������ͣ�����ýڵ��Ƿ�Ϊ���ݣ�\n");
                continue;
            }
            // �����ҵ������ļ���class
            Set<Class<?>> classLs = new HashSet<Class<?>>();
            // ת���Ĳ�ѯ��ʼ����
            Set<String> classQuery = new HashSet<String>();
            List<String> xmlpaths = new ArrayList<String>();
            for (String[] classname : list) {
                try {
                    // ת����ѯģ��
                    if (classname[1] != null) {
                        classname[1] = classname[1].substring(1, classname[1].length() - 1);
                        classQuery.add(classname[1]);
                    }
                    Class<?> clazz = null;
                    // ת��������
                    if (classname[0] != null) {
                        clazz =
                                ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                                        ruleExecContext.getBusinessComponent().getProjectName(), classname[0]);
                        boolean isExtend =
                                ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                        ruleExecContext.getBusinessComponent().getProjectName(), classname[0],
                                        "nc.ui.pubapp.billref.src.view.SourceRefDlg");
                        if (!isExtend) {
                            continue;
                        }
                        classLs.add(clazz);
                        // ��Դ�ļ��ҵ�xml·����ֻ�ܴ���������һ���ַ����������Ǳ���Ҳ�����Ƿ���
                        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
                        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
                        javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
                        javaResourceQuery.setClassNameFilterList(Arrays.asList(classname[0]));
                        List<JavaClassResource> javaResources = ResourceManagerFacade.getResource(javaResourceQuery);
                        if (javaResources == null || javaResources.isEmpty()) {
                            boolean isExit =
                                    classname[0].contains("."
                                            + ruleExecContext.getBusinessComponent().getBusinessComp() + ".");
                            if (!isExit) {
                                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(), String
                                        .format("�ڵ�%s���ڸ����%s�£���ѡ����ȷ��������¼��ýڵ�\n ", xmlResource.getFuncNodeCode(),
                                                ruleExecContext.getBusinessComponent().getBusinessComp()));
                                break;
                            }
                            else {
                                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(), String
                                        .format("û�в鵽%s�������Ƿ���ļ���%s�����!\n ", classname[0], ruleExecContext
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
                            // Logger.error("ֻ�ܴ���һ��ת����");
                            continue;
                        }
                    }

                }
                catch (RuleClassLoadException e1) {
                    Logger.error("�����ʧ��", e1);
                    continue;
                }
                catch (FileNotFoundException e) {
                    Logger.error("δ���ҵ��ļ�", e);
                    continue;
                }
            }
            if (classQuery.size() != 0) {
                for (String clazzname : classQuery) {
                    this.checkExtends(clazzname, result, ruleExecContext);
                }
            }
            if (classLs.isEmpty() || classLs.size() == 0) {
                result.addResultElement(xmlResource.getFuncNodeCode(), "û���ҵ�ת�����棡\n");
                continue;
            }
            for (String xmlpath : xmlpaths) {
                this.isMatchFrame(xmlpath, result, ruleExecContext);
            }

        }
        return result;
    }

    /**
     * ���ݽڵ���ҵ���Ӧ�ĵ�������id
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
     * ���ݵ��������ҵ���Ӧ����
     * 
     * @param srcBillType
     * @param ruleExecContext
     * @return
     */
    private List<String[]> getBillUI(String srcBillType, IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        StringBuilder sql = new StringBuilder();
        sql.append("select src_billui,src_qrytemplate from pub_vochange where src_billtype  ='");
        sql.append(srcBillType);
        sql.append("'");
        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
        if (MMValueCheck.isEmpty(dataSet) || dataSet.getRows().size() == 0) {
            return null;
        }
        List<String[]> result = new ArrayList<String[]>();
        for (DataRow dataRow : dataSet.getRows()) {
            String[] rs = new String[2];
            rs[0] = (String) dataRow.getValue("src_billui");
            rs[1] = (String) dataRow.getValue("src_qrytemplate");
            if (rs[0] != null || rs[1] != null) {
                result.add(rs);
            }
        }
        return result;
    }

    /**
     * �ж��Ƿ����ת�����
     * 
     * @return
     */
    private boolean isMatchFrame(String xmlpath, ResourceRuleExecuteResult result, IRuleExecuteContext ruleExecContext) {
        try {
            // ȥ��"/","<"��">"
            xmlpath = xmlpath.startsWith("/") ? xmlpath.substring(1) : xmlpath;
            xmlpath = xmlpath.substring(1, xmlpath.length() - 1);
            SpringXmlDocument springDocument =
                    XmlUtils.parseSpringXml(ruleExecContext.getBusinessComponent().getProjectName(), xmlpath);

            if (springDocument == null) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                        String.format("���õ���Դ��%s�ڵ�ǰ���̵���·����δ���ص�!", xmlpath));
                return false;
            }

            List<Element> listElements = springDocument.getElementsByTagName("bean");

            List<String> lsClass = new ArrayList<String>();
            for (Element ele : listElements) {
                lsClass.add(ele.getAttribute("class"));
            }
            Map<String, Boolean> matchMap = new HashMap<String, Boolean>();
            matchMap.put("IRefQueryService", false);
            matchMap.put("IRefPanelInit", false);
            for (String classname : lsClass) {

                this.checkImpl(matchMap, "nc.ui.pubapp.billref.src.IRefPanelInit", "IRefPanelInit", classname,
                        ruleExecContext);
                this.checkImpl(matchMap, "nc.ui.pubapp.uif2app.query2.model.IRefQueryService", "IRefQueryService",
                        classname, ruleExecContext);
            }

            if (!matchMap.get("IRefPanelInit")) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                        String.format("���õ���Դ��%s�����ļ���û��ʵ��IRefPanelInit!", xmlpath));
            }
            if (!matchMap.get("IRefQueryService")) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                        String.format("���õ���Դ��%s�����ļ���û��ʵ��IRefQueryService!", xmlpath));
            }

        }
        catch (RuleClassLoadException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (RuleBaseException e) {
            Logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * ����Ƿ�̳�����
     * 
     * @param matchMap
     * @param path
     * @param name
     * @param classname
     * @param ruleExecContext
     */
    private void checkExtends(String classname, ResourceRuleExecuteResult result, IRuleExecuteContext ruleExecContext) {
        boolean isExtends = false;
        try {
            isExtends =
                    ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), classname,
                            "nc.ui.pubapp.billref.src.DefaultBillReferQuery");
            if (!isExtends) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getProjectName(),
                        String.format("���õ���Դ��%sû�м̳�DefaultBillReferQuery!", classname));
            }

        }
        catch (RuleClassLoadException e) {
            Logger.error(e.getMessage(), e);
        }
    }

    /**
     * �ж��Ƿ�ʵ���˽ӿ�
     * 
     * @param matchMap
     * @param path
     * @param name
     * @param classname
     * @param ruleExecContext
     */
    private void checkImpl(Map<String, Boolean> matchMap, String path, String name, String classname,
            IRuleExecuteContext ruleExecContext) {
        boolean isExtends;
        try {
            isExtends =
                    ClassLoaderUtilsFactory.getClassLoaderUtils().isImplementedInterface(
                            ruleExecContext.getBusinessComponent().getProjectName(), classname, path);

            if (isExtends) {
                if (!matchMap.get(name)) {
                    matchMap.put(name, true);
                    return;
                }
            }
        }
        catch (RuleClassLoadException e) {
            Logger.error(e.getMessage(), e);
        }
    }
}
