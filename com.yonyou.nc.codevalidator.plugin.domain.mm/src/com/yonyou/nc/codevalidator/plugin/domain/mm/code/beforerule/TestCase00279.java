package com.yonyou.nc.codevalidator.plugin.domain.mm.code.beforerule;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
 * ����BP��ǰ�����Զ����������Ϣ�Ĺ��� (nc.bs.pubapp.pub.rule.FillUpdateDataRule).
 * 
 * @since 6.0
 * @version 2013-11-4 ����10:53:17
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "����BP��ǰ�����Զ����������Ϣ�Ĺ��� (nc.bs.pubapp.pub.rule.FillUpdateDataRule)��", relatedIssueId = "279", coder = "zhongcha", solution = "��ǰ��������ӵ��ݺŵĸ��¹���(nc.bs.pubapp.pub.rule.UpdateBillCodeRule)��")
public class TestCase00279 extends AbstractJavaQueryRuleDefinition {

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

        // ��¼û��addBeforeRule()����
        List<String> notMethodClz = new ArrayList<String>();
        // ��¼û��ִ�е��ݺŵĸ��¹������
        List<String> notRuleClz = new ArrayList<String>();
        // ��Ҫ�����������package�������Ķ�Ӧ
        Map<String, JavaClassResource> package2BPClzs = new HashMap<String, JavaClassResource>();
        try {
            for (JavaClassResource javaClassResource : resources) {
                String fileName = javaClassResource.getJavaCodeClassName();
                // Ĭ����Ϊͬһ�����´��ڶ����׺��ͬ���࣬��������̵�����Ҫ����
                if (fileName.endsWith("UpdateBP")) {
                    String filePackage = fileName.substring(0, fileName.lastIndexOf("."));
                    if (!package2BPClzs.keySet().contains(filePackage) || package2BPClzs.keySet().contains(filePackage)
                            && package2BPClzs.get(filePackage).getJavaCodeClassName().length() > fileName.length()) {
                        package2BPClzs.put(filePackage, javaClassResource);
                        break;
                    }
                }
            }
            if (MMValueCheck.isEmpty(package2BPClzs)) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        "������в�����������׺ΪUpdateBP��BP�ࡣ\n");
                return result;
            }
            for (JavaClassResource javaClassResource : package2BPClzs.values()) {
                String fileName = javaClassResource.getJavaCodeClassName();
                ScanUpdateBPRule scanRule = new ScanUpdateBPRule();
                // ɨ���java�ļ��ж��Ƿ�����жϵ���״̬�Ĺ���
                SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(), scanRule);
                if (!scanRule.isExistMethod()) {
                    notMethodClz.add(fileName);
                }
                if (scanRule.isExistMethod() && !scanRule.isExistRule()) {
                    notRuleClz.add(fileName);
                }
            }
            StringBuilder errorContxt = new StringBuilder();

            if (MMValueCheck.isNotEmpty(notMethodClz)) {
                errorContxt.append("���������Ϊ" + notMethodClz + "��BP��û��addBeforeRule�ķ�����\n");
            }
            if (MMValueCheck.isNotEmpty(notRuleClz)) {
                errorContxt.append("���������Ϊ" + notRuleClz
                        + "��BP���addBeforeRule�ķ�����û��ִ���Զ����������Ϣ�Ĺ��� (nc.bs.pubapp.pub.rule.FillUpdateDataRule)��\n");
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
     * ɨ��java�ļ��Ĺ��򣨼ٶ��û�������дjava.lang���е��ࣩ
     * 
     * @since 6.0
     * @version 2013-10-30 ����2:02:23
     * @author zhongcha
     */
    private static class ScanUpdateBPRule extends AbstractJavaRule {
        private String currentPackageName;

        private Map<String, String> importClassMap = new HashMap<String, String>();

        private boolean isExistMethod = false;

        public boolean isExistMethod() {
            return this.isExistMethod;
        }

        private boolean isExistRule = false;

        public boolean isExistRule() {
            return this.isExistRule;
        }

        @Override
        public Object visit(ASTPackageDeclaration node, Object data) {
            List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
            if (astNames.size() == 1) {
                ASTName astName = astNames.get(0);
                this.currentPackageName = astName.getImage();
            }
            else {
                RuleContext ruleContext = (RuleContext) data;
                ruleContext.addRuleViolation(ruleContext, this, node, "", null);
                return data;
            }
            return super.visit(node, data);
        }

        @Override
        public Object visit(ASTImportDeclaration node, Object data) {
            List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
            if (!astNames.isEmpty()) {
                for (ASTName astName : astNames) {
                    String importClassName = astName.getImage();
                    this.importClassMap.put(importClassName.substring(importClassName.lastIndexOf(".") + 1),
                            importClassName);
                }
            }
            return super.visit(node, data);
        }

        /**
         * ɨ�跽������¼��ǰ�ķ�����
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
                                if (!image.contains(".")) {
                                    image =
                                            this.importClassMap.containsKey(image) ? this.importClassMap.get(image)
                                                    : String.format("%s.%s", this.currentPackageName, image);
                                }
                                if ("nc.bs.pubapp.pub.rule.FillUpdateDataRule".equals(image)) {
                                    this.isExistRule = true;
                                    return data;
                                }
                            }
                        }
                    }
                }
            }
            return super.visit(node, data);
        }

    }

}
