package com.yonyou.nc.codevalidator.plugin.domain.mm.code.query;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ����ֻ֯�ܲ��ճ���Ȩ�޵�ҵ��Ԫ
 * 
 * @author qiaoyanga
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODEDESIGN, description = "����ֻ֯�ܲ��ճ���Ȩ�޵�ҵ��Ԫ ", relatedIssueId = "235", coder = "qiaoyanga", solution = "���ýڵ�Ų�������ͨ���ڵ���ҵ���Ӧ�������ļ���ͨ�������ļ��ҵ���ѯ���Ի��࣬�ж������Ƿ���'setPowerEnable'��'registerNeedPermissionOrgFieldCode'����")
public class TestCase00235 extends AbstractXmlRuleDefinition {
    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        return new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        for (XmlResource xmlResource : resources) {
            Element queryAction = xmlResource.getElementById("queryAction");
            Element qryCondDLGInitializer = xmlResource.getChildPropertyElement(queryAction, "qryCondDLGInitializer");
            if (qryCondDLGInitializer == null) {
                result.addResultElement(xmlResource.getResourcePath(), "δ�ҵ���ȷ�Ĳ�ѯ��ʼ����\n");
                continue;
            }
            String ref = qryCondDLGInitializer.getAttribute("ref");
            if (ref == null) {
                result.addResultElement(xmlResource.getResourcePath(), "δ�ҵ���ȷ�Ĳ�ѯ��ʼ����\n");
                continue;
            }
            Element qryCondDLGInitializerBean = xmlResource.getElementById(ref);
            if (qryCondDLGInitializerBean == null) {
                result.addResultElement(xmlResource.getResourcePath(), "δ�ҵ���ȷ�Ĳ�ѯ��ʼ����\n");
                continue;
            }
            String qryCondDLGInitializerBeanName = qryCondDLGInitializerBean.getAttribute("class");
            JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
            javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
            javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
            List<String> className = new ArrayList<String>();
            className.add(qryCondDLGInitializerBeanName);
            javaResourceQuery.setClassNameFilterList(className);
            List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);
            if (javaResourceList == null || javaResourceList.size() <= 0) {
                result.addResultElement(xmlResource.getResourcePath(), "δ�ҵ���ȷ�Ĳ�ѯ��ʼ����\n");
                continue;
            }
            JavaClassResource javaClassResource = javaResourceList.get(0);
            if (javaClassResource == null) {
                result.addResultElement(xmlResource.getResourcePath(), "δ�ҵ���ȷ�Ĳ�ѯ��ʼ����\n");
                continue;
            }
            try {
                CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                QryCondDLGInitializer visitor = new QryCondDLGInitializer();
                visitor.visit(compilationUnit, null);
                boolean isImport = visitor.isFlag();
                if (!isImport) {
                    result.addResultElement(javaClassResource.getJavaCodeClassName(), "��ѯ���Ի�����������������֯Ȩ�� \n");
                    continue;
                }
            }
            catch (ParseException e) {
                Logger.error(e.getMessage(), e);
            }
            catch (IOException e) {
                Logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * ɨ��������
     * 
     * @author qiaoyanga
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class QryCondDLGInitializer extends VoidVisitorAdapter<Void> {
        boolean flag = false;

        @Override
        public void visit(MethodDeclaration n, Void v) {

            // �õ������壬��{}�е�����
            BlockStmt body = n.getBody();
            if (body == null) {
                return;
            }
            // �õ��������ڵ�����
            List<Statement> stmts = body.getStmts();
            if (stmts == null) {
                return;
            }
            boolean setPowerEnable = false;
            boolean registerNeedPermissionOrgFieldCode = false;
            for (Statement stmt : stmts) {
                if (stmt.toString().contains("setPowerEnable")) {
                    setPowerEnable = true;
                }
                if (stmt.toString().contains("registerNeedPermissionOrgFieldCode")) {
                    registerNeedPermissionOrgFieldCode = true;
                }
            }
            if (setPowerEnable && registerNeedPermissionOrgFieldCode) {
                this.flag = true;
            }

        }

        public boolean isFlag() {
            return this.flag;
        }
    }

}
