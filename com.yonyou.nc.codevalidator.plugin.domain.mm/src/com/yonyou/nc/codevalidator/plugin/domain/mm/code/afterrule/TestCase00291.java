package com.yonyou.nc.codevalidator.plugin.domain.mm.code.afterrule;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMMapUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
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
 * 删除单据时，后规则中，流程制造使用MMDownBillRewriter框架 回写来源单据的规则，差量回写，需要使用本领域的通用回写框架
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "删除时，后规则中回写来源单据的规则，差量回写，需要使用本领域的通用回写框架【单据可选】", relatedIssueId = "291", coder = "lijbe", solution = "通过关键字符*DeleteBP取bs包下的*DeleteBP.java文件;再判断BP和Rule中是否引用了本领域的回写框架【流程制造回写框架MMDownBillRewriter】,回写规则以Rewrite开头，并以Rule结尾;"
        + "通过参数注入本领域回写框架,果如参数为空,默认检查制造领域的回写框架【nc.vo.mmbd.pub.rewrite.MMDownBillRewriter】;注意添加后规则的方法名为addAfterRule.", specialParamDefine = "回写框架")
public class TestCase00291 extends AbstractJavaQueryRuleDefinition {

    private static String PARAM_NAME = "回写框架";

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);

        return javaResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        MapList<String, JavaClassResource> javaClazzResMapList = new MapList<String, JavaClassResource>();
        /*
         * 找出一个包下所有已InsertBP结尾的java文件，在这多个java文件中找类名最短的BP作为目标文件。
         * 比如离散生产订单就有DmoPushInsertBP和DmoTranstypeInsertBP，但是真的目标文件是DmoInsertBP
         * 而且通常的命名规则也是如此的
         */
        for (final JavaClassResource javaClassResource : resources) {

            String className = javaClassResource.getJavaCodeClassName();

            if (className.endsWith("DeleteBP")) {
                String key = className.substring(0, className.indexOf("bp") + 2);
                javaClazzResMapList.put(key, javaClassResource);
            }
        }
        if (MMMapUtil.isEmpty(javaClazzResMapList)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getBusinessComp(),
                    "没有找到以DeleteBP结尾的java文件，请检查删除BP命名是否以DeleteBP结尾. \n");
            return result;
        }
        String paramValue = ruleExecContext.getParameter(TestCase00291.PARAM_NAME);
        if (MMValueCheck.isEmpty(paramValue)) {
            paramValue = "nc.vo.mmbd.pub.rewrite.MMDownBillRewriter";
        }
        Set<String> keys = javaClazzResMapList.keySet();
        for (String key : keys) {

            List<JavaClassResource> javaClassResList = javaClazzResMapList.get(key);
            if (javaClassResList.size() == 1) {
                // 检查
                this.check(javaClassResList.get(0), paramValue, ruleExecContext, result);
            }
            else {
                this.check(this.findOptimalClassResource(javaClassResList), paramValue, ruleExecContext, result);
            }
        }

        return result;
    }

    /**
     * 找到最优的目标文件
     * 
     * @param javaClassResList
     * @return
     */
    private JavaClassResource findOptimalClassResource(List<JavaClassResource> javaClassResList) {
        List<String> clazzNameList = new ArrayList<String>();
        Map<String, JavaClassResource> resourceMap = new HashMap<String, JavaClassResource>();
        for (JavaClassResource javaClassResource : javaClassResList) {
            resourceMap.put(javaClassResource.getJavaCodeClassName(), javaClassResource);
            clazzNameList.add(javaClassResource.getJavaCodeClassName());
        }
        Collections.sort(clazzNameList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        });
        return resourceMap.get(clazzNameList.get(0));
    }

    private void check(final JavaClassResource javaClassResource, String framework,
            IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result) throws RuleBaseException {
        try {

            CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));

            BPVisitorAdapter bpvisitor = new BPVisitorAdapter(framework);
            bpvisitor.visit(compilationUnit, null);

            // BP引用了框架，那么就返回成功，否则继续往下找rule中是否引用了;如果没有胡写规则,那么也返回成功
            if (bpvisitor.isFlag() || MMValueCheck.isEmpty(bpvisitor.getRewriteClassList())) {
                return;
            } // 扫描Rewrite规则文件
            StringBuilder noteBuilder = new StringBuilder();
            List<JavaClassResource> rewriteJavaResList =
                    this.getJavaResources(ruleExecContext, bpvisitor.getRewriteClassList());
            RewriteRuleVisitorAdapter rewriteVisitor = new RewriteRuleVisitorAdapter(framework);
            for (JavaClassResource rewriteJavaResource : rewriteJavaResList) {
                CompilationUnit compilationUnit2 = JavaParser.parse(new File(rewriteJavaResource.getResourcePath()));
                rewriteVisitor.visit(compilationUnit2, null);
                if (!rewriteVisitor.isFlag()) {
                    this.appendMsg(noteBuilder, rewriteJavaResource, framework);
                }
            }
            if (noteBuilder.toString().length() > 0) {
                result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
            }

        }
        catch (ParseException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }
    }

    private List<JavaClassResource> getJavaResources(IRuleExecuteContext ruleExecContext, List<String> filterClazzs)
            throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);

        javaResourceQuery.setClassNameFilterList(filterClazzs);
        List<JavaClassResource> javaResourceList = new ArrayList<JavaClassResource>();
        javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);

        return javaResourceList;
    }

    private void appendMsg(StringBuilder noteBuilder, JavaClassResource javaClassResource, String framework) {
        noteBuilder.append(String.format("回写规则【%s】没有使用本领域回写框架【" + framework + "】！\n",
                javaClassResource.getJavaCodeClassName()));
    }

    /**
     * 扫miao引用类和方法
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class BPVisitorAdapter extends VoidVisitorAdapter<Void> {
        boolean isImport = false;

        /**
         * 是否还需要往下找Rule文件，false，继续往下找
         */
        boolean flag = false;

        List<String> rewriteClassList = new ArrayList<String>();

        String framework = "nc.vo.mmbd.pub.rewrite.MMDownBillRewriter";

        public BPVisitorAdapter(String framework) {
            this.framework = framework;
        }

        @Override
        public void visit(ImportDeclaration n, Void arg) {

            // 如果BP中已经使用了回写框架，那么就不需要往下找Rule中是否引用了框架
            if (this.isImport) {
                return;
            }
            String importClazz = n.getName().toString();
            String className = n.getName().getName();
            if (MMValueCheck.isEmpty(importClazz)) {
                return;
            }
            if (this.framework.equals(importClazz)) {
                this.isImport = true;
            }

            if (className.startsWith("Rewrite") && className.endsWith("Rule")) {
                this.rewriteClassList.add(importClazz);
            }
        }

        @Override
        public void visit(MethodDeclaration n, Void v) {

            // 没有引用,则返回
            if (!this.isImport) {
                return;
            }
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

            for (Statement stmt : stmts) {
                if (stmt.toString().contains(this.framework.substring(this.framework.lastIndexOf(".") + 1))) {
                    this.flag = true;
                    return;
                }
            }

        }

        public boolean isFlag() {
            return this.flag;
        }

        public List<String> getRewriteClassList() {
            return this.rewriteClassList;
        }

    }

    /**
     * 扫miao引用类和方法
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class RewriteRuleVisitorAdapter extends VoidVisitorAdapter<Void> {
        /**
         * 是否还需要往下找Rule文件，false，继续往下找
         */
        boolean flag = false;

        String framework = "nc.vo.mmbd.pub.rewrite.MMDownBillRewriter";

        public RewriteRuleVisitorAdapter(String framework) {
            this.framework = framework;
        }

        @Override
        public void visit(MethodDeclaration n, Void v) {

            // 如果已经引用了框架,则不再继续往下找了
            if (this.isFlag()) {
                return;
            }
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

            for (Statement stmt : stmts) {
                if (stmt.toString().contains(this.framework.substring(this.framework.lastIndexOf(".") + 1))) {
                    this.flag = true;
                    return;
                }
            }

        }

        public boolean isFlag() {
            return this.flag;
        }

    }
}
