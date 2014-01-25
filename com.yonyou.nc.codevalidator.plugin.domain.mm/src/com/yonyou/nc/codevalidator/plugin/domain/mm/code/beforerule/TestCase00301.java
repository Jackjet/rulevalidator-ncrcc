package com.yonyou.nc.codevalidator.plugin.domain.mm.code.beforerule;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
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
 * ȡ��������BP��Ҫ�жϵ���״̬�Ĺ���(Ĭ��ȡ������BP������ʽΪ����׺��UnApproveBP������BP����������������贫�������)
 * 
 * @since 6.0
 * @version 2013-10-11 ����8:03:49
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, specialParamDefine = {
    "������"
}, description = "ȡ��������BP��Ҫ�жϵ���״̬�ļ�����(��û�в���ʱ�����Ĭ�ϳ���������BP������ʽΪ��׺��UnApproveBP��������ΪUnapproveStatusCheckRule���в��������������ֻ��һ���ڵ㣬����ֱ��Ϊ�����磺DomUpdateStatusCheckRule��,�����ڶ���ڵ㣬���������dmo@CheckDmoStatusRule#BillUpdateStatusCheckRule�������ڵ����Ϊdmo�µ�BP����CheckDmoStatusRule��BillUpdateStatusCheckRule����ָ���ڵ���ֻ���ָ���Ĺ���������������������ڵ��BP��ִ��Ĭ�ϵĻ��߲����в����ڵ����������ļ�顣)  ", relatedIssueId = "301", coder = "zhongcha", solution = "��ǰ����������жϵ���״̬�Ĺ���", memo = "�����ɿա����ڶ���������ã����������������ֻ��һ���ڵ㣬��������Ϊ����Ĺ����磺DomUpdateStatusCheckRule��,�����ڶ���ڵ㣬���������dmo@CheckDmoStatusRule#BillUpdateStatusCheckRule�������ڵ����Ϊdmo�µ�BP����CheckDmoStatusRule��BillUpdateStatusCheckRule����ָ���ڵ���ֻ���ָ���Ĺ���������������������ڵ��BP��ִ��Ĭ�ϵĻ��߲����в����ڵ����������ļ�顣)")
public class TestCase00301 extends AbstractJavaQueryRuleDefinition {
    private final static String COM_PARAM = "commonParameter";

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
        String[] parameters = ruleExecContext.getParameterArray("������");
        // ������ת��Ϊ��ȡ�������� -��Ӧ- ���Ĺ���
        MapList<String, String> node2rules = this.getNode2Rules(parameters);
        // ��Ҫ����class�����Ӧ��Ҫ����rule
        MapList<String, String> clz2rules = new MapList<String, String>();
        // ��¼û��ִ�еĹ���
        MapList<String, String> errorClass2rules = new MapList<String, String>();
        // ��¼û��addBeforeRule()����
        List<String> notMethodClz = new ArrayList<String>();
        // ��¼�����нڵ������û�ҵ��Ϸ�BP�Ľڵ����
        Set<String> nodes = new HashSet<String>();
        nodes.addAll(node2rules.keySet());
        nodes.remove(TestCase00301.COM_PARAM);
        // ��Ҫ�����������package�������Ķ�Ӧ
        Map<String, JavaClassResource> package2BPClzs = new HashMap<String, JavaClassResource>();
        try {
            for (JavaClassResource javaClassResource : resources) {
                String fileName = javaClassResource.getJavaCodeClassName();
                String filePackage = fileName.substring(0, fileName.lastIndexOf("."));
                if (fileName.endsWith("UnApproveBP")) {
                    // ���ڼ�¼��fileName�ǲ������ڲ����е�ĳ���ڵ��£��������������ָ���Ĺ��򣬷���ִ��Ĭ�ϵĺͲ����в����ڵ����������
                    boolean isExistNode = false;
                    for (String node : node2rules.keySet()) {
                        if (fileName.contains("." + node + ".")) {
                            // Ĭ����Ϊͬһ�����´��ڶ����׺��ͬ���࣬��������̵�����Ҫ����
                            if (!package2BPClzs.keySet().contains(filePackage)
                                    || package2BPClzs.keySet().contains(filePackage)
                                    && package2BPClzs.get(filePackage).getJavaCodeClassName().length() > fileName
                                            .length()) {
                                package2BPClzs.put(filePackage, javaClassResource);
                                clz2rules.putAll(fileName, node2rules.get(node));
                                nodes.remove(node);
                            }
                            isExistNode = true;
                            break;
                        }
                    }
                    if (!isExistNode) {
                        // Ĭ����Ϊͬһ�����´��ڶ����׺��ͬ���࣬��������̵�����Ҫ����
                        if (!package2BPClzs.keySet().contains(filePackage)
                                || package2BPClzs.keySet().contains(filePackage)
                                && package2BPClzs.get(filePackage).getJavaCodeClassName().length() > fileName.length()) {
                            package2BPClzs.put(filePackage, javaClassResource);
                            clz2rules.putAll(fileName, node2rules.get(TestCase00301.COM_PARAM));
                        }
                    }
                }
            }
            // �����ڴ������ֱ࣬�ӱ���
            if (MMValueCheck.isEmpty(package2BPClzs)) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        "������в�����������׺ΪUnApproveBP��ȡ������BP��\n");
                return result;
            }
            for (JavaClassResource javaClassResource : package2BPClzs.values()) {
                String fileName = javaClassResource.getJavaCodeClassName();
                List<String> rulesList = new ArrayList<String>();
                for (String rule : clz2rules.get(fileName)) {
                    rulesList.add(rule);
                }
                ScanUnApproveBPRule scanRule = new ScanUnApproveBPRule(rulesList);
                // ɨ���java�ļ��ж��Ƿ�����жϵ���״̬�Ĺ���
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (!scanRule.isExistMethod()) {
                    notMethodClz.add(fileName);
                }
                if (scanRule.isExistMethod() && !scanRule.getRulesList().isEmpty()) {
                    for (String rule : scanRule.getRulesList()) {
                        errorClass2rules.put(fileName, rule);
                    }
                }
            }
            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(nodes)) {
                errorContxt.append("������в�������Ϊ" + nodes + "�Ľڵ㣨����·���в�����Щ�ڵ�������\n");
            }
            if (MMValueCheck.isNotEmpty(notMethodClz)) {
                errorContxt.append("���������Ϊ" + notMethodClz + "��BP��û��addBeforeRule�ķ�����\n");
            }
            if (MMValueCheck.isNotEmpty(errorClass2rules)) {
                for (String key : errorClass2rules.keySet()) {
                    errorContxt.append("����Ϊ[ " + key + " ]��BP�е�ǰ����û��ִ���жϵ���״̬�Ĺ���:" + errorClass2rules.get(key) + "��\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorContxt)) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        errorContxt.toString());
            }
        }
        catch (FileNotFoundException e) {
            Logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * ������ת��Ϊ�ڵ���� -��Ӧ- ���Ĺ����������ڽڵ���������
     * 
     * @param parameters
     * @return
     */
    public MapList<String, String> getNode2Rules(String[] parameters) {

        MapList<String, String> node2rules = new MapList<String, String>();
        if (MMValueCheck.isEmpty(parameters)) {
            node2rules.put(TestCase00301.COM_PARAM, "UnapproveStatusCheckRule");
            return node2rules;
        }
        for (String parameter : parameters) {
            if (parameter.indexOf("@") <= 0) {
                if (parameter.indexOf("@") == 0) {
                    parameter = parameter.substring(1);
                }
                String[] rules = parameter.split("#");
                for (String rule : rules) {
                    node2rules.put(TestCase00301.COM_PARAM, rule);
                }
            }
            else {
                String[] clsAndRuls = parameter.split("@");
                String[] classNames = clsAndRuls[0].split("#");
                String[] ruleNames = clsAndRuls[1].split("#");
                for (String className : classNames) {
                    for (String ruleName : ruleNames) {
                        node2rules.put(className.trim(), ruleName.trim());
                    }
                }
            }
        }
        if (MMValueCheck.isEmpty(node2rules.get(TestCase00301.COM_PARAM))) {
            node2rules.put(TestCase00301.COM_PARAM, "UnapproveStatusCheckRule");
        }
        return node2rules;
    }

    /**
     * ɨ��java�ļ��Ĺ��򣨼ٶ��û�������дjava.lang���е��ࣩ
     * 
     * @since 6.0
     * @version 2013-10-30 ����2:02:23
     * @author zhongcha
     */
    private static class ScanUnApproveBPRule extends AbstractJavaRule {
        public ScanUnApproveBPRule(List<String> rulesList) {
            this.rulesList = rulesList;
        }

        private List<String> rulesList = new ArrayList<String>();

        public List<String> getRulesList() {
            return this.rulesList;
        }

        private boolean isExistMethod = false;

        public boolean isExistMethod() {
            return this.isExistMethod;
        }

        /**
         * ����ǰɨ��ķ���ΪaddBeforeRuleʱ���ж��Ƿ�����жϵ���״̬�Ĺ���
         */
        @Override
        public Object visit(ASTMethodDeclarator node, Object data) {
            if (MMValueCheck.isNotEmpty(node.getImage()) && "addBeforeRule".equals(node.getImage().trim())) {
                if (!this.isExistMethod) {
                    this.isExistMethod = true;
                }
                if (MMValueCheck.isNotEmpty(node.getFirstParentOfType(ASTMethodDeclaration.class))
                        && node.getFirstParentOfType(ASTMethodDeclaration.class).hasDecendantOfAnyType(
                                ASTBlockStatement.class)) {
                    List<ASTBlockStatement> blockStatemtList =
                            node.getFirstParentOfType(ASTMethodDeclaration.class).findDescendantsOfType(
                                    ASTBlockStatement.class);
                    for (ASTBlockStatement blockStatemt : blockStatemtList) {
                        if (MMValueCheck
                                .isNotEmpty(blockStatemt.getFirstChildOfType(ASTLocalVariableDeclaration.class))
                                && blockStatemt.getFirstChildOfType(ASTLocalVariableDeclaration.class)
                                        .hasDecendantOfAnyType(ASTAllocationExpression.class)) {
                            ASTClassOrInterfaceType ruleType =
                                    blockStatemt.getFirstChildOfType(ASTLocalVariableDeclaration.class)
                                            .findDescendantsOfType(ASTAllocationExpression.class).get(0)
                                            .getFirstChildOfType(ASTClassOrInterfaceType.class);
                            if (MMValueCheck.isNotEmpty(ruleType) && MMValueCheck.isNotEmpty(ruleType.getImage())) {
                                String image = ruleType.getImage().trim();
                                if (image.contains(".")) {
                                    image = image.substring(image.lastIndexOf('.') + 1, image.length());
                                }
                                this.rulesList.remove(image);
                            }
                        }
                    }
                }
            }
            return super.visit(node, data);
        }
    }

}
