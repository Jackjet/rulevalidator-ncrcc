package com.yonyou.nc.codevalidator.plugin.domain.dmm.code;

import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
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
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, coder = "songkj", description = "前台未通过服务直接调用后台方法校验规则（针对专项补丁）", relatedIssueId = "752", subCatalog = SubCatalogEnum.JC_CODECRITERION, repairLevel = RepairLevel.MUSTREPAIR, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00752 extends AbstractJavaQueryRuleDefinition {

    @Override
    protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
        javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
        javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
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
                        SourceCodeProcessor.parseRule(resource.getResourcePath(), new ImportPrivateCodeCheckRule());
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

    public static class ImportPrivateCodeCheckRule extends AbstractJavaRule {
        private static final String NC_BS_STRING = "nc.bs.";

        private static final String NC_BS_UIF2_STRING = "nc.bs.uif2";

        private static final String NC_BS_PUBAPP_STRING = "nc.bs.pubapp";

        private static final String NC_BS_LOGGING_STRING = "nc.bs.logging";

        private static final String NC_IMPL_STRING = "nc.impl";

        private static final String NC_PUBIMPL_STRING = "nc.pubimpl";

        private static final String NCLOCATOR_STRING = "nc.bs.framework.common";

        @Override
        public Object visit(ASTImportDeclaration node, Object data) {
            RuleContext ruleContext = (RuleContext) data;
            List<ASTName> nameList = node.findChildrenOfType(ASTName.class);
            if (nameList.size() == 1) {
                String image = nameList.get(0).getImage();
                if (image.startsWith(ImportPrivateCodeCheckRule.NC_BS_STRING)) {
                    if (!image.startsWith(ImportPrivateCodeCheckRule.NC_BS_UIF2_STRING)
                            && !image.startsWith(ImportPrivateCodeCheckRule.NC_BS_PUBAPP_STRING)
                            && !image.startsWith(ImportPrivateCodeCheckRule.NC_BS_LOGGING_STRING)
                            && !image.startsWith(ImportPrivateCodeCheckRule.NCLOCATOR_STRING)) {
                        ruleContext.addRuleViolation(ruleContext, this, node, "前台未通过服务直接调用后台方法", null);
                    }
                }
                if (image.startsWith(ImportPrivateCodeCheckRule.NC_IMPL_STRING)
                        || image.startsWith(ImportPrivateCodeCheckRule.NC_PUBIMPL_STRING)) {
                    ruleContext.addRuleViolation(ruleContext, this, node, "前台未通过服务直接调用后台方法", null);
                }
            }
            return super.visit(node, data);
        }
    }
}
