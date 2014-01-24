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
 * 更新BP的前规则：自动更新审计信息的规则 (nc.bs.pubapp.pub.rule.FillUpdateDataRule).
 * 
 * @since 6.0
 * @version 2013-11-4 上午10:53:17
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "更新BP的前规则：自动更新审计信息的规则 (nc.bs.pubapp.pub.rule.FillUpdateDataRule)。", relatedIssueId = "279", coder = "zhongcha", solution = "在前规则中添加单据号的更新规则(nc.bs.pubapp.pub.rule.UpdateBillCodeRule)。")
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

        // 记录没有addBeforeRule()的类
        List<String> notMethodClz = new ArrayList<String>();
        // 记录没有执行单据号的更新规则的类
        List<String> notRuleClz = new ArrayList<String>();
        // 需要检查的类的所在package和类名的对应
        Map<String, JavaClassResource> package2BPClzs = new HashMap<String, JavaClassResource>();
        try {
            for (JavaClassResource javaClassResource : resources) {
                String fileName = javaClassResource.getJavaCodeClassName();
                // 默认认为同一个包下存在多个后缀相同的类，则类名最短的是需要检查的
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
                        "该组件中不存在类名后缀为UpdateBP的BP类。\n");
                return result;
            }
            for (JavaClassResource javaClassResource : package2BPClzs.values()) {
                String fileName = javaClassResource.getJavaCodeClassName();
                ScanUpdateBPRule scanRule = new ScanUpdateBPRule();
                // 扫描该java文件判定是否存在判断单据状态的规则
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
                errorContxt.append("该组件中名为" + notMethodClz + "的BP类没有addBeforeRule的方法。\n");
            }
            if (MMValueCheck.isNotEmpty(notRuleClz)) {
                errorContxt.append("该组件中名为" + notRuleClz
                        + "的BP类的addBeforeRule的方法中没有执行自动更新审计信息的规则 (nc.bs.pubapp.pub.rule.FillUpdateDataRule)。\n");
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
     * 扫描java文件的规则（假定用户不会重写java.lang包中的类）
     * 
     * @since 6.0
     * @version 2013-10-30 下午2:02:23
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
         * 扫描方法，记录当前的方法名
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
