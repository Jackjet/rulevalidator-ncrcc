package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.printtemplet;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTEnumBody;
import net.sourceforge.pmd.lang.java.ast.ASTEnumConstant;
import net.sourceforge.pmd.lang.java.ast.ASTEnumDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.JavaNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMArrayUtil;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * 插入点是否可用。 通过关键字符*PluginPoint.java取bs包下的*PluginPoint.java文件.反射,得到一个枚举实例,
 * 判断如果存在这些iPluginPoint.getComponent() == null || iPluginPoint.getModule() ==
 * null || iPluginPoint.getPoint() == null 则处理不正确
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(
		executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.COMPILE,
		catalog = CatalogEnum.JAVACODE,
		subCatalog = SubCatalogEnum.JC_CODECRITERION,
		description = "插入点是可用",
		relatedIssueId = "331",
		coder = "wangfra",
		solution = "通过关键字符PluginPoint取bs包下的*PluginPoint.java文件.反射,得到一个枚举实例，判断如果【getComponent() == null || getModule() == null ||getPoint() == null 】则处理不正确.")
public class TestCase00331 extends AbstractJavaQueryRuleDefinition {

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
		Map<String, JavaClassResource> javaClazzResMap = new HashMap<String, JavaClassResource>();

		for (final JavaClassResource javaClassResource : resources) {

			String className = javaClassResource.getJavaCodeClassName();

			if (className.endsWith("BP")) {
				String key = className.substring(0, className.lastIndexOf("."));
				javaClazzResMap.put(key, javaClassResource);
			}
		}

		Set<String> keys = javaClazzResMap.keySet();
		for (String key : keys) {

			JavaClassResource javaClassRes = javaClazzResMap.get(key);

			// 检查
			this.check(javaClassRes, ruleExecContext, result);

		}

		return result;
	}

	private void check(JavaClassResource javaClassResource, IRuleExecuteContext ruleExecContext,
			ResourceRuleExecuteResult result) {
		try {
			StringBuilder noteBuilder = new StringBuilder();
			String className = javaClassResource.getJavaCodeClassName();

			Class<?> loadClass;

			loadClass = ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
					ruleExecContext.getBusinessComponent().getProjectName(), className);

//			RuleContext ruleContext = SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(),
//					new ScanPluginPointRule(className));
			// List<RuleViolation> ruleViolationList =
			// ruleContext.getRuleViolationList();
			// ***********************************************************************
			Object[] enums = loadClass.getEnumConstants();

			/*
			 * 执行 getComponent(), getModule(), getPoint()三个方法，如果有一个返回null，则不通过
			 */

			if (MMArrayUtil.isEmpty(enums)) {
				noteBuilder.append(String.format("当前节点的规则插入点没有正确定义枚举常量,检查枚举类【%s】\n ！",
						javaClassResource.getJavaCodeClassName()));
			} else {
				Method[] methods = loadClass.getMethods();
				final List<String> itfMethodnames = new ArrayList<String>();
				for (Method method : methods) {
					itfMethodnames.add(method.getName());
				}
				List<String> enumsValues = new ArrayList<String>();
				for (Object value : enums) {
					enumsValues.add(className + "." + value);
				}
				CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
				final StringBuilder noteBuilder1 = new StringBuilder();
				VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<Void>() {
					@Override
					public void visit(MethodDeclaration n, Void v) {
						if (itfMethodnames.contains(n.getName())) {
							BlockStmt body = n.getBody();
							if (body == null) {
								return;
							}
							List<Statement> stmts = body.getStmts();
							if (stmts == null) {
								return;
							}
							boolean isContainsMarch = false;
							// String point = null;
							for (Statement stmt : stmts) {
								for (String value : itfMethodnames) {
									if (stmt.toString().contains(value)) {
										isContainsMarch = true;
										break;
									}
								}
							}
							if (!isContainsMarch) {
								noteBuilder1.append(String.format("类 %s 中定义的插入点不可用!\n", n.getClass()));
							}
						}
					}
				};
				visitor.visit(compilationUnit, null);
				if (noteBuilder.toString().length() > 0) {
					result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
				}

			}
			if (noteBuilder.toString().length() > 0) {
				result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
			}
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (RuleClassLoadException e) {

			e.printStackTrace();

		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 假定用户不会重写java.lang包中的类
	 * 
	 * @author wangfra
	 */
	public static class ScanPluginPointRule extends AbstractJavaRule {

		private String currentPackageName;

		private String upmImplClassList;

		private Map<String, String> importClassMap = new HashMap<String, String>();

		ScanPluginPointRule(String upmImplClassList) {
			this.upmImplClassList = upmImplClassList;
		}

		@Override
		public Object visit(ASTPackageDeclaration node, Object data) {
			List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
			if (astNames.size() == 1) {
				ASTName astName = astNames.get(0);
				this.currentPackageName = astName.getImage();
			} else {
				RuleContext ruleContext = (RuleContext) data;
				ruleContext.addRuleViolation(ruleContext, this, node, "", null);
				return data;
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTCompilationUnit node, Object data) {
			Object result = super.visit(node, data);
			return result;
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

		@Override
		public Object visit(ASTAllocationExpression node, Object data) {
			List<ASTClassOrInterfaceType> findDescendantsOfType = node
					.findChildrenOfType(ASTClassOrInterfaceType.class);
			if (!findDescendantsOfType.isEmpty()) {
				for (ASTClassOrInterfaceType astClassOrInterfaceType : findDescendantsOfType) {
					String classType = astClassOrInterfaceType.getImage();
					if (!classType.contains(".")) {
						classType = this.importClassMap.containsKey(classType) ? this.importClassMap.get(classType)
								: String.format("%s.%s", this.currentPackageName, classType);
					}
					if (this.upmImplClassList.contains(classType)) {
						RuleContext ruleContext = (RuleContext) data;
						ruleContext.addRuleViolation(ruleContext, this, node, "错误信息", null);
					}
				}
			}
			return super.visit(node, data);
		}

		public Object visit(ASTEnumBody node, Object data) {
			List<ASTEnumBody> enumBodies = node.findDescendantsOfType(ASTEnumBody.class);
			for (ASTEnumBody enumBody : enumBodies) {
				System.out.println(enumBody);
			}
			return visit((JavaNode) node, data);
		}

		public Object visit(ASTEnumConstant node, Object data) {
			List<ASTEnumConstant> enumConstants = node.findDescendantsOfType(ASTEnumConstant.class);
			for (ASTEnumConstant enumConstant : enumConstants) {
				System.out.println(enumConstant);
			}
			return visit((JavaNode) node, data);
		}

		public Object visit(ASTEnumDeclaration node, Object data) {
			List<ASTEnumDeclaration> enumConstants = node.findDescendantsOfType(ASTEnumDeclaration.class);
			for (ASTEnumDeclaration enumConstant : enumConstants) {
				System.out.println(enumConstant);
			}
			return visit((JavaNode) node, data);
		}

		public Object visit(ASTFormalParameter node, Object data) {
			List<ASTFormalParameter> enumConstants = node.findDescendantsOfType(ASTFormalParameter.class);
			for (ASTFormalParameter enumConstant : enumConstants) {
				System.out.println(enumConstant);
			}
			return visit((JavaNode) node, data);
		}

		public Object visit(ASTName node, Object data) {
			List<ASTName> enumConstants = node.findDescendantsOfType(ASTName.class);
			for (ASTName enumConstant : enumConstants) {
				System.out.println(enumConstant);
			}
			return visit((JavaNode) node, data);
		}

		public Object visit(ASTArguments node, Object data) {
			List<ASTArguments> enumConstants = node.findDescendantsOfType(ASTArguments.class);
			for (ASTArguments enumConstant : enumConstants) {
				System.out.println(enumConstant);
			}
			return visit((JavaNode) node, data);
		}
	}

}