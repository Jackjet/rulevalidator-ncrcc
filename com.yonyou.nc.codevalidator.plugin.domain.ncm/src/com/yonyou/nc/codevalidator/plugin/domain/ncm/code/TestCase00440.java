package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
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

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "实现外部接口的实现类时,应该减少或者不要定义实例变量 ",
		memo = "接口的调用方用单例模式实例化接口的实现类，导致在并发的情况下，实现类的成员变量成为了共享资源，引发并发错误。", solution = "建议注册的接口实现类不定义成员变量",
		repairLevel = RepairLevel.SUGGESTREPAIR, specialParamDefine = { "interfaceNames" }, coder = "luoxin3",
		subCatalog = SubCatalogEnum.JC_CONCURRENISSUES, relatedIssueId = "440", executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00440 extends AbstractJavaQueryRuleDefinition {
	private static final String INTERFACENAMES = "interfaceNames";

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
		String[] parameterArray = ruleExecContext.getParameterArray(INTERFACENAMES);
		try {
			for (final JavaClassResource resource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(),
						new ReduceFiledInImplementation(parameterArray));
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String generateErrorDetail = SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (generateErrorDetail != null && generateErrorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), generateErrorDetail);
				}
			}
			return result;
		} catch (FileNotFoundException e) {
			throw new ResourceParserException(e);
		}
	}

	public static class ReduceFiledInImplementation extends AbstractJavaRule {
		private boolean isImplement = false;

		private String[] interfaces;

		public ReduceFiledInImplementation(String[] interfaces) {
			this.interfaces = interfaces;
		}

		@Override
		public Object visit(ASTClassOrInterfaceBodyDeclaration node, Object data) {
			if (isImplement) {
				List<ASTFieldDeclaration> astFieldDeclarations = node.findDescendantsOfType(ASTFieldDeclaration.class);
				for (ASTFieldDeclaration astFieldDeclaration : astFieldDeclarations) {
					if (astFieldDeclaration.isFinal() && astFieldDeclaration.isStatic()) {
						continue;
					} else {
						RuleContext ruleContext = (RuleContext) data;
						ruleContext.addRuleViolation(ruleContext, this, node, "该实现类中含有成员变量", null);
					}
				}
			}
			return super.visit(node, data);
		}

		@Override
		public Object visit(ASTImplementsList node, Object data) {
			List<ASTClassOrInterfaceType> interfaceTypes = node.findDescendantsOfType(ASTClassOrInterfaceType.class);
			for (ASTClassOrInterfaceType astClassOrInterfaceType : interfaceTypes) {
				if (interfaces != null) {
					for (int i = 0; i < interfaces.length; i++) {
						if (astClassOrInterfaceType.getImage().equals(interfaces[i])) {
							isImplement = true;
							break;
						}
						isImplement = false;
					}
				}
			}
			return super.visit(node, data);
		}
	}
}
