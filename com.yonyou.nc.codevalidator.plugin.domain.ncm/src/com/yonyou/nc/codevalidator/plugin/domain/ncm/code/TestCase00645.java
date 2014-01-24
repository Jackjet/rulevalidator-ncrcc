package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTAdditiveExpression;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTStatement;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
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
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

/**
 * 避免对字符串连接操作符（“+”）的过度使用
 * <p>
 * 由于字符串的不变性，在使用连接操作符拼接多个字符串时，
 * 会生成多个需要被垃圾回收的中间对象。而且在编译后的代码中拼接过程中会创建多个StringBuilder对象，调用append方法。
 * 比如IUFO代码里拼接sql语句的时候。这样会导致时间复杂度和空间复杂度都很高，严重影响性能。
 * 建议使用StringBuilder，并使用append方法。
 * 
 * @author luoxin3
 * @reporter 雷凤山
 */
@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "避免对字符串连接操作符（“+”）的过度使用",
		memo = "由于字符串的不变性，在使用连接操作符拼接多个字符串时，会生成多个需要被垃圾回收的中间对象。\r\n"
				+ "而且在编译后的代码中拼接过程中会创建多个StringBuilder对象，调用append方法。比如IUFO代码里拼接sql语句的时候。\r\n"
				+ "这样会导致时间复杂度和空间复杂度都很高，严重影响性能建议使用StringBuilder，并使用append方法。 ",
		solution = "建议使用StringBuilder，并使用append方法。", specialParamDefine = {}, coder = "luoxin3",
		repairLevel = RepairLevel.SUGGESTREPAIR, subCatalog = SubCatalogEnum.JC_CODECRITERION, relatedIssueId = "645",
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00645 extends AbstractJavaQueryRuleDefinition {

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
				RuleContext ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(),
						new ReduceStringAddtion());
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String generateErrorDetail = net.sourceforge.pmd.SourceCodeProcessor
						.generateErrorDetail(ruleViolationList);
				if (generateErrorDetail != null && generateErrorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), generateErrorDetail);
				}
			}
		} catch (FileNotFoundException e) {
			throw new ResourceParserException(e);
		}
		return result;
	}

	public static class ReduceStringAddtion extends AbstractJavaRule {
		private boolean isField = false;
		private boolean hasFormalPara = false;
		private boolean isString = false;
		private boolean isLocalVariable = false;
		private boolean isStatement = false;
		private boolean isEqual = false;
		private String name;
		private boolean isEqualOperator = false;

		private List<String> localStringList = new ArrayList<String>();
		private List<String> fieldStringList = new ArrayList<String>();

		@Override
		public Object visit(ASTCompilationUnit node, Object data) {
			Object result = super.visit(node, data);
			fieldStringList.clear();
			return result;
		}

		@Override
		public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
			isField = node.hasDecendantOfAnyType(ASTFieldDeclaration.class);
			Object result = super.visit(node, data);
			localStringList.clear();
			return result;
		}

		@Override
		public Object visit(ASTFieldDeclaration node, Object data) {
			boolean hasReferenceType = node.hasDecendantOfAnyType(ASTReferenceType.class);
			if (hasReferenceType) {
				List<ASTClassOrInterfaceType> interfaceTypes = node
						.findDescendantsOfType(ASTClassOrInterfaceType.class);
				for (ASTClassOrInterfaceType interfaceType : interfaceTypes) {
					if (interfaceType.getImage().equals("String")) {
						isString = true;
						Object result = super.visit(node, data);
						isField = false;
						isString = false;
						return result;
					}
				}

			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTMethodDeclarator node, Object data) {
			hasFormalPara = node.hasDecendantOfAnyType(ASTFormalParameters.class);
			Object result = super.visit(node, data);
			hasFormalPara = false;
			return result;
		}

		@Override
		public Object visit(ASTFormalParameter node, Object data) {
			boolean hasReferenceType = node.hasDecendantOfAnyType(ASTReferenceType.class);
			if (hasReferenceType && hasFormalPara) {
				List<ASTClassOrInterfaceType> interfaceTypes = node
						.findDescendantsOfType(ASTClassOrInterfaceType.class);
				for (ASTClassOrInterfaceType interfaceType : interfaceTypes) {
					if (interfaceType.getImage().equals("String")) {
						isString = true;
						Object result = super.visit(node, data);
						isString = false;
						return result;
					}
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTBlockStatement node, Object data) {
			isLocalVariable = node.hasDecendantOfAnyType(ASTLocalVariableDeclaration.class);
			if (node.getFirstChildOfType(ASTStatement.class) instanceof ASTStatement) {
				isStatement = true;
			}
			Object result = super.visit(node, data);
			isLocalVariable = false;
			isStatement = false;
			return result;
		}

		@Override
		public Object visit(ASTVariableDeclaratorId node, Object data) {
			if (isString && isField) {
				fieldStringList.add(node.getImage());
				return super.visit(node, data);
			} else if (isString && hasFormalPara) {
				localStringList.add(node.getImage());
				return super.visit(node, data);
			} else if (isString && isLocalVariable) {
				localStringList.add(node.getImage());
				return super.visit(node, data);
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTLocalVariableDeclaration node, Object data) {
			boolean hasReferenceType = node.hasDecendantOfAnyType(ASTReferenceType.class);
			if (hasReferenceType) {
				List<ASTClassOrInterfaceType> interfaceTypes = node
						.findDescendantsOfType(ASTClassOrInterfaceType.class);
				for (ASTClassOrInterfaceType interfaceType : interfaceTypes) {
					if (interfaceType.getImage().equals("String")) {
						isString = true;
						Object result = super.visit(node, data);
						isString = false;
						return result;
					}
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTStatementExpression node, Object data) {
			if (isStatement) {
				List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
				for (ASTName astName : astNames) {
					name = astName.getImage();
					for (int i = 0; i < localStringList.size(); i++) {
						if (name != null && name.equals(localStringList.get(i))) {
							isEqual = true;
							Object result = super.visit(node, data);
							isEqual = false;
							name = null;
							return result;
						}
					}
					for (int j = 0; j < fieldStringList.size(); j++) {
						if (name != null && name.equals(fieldStringList.get(j))) {
							isEqual = true;
							Object result = super.visit(node, data);
							isEqual = false;
							name = null;
							return result;
						}
					}
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTAssignmentOperator node, Object data) {
			if (isEqual && node.getImage().trim().equals("+=")) {
				RuleContext ruleContext = (RuleContext) data;
				ruleContext.addRuleViolation(ruleContext, this, node,
						"String连接次数过多，建议使用StringBuffer或者StringBuilder中append()方法连接!", null);
			} else if (isEqual && node.getImage().trim().equals("=")) {
				isEqualOperator = true;
				Object result = super.visit(node, data);
				return result;
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTExpression node, Object data) {
			if (isStatement && isEqualOperator && isEqual) {
				List<ASTAdditiveExpression> additiveExpressions = node
						.findDescendantsOfType(ASTAdditiveExpression.class);
				for (ASTAdditiveExpression additiveExpression : additiveExpressions) {
					if (additiveExpression.getImage().trim().equals("+")) {
						RuleContext ruleContext = (RuleContext) data;
						ruleContext.addRuleViolation(ruleContext, this, node,
								"String连接次数过多，建议使用StringBuffer或者StringBuilder中append()方法连接!", null);
					}
				}
			}
			return super.visit(node, data);
		}
	}

}
