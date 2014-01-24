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
 * ɾ������ʱ��������У���������ʹ��MMDownBillRewriter��� ��д��Դ���ݵĹ��򣬲�����д����Ҫʹ�ñ������ͨ�û�д���
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "ɾ��ʱ��������л�д��Դ���ݵĹ��򣬲�����д����Ҫʹ�ñ������ͨ�û�д��ܡ����ݿ�ѡ��", relatedIssueId = "291", coder = "lijbe", solution = "ͨ���ؼ��ַ�*DeleteBPȡbs���µ�*DeleteBP.java�ļ�;���ж�BP��Rule���Ƿ������˱�����Ļ�д��ܡ����������д���MMDownBillRewriter��,��д������Rewrite��ͷ������Rule��β;"
        + "ͨ������ע�뱾�����д���,�������Ϊ��,Ĭ�ϼ����������Ļ�д��ܡ�nc.vo.mmbd.pub.rewrite.MMDownBillRewriter��;ע����Ӻ����ķ�����ΪaddAfterRule.", specialParamDefine = "��д���")
public class TestCase00291 extends AbstractJavaQueryRuleDefinition {

    private static String PARAM_NAME = "��д���";

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
         * �ҳ�һ������������InsertBP��β��java�ļ���������java�ļ�����������̵�BP��ΪĿ���ļ���
         * ������ɢ������������DmoPushInsertBP��DmoTranstypeInsertBP���������Ŀ���ļ���DmoInsertBP
         * ����ͨ������������Ҳ����˵�
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
                    "û���ҵ���DeleteBP��β��java�ļ�������ɾ��BP�����Ƿ���DeleteBP��β. \n");
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
                // ���
                this.check(javaClassResList.get(0), paramValue, ruleExecContext, result);
            }
            else {
                this.check(this.findOptimalClassResource(javaClassResList), paramValue, ruleExecContext, result);
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

    private void check(final JavaClassResource javaClassResource, String framework,
            IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result) throws RuleBaseException {
        try {

            CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));

            BPVisitorAdapter bpvisitor = new BPVisitorAdapter(framework);
            bpvisitor.visit(compilationUnit, null);

            // BP�����˿�ܣ���ô�ͷ��سɹ����������������rule���Ƿ�������;���û�к�д����,��ôҲ���سɹ�
            if (bpvisitor.isFlag() || MMValueCheck.isEmpty(bpvisitor.getRewriteClassList())) {
                return;
            } // ɨ��Rewrite�����ļ�
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
        noteBuilder.append(String.format("��д����%s��û��ʹ�ñ������д��ܡ�" + framework + "����\n",
                javaClassResource.getJavaCodeClassName()));
    }

    /**
     * ɨmiao������ͷ���
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class BPVisitorAdapter extends VoidVisitorAdapter<Void> {
        boolean isImport = false;

        /**
         * �Ƿ���Ҫ������Rule�ļ���false������������
         */
        boolean flag = false;

        List<String> rewriteClassList = new ArrayList<String>();

        String framework = "nc.vo.mmbd.pub.rewrite.MMDownBillRewriter";

        public BPVisitorAdapter(String framework) {
            this.framework = framework;
        }

        @Override
        public void visit(ImportDeclaration n, Void arg) {

            // ���BP���Ѿ�ʹ���˻�д��ܣ���ô�Ͳ���Ҫ������Rule���Ƿ������˿��
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

            // û������,�򷵻�
            if (!this.isImport) {
                return;
            }
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
     * ɨmiao������ͷ���
     * 
     * @author lijbe
     * @since V1.0
     * @version 1.0.0.0
     */
    private static class RewriteRuleVisitorAdapter extends VoidVisitorAdapter<Void> {
        /**
         * �Ƿ���Ҫ������Rule�ļ���false������������
         */
        boolean flag = false;

        String framework = "nc.vo.mmbd.pub.rewrite.MMDownBillRewriter";

        public RewriteRuleVisitorAdapter(String framework) {
            this.framework = framework;
        }

        @Override
        public void visit(MethodDeclaration n, Void v) {

            // ����Ѿ������˿��,���ټ�����������
            if (this.isFlag()) {
                return;
            }
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
