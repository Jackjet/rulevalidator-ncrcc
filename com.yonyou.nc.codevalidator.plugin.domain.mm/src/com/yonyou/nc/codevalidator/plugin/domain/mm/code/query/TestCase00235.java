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
 * 主组织只能参照出有权限的业务单元
 * 
 * @author qiaoyanga
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODEDESIGN, description = "主组织只能参照出有权限的业务单元 ", relatedIssueId = "235", coder = "qiaoyanga", solution = "配置节点号参数，将通过节点号找到对应的配置文件，通过配置文件找到查询初试化类，判断类中是否有'setPowerEnable'和'registerNeedPermissionOrgFieldCode'方法")
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
                result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
                continue;
            }
            String ref = qryCondDLGInitializer.getAttribute("ref");
            if (ref == null) {
                result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
                continue;
            }
            Element qryCondDLGInitializerBean = xmlResource.getElementById(ref);
            if (qryCondDLGInitializerBean == null) {
                result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
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
                result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
                continue;
            }
            JavaClassResource javaClassResource = javaResourceList.get(0);
            if (javaClassResource == null) {
                result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
                continue;
            }
            try {
                CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
                QryCondDLGInitializer visitor = new QryCondDLGInitializer();
                visitor.visit(compilationUnit, null);
                boolean isImport = visitor.isFlag();
                if (!isImport) {
                    result.addResultElement(javaClassResource.getJavaCodeClassName(), "查询初试化类中物料需设置组织权限 \n");
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
     * 扫描引用类
     * 
     * @author qiaoyanga
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class QryCondDLGInitializer extends VoidVisitorAdapter<Void> {
        boolean flag = false;

        @Override
        public void visit(MethodDeclaration n, Void v) {

            // 得到方法体，即{}中的内容
            BlockStmt body = n.getBody();
            if (body == null) {
                return;
            }
            // 得到方法体内的语句块
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
