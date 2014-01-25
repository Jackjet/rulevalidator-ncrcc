package com.yonyou.nc.codevalidator.plugin.domain.mm.code.beforerule;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
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

/**
 * 是否启用审批流的检查规则
 * 1.通过关键字符*CommitBP.java取bs包下的*CommitBP.java文件;2.再通过关键字符nc.bs.pubapp
 * .pub.rule.WorkflowCheckRule或者注入的rule.判断BP是否引用了
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "提交时,前规则中是否有启用审批流的检查规则", relatedIssueId = "297", coder = "lijbe", solution = "通过关键字符CommitBP取bs包下的*CommitBP.java文件;再通过关键字符nc.bs.pubapp.pub.rule.WorkflowCheckRule.判断BP是否引用了规则."
        + "注意添加前规则的方法名为addBeforeRule.", memo = "该条规则有审批流时检查")
public class TestCase00297 extends AbstractJavaQueryRuleDefinition {

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

            if (className.endsWith("ApproveBP")) {
                String key = className.substring(0, className.indexOf("bp") + 2);
                javaClazzResMapList.put(key, javaClassResource);
            }
        }
        if (MMMapUtil.isEmpty(javaClazzResMapList)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getBusinessComp(),
                    "没有找到以CommitBP结尾的java文件，请检查插入BP命名是否以ApproveBP结尾. \n");
            return result;
        }
        Set<String> keys = javaClazzResMapList.keySet();
        for (String key : keys) {

            List<JavaClassResource> javaClassResList = javaClazzResMapList.get(key);

            if (javaClassResList.size() == 1) {
                // 检查
                this.check(javaClassResList.get(0), ruleExecContext, result);
            }
            else {
                this.check(this.findOptimalClassResource(javaClassResList), ruleExecContext, result);
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

    private void check(final JavaClassResource javaClassResource, IRuleExecuteContext ruleExecContext,
            ResourceRuleExecuteResult result) {

        try {
            String className = javaClassResource.getJavaCodeClassName();

            Class<?> loadClass;

            loadClass =
                    ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), className);

            Method[] methods = loadClass.getDeclaredMethods();
            final List<String> methodNames = new ArrayList<String>();
            for (Method method : methods) {
                if (method.getName().contains("addBeforeRule")) {
                    methodNames.add(method.getName());
                }

            }
            if (MMValueCheck.isEmpty(methodNames)) {
                result.addResultElement(javaClassResource.getJavaCodeClassName(), "添加前规则方法名不为addBeforeRule,请修改！");
                return;
            }

            CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));

            final StringBuilder noteBuilder = new StringBuilder();
            VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodDeclaration n, Void v) {
                    if (methodNames.contains(n.getName())) {
                        boolean isBillCodeCheck = false;
                        // 得到方法体，即{}中的内容
                        BlockStmt body = n.getBody();
                        if (body == null) {
                            this.appendMsg(n.getName());
                            return;
                        }
                        // 得到方法体内的语句块
                        List<Statement> stmts = body.getStmts();
                        if (stmts == null) {
                            this.appendMsg(n.getName());
                            return;
                        }

                        for (Statement stmt : stmts) {
                            if (stmt.toString().contains("WorkflowCheckRule")) {
                                isBillCodeCheck = true;
                                break;
                            }
                        }
                        if (!isBillCodeCheck) {
                            this.appendMsg(n.getName());
                        }
                    }
                }

                private void appendMsg(String methodName) {
                    noteBuilder.append(String.format(
                            "方法【%s】没有添加是否启用审批流的检查规则【nc.bs.pubapp.pub.rule.WorkflowCheckRule】\n!",
                            javaClassResource.getJavaCodeClassName() + "." + methodName));
                }
            };

            visitor.visit(compilationUnit, null);

            if (noteBuilder.toString().length() > 0) {
                result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
            }
        }
        catch (RuleClassLoadException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (ParseException e) {
            Logger.error(e.getMessage(), e);
        }
        catch (IOException e) {
            Logger.error(e.getMessage(), e);
        }
    }

}
