package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTCastExpression;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimitiveType;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.ASTVariableInitializer;
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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "int ,integer,long 类型不允许把值传递给short类型",
		relatedIssueId = "659", subCatalog = SubCatalogEnum.JC_CODECRITERION, coder = "wumr3",
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00659 extends AbstractJavaQueryRuleDefinition {

	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws ResourceParserException {
		JavaResourceQuery resourceQuery = new JavaResourceQuery();
		resourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return resourceQuery;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		try {
			for (JavaClassResource resource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(),
						new TypeCastCheckRule());
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

	public static class TypeCastCheckRule extends AbstractJavaRule {

		private List<String> localVariable = new ArrayList<String>();
		private List<String> globalVariable = new ArrayList<String>();

		private void cleanClass() {
			globalVariable.clear();
		}

		/**
		 * 检测强制转换表达式，如果强转类型为short，变量为int,long,Integer,Long时给出提示
		 */
		@Override
		public Object visit(ASTCastExpression node, Object data) {
			List<ASTPrimitiveType> astPrimitiveTypes = node.findDescendantsOfType(ASTPrimitiveType.class);
			if (!astPrimitiveTypes.isEmpty()) {
				for (ASTPrimitiveType astPrimitiveType : astPrimitiveTypes) {
					if ("short".equals(astPrimitiveType.getImage())) {
						List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
						if (!astNames.isEmpty()) {
							for (ASTName astName : astNames) {
								String paramName = astName.getImage();
								if (globalVariable.contains(paramName) || localVariable.contains(paramName)) {
									RuleContext ruleContext = (RuleContext) data;
									String detailMessage = String.format("变量%s不允许转换成short类型！", astName.getImage());
									ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
								}
							}
						}
					}
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {

			Object result = super.visit(node, data);
			cleanClass();
			return result;

		}

		/**
		 * 将全局的int,long,Integer,Long类型的变量放入List中
		 */
		@Override
		public Object visit(ASTFieldDeclaration node, Object data) {// 检测类中全局的int,long,Integer,Long变量
			List<ASTPrimitiveType> astPrimitiveTypes = node.findDescendantsOfType(ASTPrimitiveType.class);
			List<ASTClassOrInterfaceType> astClassOrInterfaceTypes = node
					.findDescendantsOfType(ASTClassOrInterfaceType.class);
			if (!astPrimitiveTypes.isEmpty()) {
				for (ASTPrimitiveType astPrimitiveType : astPrimitiveTypes) {
					if ("long".equals(astPrimitiveType.getImage()) || "int".equals(astPrimitiveType.getImage())) {
						List<ASTVariableDeclaratorId> astVariableDeclaratorIds = node
								.findDescendantsOfType(ASTVariableDeclaratorId.class);
						if (!astVariableDeclaratorIds.isEmpty()) {
							for (ASTVariableDeclaratorId astVariableDeclaratorId : astVariableDeclaratorIds) {
								globalVariable.add(astVariableDeclaratorId.getImage());// 把int和long的全局变量加入list中
							}
						}
					}
				}

			}
			if (!astClassOrInterfaceTypes.isEmpty()) {
				for (ASTClassOrInterfaceType astClassOrInterfaceType : astClassOrInterfaceTypes) {
					if ("Integer".equals(astClassOrInterfaceType.getImage())
							|| "Long".equals(astClassOrInterfaceType.getImage())) {
						List<ASTVariableDeclaratorId> astVariableDeclaratorIds = node
								.findDescendantsOfType(ASTVariableDeclaratorId.class);
						if (!astVariableDeclaratorIds.isEmpty()) {
							for (ASTVariableDeclaratorId astVariableDeclaratorId : astVariableDeclaratorIds) {
								globalVariable.add(astVariableDeclaratorId.getImage());
							}
						}
					}
				}
			}
			return super.visit(node, data);
		}

		/**
		 * 初始化表达式
		 */
		@Override
		public Object visit(ASTVariableInitializer node, Object data) {
			RuleContext ruleContext = (RuleContext) data;
			List<ASTPrimarySuffix> astPrimarySuffixs = node.findDescendantsOfType(ASTPrimarySuffix.class);
			List<ASTPrimaryPrefix> astPrimaryPrefixs = node.findDescendantsOfType(ASTPrimaryPrefix.class);
			if (!astPrimarySuffixs.isEmpty()) {
				for (ASTPrimarySuffix astPrimarySuffix : astPrimarySuffixs) {
					if ("shortValue".equals(astPrimarySuffix.getImage())) {
						List<ASTCastExpression> astCastExpressions = node
								.findDescendantsOfType(ASTCastExpression.class);
						List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
						if (!(astCastExpressions.isEmpty() || astNames.isEmpty())) {
							for (ASTName astName : astNames) {
								String paramName = astName.getImage();
								if (globalVariable.contains(paramName) || localVariable.contains(paramName)) {
									String detailMessage = String.format("变量%s不允许转换成short类型！", astName.getImage());
									ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
								}
							}
						}
					}
				}
			}
			if (!astPrimaryPrefixs.isEmpty()) {
				for (ASTPrimaryPrefix astPrimaryPrefix : astPrimaryPrefixs) {
					List<ASTName> astNames = astPrimaryPrefix.findDescendantsOfType(ASTName.class);
					if (!astNames.isEmpty()) {
						for (ASTName astName : astNames) {
							String primaryPrefix = astName.getImage();
							if (primaryPrefix.contains(".shortValue")) {
								String paramName = primaryPrefix.substring(0, primaryPrefix.indexOf("."));// 获取变量名
								if (globalVariable.contains(paramName) || localVariable.contains(paramName)) {
									String detailMessage = String.format("变量%s不允许转换成short类型！", paramName);
									ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
								}
							}
						}
					}

				}
			}
			return super.visit(node, data);
		}

		private void cleanMethod() {// 清除局部变量
			localVariable.clear();
		}

		/**
		 * 将方法中参数是int,long,Integer,Long类型的放入List中
		 */
		@Override
		public Object visit(ASTMethodDeclaration node, Object data) {

			List<ASTFormalParameter> methodParameters = node.findDescendantsOfType(ASTFormalParameter.class);
			if (!methodParameters.isEmpty()) {
				for (ASTFormalParameter astFormalParameter : methodParameters) {
					ASTType astType = (ASTType) astFormalParameter.jjtGetChild(0);
					if (astType.jjtGetChild(0) instanceof ASTPrimitiveType) {
						ASTPrimitiveType primitiveType = (ASTPrimitiveType) astType.jjtGetChild(0);
						if ("int".equals(primitiveType.getImage()) || "long".equals(primitiveType.getImage())) {
							List<ASTVariableDeclaratorId> variableIds = astFormalParameter
									.findDescendantsOfType(ASTVariableDeclaratorId.class);
							for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
								localVariable.add(astVariableDeclaratorId.getImage());
							}
						}
					}
					if (astType.jjtGetChild(0) instanceof ASTReferenceType) {
						List<ASTClassOrInterfaceType> astClassOrInterfaceTypes = astType
								.findDescendantsOfType(ASTClassOrInterfaceType.class);
						if (!astClassOrInterfaceTypes.isEmpty()) {
							for (ASTClassOrInterfaceType classOrInterfaceType : astClassOrInterfaceTypes) {
								if ("Integer".equals(classOrInterfaceType.getImage())
										|| "Long".equals(classOrInterfaceType.getImage())) {
									List<ASTVariableDeclaratorId> variableIds = astFormalParameter
											.findDescendantsOfType(ASTVariableDeclaratorId.class);
									for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
										localVariable.add(astVariableDeclaratorId.getImage());
									}
								}

							}
						}

					}
				}
				Object result = super.visit(node, data);
				cleanMethod();
				return result;
			}
			return data;
		}

		/**
		 * 将局部的int,long,Integer,Long类型的变量放入List中
		 */
		@Override
		public Object visit(ASTLocalVariableDeclaration node, Object data) {

			List<ASTPrimitiveType> astPrimitiveTypes = node.findDescendantsOfType(ASTPrimitiveType.class);
			List<ASTClassOrInterfaceType> astClassOrInterfaceTypes = node
					.findDescendantsOfType(ASTClassOrInterfaceType.class);
			if (!astPrimitiveTypes.isEmpty()) {
				for (ASTPrimitiveType astPrimitiveType : astPrimitiveTypes) {
					if ("long".equals(astPrimitiveType.getImage()) || "int".equals(astPrimitiveType.getImage())) {
						List<ASTVariableDeclaratorId> astVariableDeclaratorIds = node
								.findDescendantsOfType(ASTVariableDeclaratorId.class);
						if (!astVariableDeclaratorIds.isEmpty()) {
							for (ASTVariableDeclaratorId astVariableDeclaratorId : astVariableDeclaratorIds) {
								localVariable.add(astVariableDeclaratorId.getImage());// 把int和long的全局变量加入list中
							}
						}
					}
				}
			}
			if (!astClassOrInterfaceTypes.isEmpty()) {
				for (ASTClassOrInterfaceType astClassOrInterfaceType : astClassOrInterfaceTypes) {
					if ("Integer".equals(astClassOrInterfaceType.getImage())
							|| "Long".equals(astClassOrInterfaceType.getImage())) {
						List<ASTVariableDeclaratorId> astVariableDeclaratorIds = node
								.findDescendantsOfType(ASTVariableDeclaratorId.class);
						if (!astVariableDeclaratorIds.isEmpty()) {
							for (ASTVariableDeclaratorId astVariableDeclaratorId : astVariableDeclaratorIds) {
								localVariable.add(astVariableDeclaratorId.getImage());
							}
						}
					}
				}
			}
			return super.visit(node, data);
		}
	}
}
