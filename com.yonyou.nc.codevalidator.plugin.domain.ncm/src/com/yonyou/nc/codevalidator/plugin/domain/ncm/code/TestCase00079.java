package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTAdditiveExpression;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMultiplicativeExpression;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimitiveType;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
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

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "java里的double不能精确运算结果不准确 ",
		solution = "将NC源代码里所有参与运算的double,Double替换为可以运算的NC控件", coder = "wumr3", relatedIssueId = "79",
		subCatalog = SubCatalogEnum.JC_CODECRITERION, repairLevel = RepairLevel.MUSTREPAIR,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00079 extends AbstractJavaQueryRuleDefinition {

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
				ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(), new DoubleCheckRule());
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String errorDetail = net.sourceforge.pmd.SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (errorDetail != null && errorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), errorDetail);
				}
			} catch (FileNotFoundException e) {
				throw new ResourceParserException(e);
			}
		}
		return result;
	}

	public static class DoubleCheckRule extends AbstractJavaRule {
		private boolean findMethod = false;
		private boolean findClass = false;
		private boolean notParam = false;

		private List<String> localDoubleParams = new ArrayList<String>();// 用来存储局部的double或Double变量
		private List<String> globalDoubelParams = new ArrayList<String>();// 用来存储全局的double或Double变量

		/**
		 * visit乘法或除法表达式
		 * 
		 */
		@Override
		public Object visit(ASTMultiplicativeExpression node, Object data) {// 检测乘法或除法运算
			return processCalculateMethod(node, data, "乘法或除法");
		}

		/**
		 * visit加法或减法表达式
		 * */
		@Override
		public Object visit(ASTAdditiveExpression node, Object data) {
			notParam = false;
			return processCalculateMethod(node, data, "加法和减法");
		}

		/**
		 * 检测加减乘除表达式中是否有double或Double参与运算，参与则提示信息
		 * 
		 * @param node
		 *            加减乘除表达式
		 * @param data
		 * @param message
		 *            具体表达式
		 * @return
		 */
		private Object processCalculateMethod(AbstractJavaTypeNode node, Object data, String message) {
			RuleContext ruleContext = (RuleContext) data;
			List<ASTLiteral> astLiterals = node.findDescendantsOfType(ASTLiteral.class);
			for (ASTLiteral astLiteral : astLiterals) {
				if (astLiteral.isStringLiteral()) {
					notParam = true;
					break;
				}
			}
			if (!notParam) {

				List<ASTName> calculateNames = node.findDescendantsOfType(ASTName.class);
				if (!calculateNames.isEmpty()) {
					for (ASTName calculateName : calculateNames) {
						String calculateParamName = calculateName.getImage();
						if (calculateParamName.contains(".doubleValue")) {
							calculateParamName = calculateParamName.substring(0,
									calculateParamName.indexOf(".doubleValue"));
						}
						if (localDoubleParams.contains(calculateParamName)) {
							String detailMessage = String.format("表达式中double类型变量：%s!参与%s运算", calculateName.getImage(),
									message);
							ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
						}
						if (globalDoubelParams.contains(calculateParamName)) {
							String detailMessage = String.format("表达式中double类型变量：%s!参与%s运算", calculateName.getImage(),
									message);
							ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
						}
					}
				}
				for (ASTLiteral calculateName : astLiterals) {
					if (calculateName.isFloatLiteral()) {
						String detailMessage = String.format("变量中包含double类型的%s运算:%s！", calculateName.getImage(),
								message);
						ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
					}
				}
				notParam = false;
			}

			return data;
		}

		private void initMethod() {// 方法访问初始化
			findMethod = true;
		}

		private void initClass() {// 类访问初始化
			findClass = true;
		}

		private void cleanMethod() {// 访问方法结束清空数据
			findMethod = false;
			localDoubleParams.clear();
		}

		private void cleanClass() {// 访问类结束清空数据
			findClass = false;
			globalDoubelParams.clear();
		}

		/**
		 * 在方法中寻找运算表达式，把方法中的double或Double参数放入list中
		 */
		@Override
		public Object visit(ASTMethodDeclaration node, Object data) {// 检测方法参数中含有double类型
			List<AbstractJavaTypeNode> allCalculateExpressionList = new ArrayList<AbstractJavaTypeNode>();
			allCalculateExpressionList.addAll(node.findDescendantsOfType(ASTAdditiveExpression.class));
			allCalculateExpressionList.addAll(node.findDescendantsOfType(ASTMultiplicativeExpression.class));
			if (!allCalculateExpressionList.isEmpty()) {
				initMethod();
				List<ASTFormalParameter> methodParameters = node.findDescendantsOfType(ASTFormalParameter.class);
				for (ASTFormalParameter astFormalParameter : methodParameters) {
					// TODO: parameter中有Double怎么办？
					ASTType astType = (ASTType) astFormalParameter.jjtGetChild(0);
					if (astType.jjtGetChild(0) instanceof ASTPrimitiveType) {
						ASTPrimitiveType primitiveType = (ASTPrimitiveType) astType.jjtGetChild(0);
						if ("double".equals(primitiveType.getImage())) {
							List<ASTVariableDeclaratorId> variableIds = astFormalParameter
									.findDescendantsOfType(ASTVariableDeclaratorId.class);
							for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
								localDoubleParams.add(astVariableDeclaratorId.getImage());
							}
						}
					}
					if (astType.jjtGetChild(0) instanceof ASTReferenceType) {
						List<ASTClassOrInterfaceType> astClassOrInterfaceTypes = astType
								.findDescendantsOfType(ASTClassOrInterfaceType.class);
						if (!astClassOrInterfaceTypes.isEmpty()) {
							for (ASTClassOrInterfaceType classOrInterfaceType : astClassOrInterfaceTypes) {
								if ("Double".equals(classOrInterfaceType.getImage())) {
									List<ASTVariableDeclaratorId> variableIds = astFormalParameter
											.findDescendantsOfType(ASTVariableDeclaratorId.class);
									for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
										localDoubleParams.add(astVariableDeclaratorId.getImage());
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
		 * 将局部的double或Double变量放入localDoubleParams中
		 */
		@Override
		public Object visit(ASTLocalVariableDeclaration node, Object data) {// 检测方法中定义局部的double变量
			if (findMethod) {
				List<ASTType> astTypes = node.findChildrenOfType(ASTType.class);
				if(astTypes.size() == 1) {
					ASTType astType = astTypes.get(0);
					if (astType.jjtGetChild(0) instanceof ASTPrimitiveType) {
						ASTPrimitiveType primitiveType = (ASTPrimitiveType) astType.jjtGetChild(0);
						if ("double".equals(primitiveType.getImage())) {
							List<ASTVariableDeclaratorId> findDescendantsOfType = node
									.findDescendantsOfType(ASTVariableDeclaratorId.class);
							if (!findDescendantsOfType.isEmpty()) {
								for (ASTVariableDeclaratorId astVariableDeclaratorId : findDescendantsOfType) {
									localDoubleParams.add(astVariableDeclaratorId.getImage());
								}
							}
						}
					}
					if (astType.jjtGetChild(0) instanceof ASTReferenceType) {
						List<ASTClassOrInterfaceType> astClassOrInterfaceTypes = astType
								.findDescendantsOfType(ASTClassOrInterfaceType.class);
						if (!astClassOrInterfaceTypes.isEmpty()) {
							for (ASTClassOrInterfaceType classOrInterfaceType : astClassOrInterfaceTypes) {
								if ("Double".equals(classOrInterfaceType.getImage())) {
									List<ASTVariableDeclaratorId> variableIds = node
											.findDescendantsOfType(ASTVariableDeclaratorId.class);
									for (ASTVariableDeclaratorId astVariableDeclaratorId : variableIds) {
										localDoubleParams.add(astVariableDeclaratorId.getImage());
									}
								}
							}
						}
					}
					return super.visit(node, data);
				}
			}
			return data;
		}

		/**
		 * 将全局的double或Double放入globalDoubelParams中
		 */
		@Override
		public Object visit(ASTFieldDeclaration node, Object data) {// 检测double类型的成员变量
			if (findClass) {
				List<ASTPrimitiveType> findPrimitiveTypes = node.findDescendantsOfType(ASTPrimitiveType.class);
				List<ASTClassOrInterfaceType> findReferenceTypes = node
						.findDescendantsOfType(ASTClassOrInterfaceType.class);
				if (!findPrimitiveTypes.isEmpty()) {
					for (ASTPrimitiveType astPrimitiveType : findPrimitiveTypes) {
						if ("double".equals(astPrimitiveType.getImage())) {
							List<ASTVariableDeclaratorId> findDescendantsOfType = node
									.findDescendantsOfType(ASTVariableDeclaratorId.class);
							if (!findDescendantsOfType.isEmpty()) {
								for (ASTVariableDeclaratorId astVariableDeclaratorId : findDescendantsOfType) {
									globalDoubelParams.add(astVariableDeclaratorId.getImage());
								}
							}
						}
					}
					return super.visit(node, data);
				}
				if (!findReferenceTypes.isEmpty()) {
					for (ASTClassOrInterfaceType astClassOrInterfaceType : findReferenceTypes) {
						if ("Double".equals(astClassOrInterfaceType.getImage())) {
							List<ASTVariableDeclaratorId> findDescendantsOfType = node
									.findDescendantsOfType(ASTVariableDeclaratorId.class);
							if (!findDescendantsOfType.isEmpty()) {
								for (ASTVariableDeclaratorId astVariableDeclaratorId : findDescendantsOfType) {
									globalDoubelParams.add(astVariableDeclaratorId.getImage());
								}
							}
						}
					}
					return super.visit(node, data);
				}
			}
			return data;
		}

		@Override
		public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {// 开始访问类
			node.hasDescendantOfType(ASTMethodDeclaration.class);
			if (node.hasDescendantOfType(ASTMethodDeclaration.class)) {
				initClass();
				Object result = super.visit(node, data);
				cleanClass();
				return result;
			}
			return data;
		}
	}

}
