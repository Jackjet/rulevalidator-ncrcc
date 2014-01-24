package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTArrayDimsAndInits;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
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

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "多维数组创建时维度固定", memo = "在使用多维数组时，在创建数组时，建议值指定第一维的长度，"
		+ "其他维在循环内，根据实际数据指定为不同的长度 ", solution = "根据实际数据指定为不同的长度", specialParamDefine = { "" }, coder = "luoxin3",
		subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "418", repairLevel = RepairLevel.SUGGESTREPAIR,
		executePeriod = ExecutePeriod.CHECKOUT)
@PublicRuleDefinitionParam(params = { CommonRuleParams.CODECONTAINEXTRAFOLDER })
public class TestCase00418 extends AbstractJavaQueryRuleDefinition {

	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext) {
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
				RuleContext ruleContext = SourceCodeProcessor
						.parseRule(resource.getResourcePath(), new CheckMulArray());
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String errorDetail = net.sourceforge.pmd.SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (errorDetail != null && errorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), errorDetail);
				}
			}
			return result;
		} catch (FileNotFoundException e) {
			throw new ResourceParserException(e);
		}
	}

	public static class CheckMulArray extends AbstractJavaRule {

		@Override
		public Object visit(ASTReferenceType node, Object data) {
			if (node.getArrayDepth() > 2) {
				RuleContext ruleContext = (RuleContext) data;
				ruleContext.addRuleViolation(ruleContext, this, node, "数组维度超过2维，请使用3维以下数组！", null);
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTLocalVariableDeclaration node, Object data) {
			List<ASTReferenceType> referenceTypes = node.findDescendantsOfType(ASTReferenceType.class);
			for (ASTReferenceType astReferenceType : referenceTypes) {
				if (astReferenceType.getArrayDepth() == 2) {
					List<ASTArrayDimsAndInits> andInits = node.findDescendantsOfType(ASTArrayDimsAndInits.class);
					for (ASTArrayDimsAndInits astArrayDimsAndInits : andInits) {
						List<ASTExpression> astExpressions = astArrayDimsAndInits
								.findChildrenOfType(ASTExpression.class);
						if (astExpressions.size() == 2) {
							RuleContext ruleContext = (RuleContext) data;
							ruleContext.addRuleViolation(ruleContext, this, node, "多维数组维度不建议直接定义数组大小，建议根据实际情况在循环体内确定。",
									null);
						}
					}
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTCompilationUnit node, Object data) {
			return super.visit(node, data);
		}

	}
}
