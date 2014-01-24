package com.yonyou.nc.codevalidator.plugin.domain.dmm.code;

import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTName;
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
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, coder = "songkj", description = "UFBoolean判断时使用常量，而不是字符串\"Y\" ", relatedIssueId = "754", subCatalog = SubCatalogEnum.JC_CODECRITERION, repairLevel = RepairLevel.MUSTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00754 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        return javaResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        for (JavaClassResource resource : resources) {
            RuleContext ruleContext;
            try {
                ruleContext =
                        SourceCodeProcessor.parseRule(resource.getResourcePath(), new UFBooleanUsingStringCheckRule());
                List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
                String errorDetail = net.sourceforge.pmd.SourceCodeProcessor.generateErrorDetail(ruleViolationList);
                if (errorDetail != null && errorDetail.trim().length() > 0) {
                    result.addResultElement(resource.getResourcePath(), errorDetail);
                }
            }
            catch (FileNotFoundException e) {
                throw new ResourceParserException(e);
            }
        }
        return result;
    }

    public static class UFBooleanUsingStringCheckRule extends AbstractJavaRule {

        private static final String UFBOOLEAN = "nc.vo.pub.lang.UFBoolean";

        private static final String UFBOOLEAN_SHORTNAME = "UFBoolean";

        @Override
        public Object visit(ASTImportDeclaration node, Object data) {
            boolean findUFBooleanRef = false;
            List<ASTName> nameList = node.findChildrenOfType(ASTName.class);
            if (nameList.size() == 1) {
                String image = nameList.get(0).getImage();
                if (image.equals(UFBooleanUsingStringCheckRule.UFBOOLEAN)) {
                    findUFBooleanRef = true;
                }
            }
            if (findUFBooleanRef) {
                return super.visit(node, data);
            }
            return data;
        }

        @Override
        public Object visit(ASTAllocationExpression node, Object data) {
            RuleContext ruleContext = (RuleContext) data;
            List<ASTClassOrInterfaceType> classOrInterfaceTypeList =
                    node.findChildrenOfType(ASTClassOrInterfaceType.class);
            if (classOrInterfaceTypeList.size() == 1) {
                String image = classOrInterfaceTypeList.get(0).getImage();
                if (UFBooleanUsingStringCheckRule.UFBOOLEAN_SHORTNAME.equals(image)) {
                    List<ASTArguments> arguments = node.findChildrenOfType(ASTArguments.class);
                    if (arguments.size() == 1) {
                        List<ASTLiteral> descendantsOfType = node.findDescendantsOfType(ASTLiteral.class);
                        for (ASTLiteral astLiteral : descendantsOfType) {
                            String arg = astLiteral.getImage();
                            if(StringUtils.isNotBlank(arg)) {
                            	if (arg.equals("\"true\"")) {
                            		ruleContext.addRuleViolation(ruleContext, this, node,
                            				"UFBoolean判断时使用常量，而不是以字符串\"true\"为参数的构造方法", null);
                            	}
                            	if (arg.equals("\"Y\"")) {
                            		ruleContext.addRuleViolation(ruleContext, this, node,
                            				"UFBoolean判断时使用常量，而不是以字符串\"Y\"为参数的构造方法", null);
                            	}
                            	if (arg.equals("\"y\"")) {
                            		ruleContext.addRuleViolation(ruleContext, this, node,
                            				"UFBoolean判断时使用常量，而不是以字符串\"y\"为参数的构造方法", null);
                            	}
                            }
                        }
                    }
                }
            }
            return data;
        }
    }
}
