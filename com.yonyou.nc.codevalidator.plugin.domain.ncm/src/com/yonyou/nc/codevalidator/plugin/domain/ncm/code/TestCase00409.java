package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "提高数据库访问效率",
		memo = "在数据库访问层代码中使用预处理的PreparedStatement来代替Statement，以提高数据库访问性能。  ",
		solution = "PreparedStatement来代替Statement", specialParamDefine = { "" }, coder = "luoxin3",
		subCatalog = SubCatalogEnum.JC_CODECRITERION, repairLevel = RepairLevel.SUGGESTREPAIR, relatedIssueId = "409",
		executePeriod = ExecutePeriod.CHECKOUT)
@PublicRuleDefinitionParam(params = { CommonRuleParams.CODECONTAINEXTRAFOLDER })
public class TestCase00409 extends AbstractJavaQueryRuleDefinition {

	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws ResourceParserException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.PUBLIC);
		return javaResourceQuery;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		try {
			for (JavaClassResource resource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(),
						new CheckStatementRule());
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String generateErrorDetail = SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (generateErrorDetail != null && generateErrorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), generateErrorDetail);
				}
			}
		} catch (FileNotFoundException e) {
			throw new ResourceParserException(e);
		}
		return result;
	}

	public static class CheckStatementRule extends AbstractJavaRule {
		private static final String JAVA_SQL_STRING = "java.sql";
		private static final String STATEMENT_STRING = "Statement";
		private static final String PREPAREDSTATEMENT_STRING = "PreparedStatement";
		private boolean hasJavaSqlPackage = false;

		@Override
		public Object visit(ASTCompilationUnit node, Object data) {
			Object result = super.visit(node, data);
			hasJavaSqlPackage = false;
			return result;
		}

		@Override
		public Object visit(ASTImportDeclaration node, Object data) {
			List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
			for (ASTName astName : astNames) {
				if (astName.getImage().equals(JAVA_SQL_STRING)) {
					hasJavaSqlPackage = true;
					return super.visit(node, data);
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTReferenceType node, Object data) {
			List<ASTClassOrInterfaceType> types = node.findDescendantsOfType(ASTClassOrInterfaceType.class);
			for (ASTClassOrInterfaceType type : types) {
				String image = type.getImage();
				if (hasJavaSqlPackage && image.contains(STATEMENT_STRING) && !image.equals(PREPAREDSTATEMENT_STRING)) {
					RuleContext ruleContext = (RuleContext) data;
					ruleContext
							.addRuleViolation(ruleContext, this, node, "在数据库访问时使用PreparedStatement代替Statement", null);
					break;
				}
			}
			return super.visit(node, data);
		}
	}
}
