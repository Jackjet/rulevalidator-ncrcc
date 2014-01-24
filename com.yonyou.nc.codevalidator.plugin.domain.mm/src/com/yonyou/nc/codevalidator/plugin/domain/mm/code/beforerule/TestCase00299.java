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

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMArrayUtil;
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
 * 审批时单据状态的检查规则
 * 1.通过关键字符*ApproveBP.java取bs包下的*ApproveBP.java文件;2.再通过关键字符nc.bs.pubapp
 * .pub.rule.ApproveStatusCheckRule或者注入的rule.判断BP是否引用了
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "审批时,前规则中是否进行有单据状态检查规则", relatedIssueId = "299", coder = "lijbe", specialParamDefine = "检查规则", solution = "通过关键字符ApproveBP取bs包下的*ApproveBP.java文件;再通过关键字符nc.bs.pubapp.pub.rule.ApproveStatusCheckRule【有审批流时】或者注入的rule【无审批流时】.判断BP是否引用了规则,参数规则为节点编码=规则名,比如离散生产订单dmo,"
        + "就是dmo@CheckDmoStatusRule,如果业务组件下有多个节点，那么使用#将其分隔开;注意添加前规则的方法名为addBeforeRule.【如果没有录入参数，默认检查是否存在规则nc.bs.pubapp.pub.rule.ApproveStatusCheckRule】")
public class TestCase00299 extends AbstractJavaQueryRuleDefinition {

    private static String PARAM_NAME = "检查规则";

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
        StringBuffer noteBuilder = new StringBuffer();
        // if (MMArrayUtil.isEmpty(ruleExecContext
        // .getParameterArray(TestCase00299.PARAM_NAME))) {
        // result.addResultElement(ruleExecContext.getBusinessComponent()
        // .getBusinessComp(), "请在参数中审批时单据状态检查规则. \n");
        // return result;
        // }
        String[] params = ruleExecContext.getParameterArray(TestCase00299.PARAM_NAME);
        Map<String, String> paramMap = this.arrayToMap(params, noteBuilder);
        if (noteBuilder.length() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getBusinessComp(), noteBuilder.toString());
            return result;
        }
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
                    "没有找到以ApproveBP结尾的java文件，请检查插入BP命名是否以ApproveBP结尾. \n");
            return result;
        }
        Set<String> keys = javaClazzResMapList.keySet();
        for (String key : keys) {

            List<JavaClassResource> javaClassResList = javaClazzResMapList.get(key);
            //
            String tmpNodeId = key.substring(0, key.indexOf("bp") - 1);
            String nodeId = tmpNodeId.substring(tmpNodeId.lastIndexOf(".") + 1);
            if (javaClassResList.size() == 1) {
                // 检查
                this.check(javaClassResList.get(0), ruleExecContext, result, paramMap.get(nodeId));
            }
            else {
                this.check(this.findOptimalClassResource(javaClassResList), ruleExecContext, result,
                        paramMap.get(nodeId));
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
            ResourceRuleExecuteResult result, final String ruleName) {

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
            }
            final StringBuilder tmpRuleName = new StringBuilder();
            if (MMValueCheck.isNotEmpty(methodNames)) {
                if (MMValueCheck.isEmpty(ruleName)) {
                    tmpRuleName.append("ApproveStatusCheckRule");
                }
                else {
                    tmpRuleName.append(ruleName);
                }
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
                            if (stmt.toString().contains(tmpRuleName)) {
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
                    noteBuilder.append(String.format("方法【%s】没有添加审批时单据状态检查规则【" + tmpRuleName.toString() + "】\n!",
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

    /**
     * 将参数转换成Map形式
     * 
     * @param params
     * @return key:节点标示，比如BOM维护为bom0202,value:物料是否分配到工厂检查规则
     */
    private Map<String, String> arrayToMap(String[] params, StringBuffer errors) {
        if (MMArrayUtil.isEmpty(params)) {
            return new HashMap<String, String>();
        }
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            if (MMValueCheck.isEmpty(param)) {
                continue;
            }
            String[] strs = param.split("@");
            if (strs.length != 2) {
                errors.append("检查规则参数：" + param + "书写有误\n");
                continue;
            }
            map.put(strs[0].trim(), strs[1].trim());
        }

        return map;
    }
}
