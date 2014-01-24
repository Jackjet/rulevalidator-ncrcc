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
 * ����ʱ����״̬�ļ�����
 * 1.ͨ���ؼ��ַ�*ApproveBP.javaȡbs���µ�*ApproveBP.java�ļ�;2.��ͨ���ؼ��ַ�nc.bs.pubapp
 * .pub.rule.ApproveStatusCheckRule����ע���rule.�ж�BP�Ƿ�������
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.COMPILE, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "����ʱ,ǰ�������Ƿ�����е���״̬������", relatedIssueId = "299", coder = "lijbe", specialParamDefine = "������", solution = "ͨ���ؼ��ַ�ApproveBPȡbs���µ�*ApproveBP.java�ļ�;��ͨ���ؼ��ַ�nc.bs.pubapp.pub.rule.ApproveStatusCheckRule����������ʱ������ע���rule����������ʱ��.�ж�BP�Ƿ������˹���,��������Ϊ�ڵ����=������,������ɢ��������dmo,"
        + "����dmo@CheckDmoStatusRule,���ҵ��������ж���ڵ㣬��ôʹ��#����ָ���;ע�����ǰ����ķ�����ΪaddBeforeRule.�����û��¼�������Ĭ�ϼ���Ƿ���ڹ���nc.bs.pubapp.pub.rule.ApproveStatusCheckRule��")
public class TestCase00299 extends AbstractJavaQueryRuleDefinition {

    private static String PARAM_NAME = "������";

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
        // .getBusinessComp(), "���ڲ���������ʱ����״̬������. \n");
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
         * �ҳ�һ������������InsertBP��β��java�ļ���������java�ļ�����������̵�BP��ΪĿ���ļ���
         * ������ɢ������������DmoPushInsertBP��DmoTranstypeInsertBP���������Ŀ���ļ���DmoInsertBP
         * ����ͨ������������Ҳ����˵�
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
                    "û���ҵ���ApproveBP��β��java�ļ����������BP�����Ƿ���ApproveBP��β. \n");
            return result;
        }
        Set<String> keys = javaClazzResMapList.keySet();
        for (String key : keys) {

            List<JavaClassResource> javaClassResList = javaClazzResMapList.get(key);
            //
            String tmpNodeId = key.substring(0, key.indexOf("bp") - 1);
            String nodeId = tmpNodeId.substring(tmpNodeId.lastIndexOf(".") + 1);
            if (javaClassResList.size() == 1) {
                // ���
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
     * �ҵ����ŵ�Ŀ���ļ�
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
                result.addResultElement(javaClassResource.getJavaCodeClassName(), "���ǰ���򷽷�����ΪaddBeforeRule,���޸ģ�");
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
                        // �õ������壬��{}�е�����
                        BlockStmt body = n.getBody();
                        if (body == null) {
                            this.appendMsg(n.getName());
                            return;
                        }
                        // �õ��������ڵ�����
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
                    noteBuilder.append(String.format("������%s��û���������ʱ����״̬������" + tmpRuleName.toString() + "��\n!",
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
     * ������ת����Map��ʽ
     * 
     * @param params
     * @return key:�ڵ��ʾ������BOMά��Ϊbom0202,value:�����Ƿ���䵽����������
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
                errors.append("�����������" + param + "��д����\n");
                continue;
            }
            map.put(strs[0].trim(), strs[1].trim());
        }

        return map;
    }
}
