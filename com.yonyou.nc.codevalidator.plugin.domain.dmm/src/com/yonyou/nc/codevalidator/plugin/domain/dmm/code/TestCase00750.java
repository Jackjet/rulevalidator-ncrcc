package com.yonyou.nc.codevalidator.plugin.domain.dmm.code;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, coder = "songkj", description = "自定义Action时，在setModel方法中把action设置为单据Model的观察者 ", relatedIssueId = "750", subCatalog = SubCatalogEnum.JC_CODECRITERION, repairLevel = RepairLevel.MUSTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00750 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery classResource = new JavaResourceQuery();
        classResource.setBusinessComponent(ruleExecContext.getBusinessComponent());
        classResource.setResPrivilege(JavaResPrivilege.CLIENT);
        return classResource;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {

        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        for (JavaClassResource resource : resources) {
            RuleContext ruleContext;
            try {
                String className = resource.getJavaCodeClassName();
                boolean extendsNCAction =
                        ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(
                                ruleExecContext.getBusinessComponent().getProjectName(), className,
                                "nc.ui.uif2.NCAction");
                ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(), new ActionSetModelRule());
                if (extendsNCAction) {
                    List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
                    String errorDetail = net.sourceforge.pmd.SourceCodeProcessor.generateErrorDetail(ruleViolationList);
                    if (errorDetail != null && errorDetail.trim().length() > 0) {
                        result.addResultElement(resource.getResourcePath(), errorDetail);
                    }
                }
            }
            catch (FileNotFoundException e) {
                throw new ResourceParserException(e);
            }
        }
        return result;
    }

    public static class ActionSetModelRule extends AbstractJavaRule {

        private boolean hasSetModelMethod = false;

        @Override
        public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
            RuleContext ruleContext = (RuleContext) data;

            List<ASTMethodDeclaration> methodDeclarationList = node.findDescendantsOfType(ASTMethodDeclaration.class);
            for (ASTMethodDeclaration methodDeclaration : methodDeclarationList) {
                List<ASTMethodDeclarator> methodDeclaratorList =
                        methodDeclaration.findChildrenOfType(ASTMethodDeclarator.class);
                for (ASTMethodDeclarator methodDeclarator : methodDeclaratorList) {
                    String methodName = methodDeclarator.getImage();
                    if (Pattern.matches("^set*\\w+Model$", methodName)) {
                        this.hasSetModelMethod = true;
                        List<ASTPrimarySuffix> primarySuffixList =
                                methodDeclaration.findDescendantsOfType(ASTPrimarySuffix.class);
                        boolean hasAddAppEventListenerMethod = false;
                        for (ASTPrimarySuffix primarySuffix : primarySuffixList) {
                            String image = primarySuffix.getImage();
                            if ("addAppEventListener".equals(image)) {
                                hasAddAppEventListenerMethod = true;
                                break;
                            }
                        }
                        if (!hasAddAppEventListenerMethod) {
                            ruleContext.addRuleViolation(ruleContext, this, node,
                                    "此自定义按钮set*Model方法中没有把action设置为单据Model的观察者", null);
                        }

                        break;
                    }
                }
            }
            if (!this.hasSetModelMethod) {
                List<ASTExtendsList> extendsList = node.findDescendantsOfType(ASTExtendsList.class);
                boolean extendsHandledParents = false;
                for (ASTExtendsList superClass : extendsList) {
                    List<ASTClassOrInterfaceType> ClassOrInterfaceTypeList =
                            superClass.findDescendantsOfType(ASTClassOrInterfaceType.class);
                    for (ASTClassOrInterfaceType classOrInterfaceType : ClassOrInterfaceTypeList) {
                        String image = classOrInterfaceType.getImage();
                        if (!image.equals("NCAction")) {
                            extendsHandledParents = true;
                            break;
                        }
                    }
                }
                if (!extendsHandledParents) {
                    ruleContext.addRuleViolation(ruleContext, this, node, "此自定义按钮中没有set*Model方法", null);
                }
            }
            return data;
        }
    }
}
