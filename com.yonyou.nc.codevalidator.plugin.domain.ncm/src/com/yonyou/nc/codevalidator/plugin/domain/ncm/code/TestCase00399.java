package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.IOException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
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

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "当调用equals方法时，如果对象为空值会抛出空指针异常",
		solution = "使用确定不为空的对象调用equals方法，即调换equals前后两个参数的位置", relatedIssueId = "399", coder = "wumr3",
		subCatalog = SubCatalogEnum.JC_CODECRITERION, repairLevel = RepairLevel.SUGGESTREPAIR,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00399 extends AbstractJavaQueryRuleDefinition {

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
		try {
			for (JavaClassResource resource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(),
						new EqualsCheckRule());
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String errorDetail = net.sourceforge.pmd.SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (errorDetail != null && errorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), errorDetail);
				}
			}
			return result;
		} catch (IOException e) {
			throw new ResourceParserException(e);
		}
	}

	public static class EqualsCheckRule extends AbstractJavaRule {

		private boolean hasName = false;

		/**
		 * 寻找 object.equals表达式，如果存在则检查equals的参数，看是否是静态字符串
		 */
		@Override
		public Object visit(ASTPrimaryExpression node, Object data) {
			if (node.hasDecendantOfAnyType(ASTName.class)) {
				List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
				for (ASTName astName : astNames) {
					if (astName.getImage().contains(".equals")) {
						hasName = true;
						Object result = super.visit(node, data);
						hasName = false;
						return result;
					}
				}
			}
			return data;
		}

		@Override
		public Object visit(ASTPrimarySuffix node, Object data) {
			if (hasName && node.hasDecendantOfAnyType(ASTArguments.class)) {
				List<ASTLiteral> astLiterals = node.findDescendantsOfType(ASTLiteral.class);
				for (ASTLiteral astLiteral : astLiterals) {
					if (astLiteral.isStringLiteral()) {
						RuleContext ruleContext = (RuleContext) data;
						String detailMessage = "建议把固定字符放在equals前";
						ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
					}
				}
				return super.visit(node, data);
			}
			return data;
		}
	}

}
