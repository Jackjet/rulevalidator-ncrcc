package com.yonyou.nc.codevalidator.plugin.domain.platform.code;

import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTDoStatement;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

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
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "客户端循环中避免调用远程接口", executePeriod = ExecutePeriod.CHECKOUT,
		executeLayer = ExecuteLayer.BUSICOMP, relatedIssueId = "505", subCatalog = SubCatalogEnum.JC_CODECRITERION,
		coder = "mazhqa")
public class TestCase00505 extends AbstractJavaQueryRuleDefinition {

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
		try {
			for (JavaClassResource javaClassResource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(),
						new NCLocatorForInvoke());
				String errorDetail = SourceCodeProcessor.generateErrorDetail(ruleContext.getRuleViolationList());
				if (!StringUtils.isBlank(errorDetail)) {
					result.addResultElement(javaClassResource.getResourcePath(), errorDetail);
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuleBaseException(e);
		}
		return result;
	}

	public static class NCLocatorForInvoke extends AbstractJavaRule {

		public static final String PRIMARY_LOCATOR_EXPRESSION = "NCLocator.getInstance";

		@Override
		public Object visit(ASTName node, Object data) {
			String nodeImage = node.getImage();
			if (PRIMARY_LOCATOR_EXPRESSION.equalsIgnoreCase(nodeImage)) {
				List<ASTForStatement> forStatements = node.getParentsOfType(ASTForStatement.class);
				List<ASTWhileStatement> whileStatements = node.getParentsOfType(ASTWhileStatement.class);
				List<ASTDoStatement> doStatements = node.getParentsOfType(ASTDoStatement.class);
				if (!forStatements.isEmpty() || !whileStatements.isEmpty() || !doStatements.isEmpty()) {
					RuleContext ruleContext = (RuleContext) data;
					ruleContext.addRuleViolation(ruleContext, this, node,
							String.format("%s 语句不能放在循环之中!", PRIMARY_LOCATOR_EXPRESSION), null);
				}
			}
			return super.visit(node, data);
		}

	}

}
