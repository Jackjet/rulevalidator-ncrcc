package com.yonyou.nc.codevalidator.plugin.domain.platform.code;

import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
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
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, executePeriod = ExecutePeriod.CHECKOUT,
		executeLayer = ExecuteLayer.BUSICOMP, description = "select语句中应避免使用select * from tablename",
		relatedIssueId = "506", subCatalog = SubCatalogEnum.JC_PERFORMISSUES, repairLevel = RepairLevel.SUGGESTREPAIR,
		coder = "mazhqa")
public class TestCase00506 extends AbstractJavaQueryRuleDefinition {

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
			for (JavaClassResource javaClassResource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(javaClassResource.getResourcePath(),
						new SelectStarCheck());
				String errorDetail = SourceCodeProcessor.generateErrorDetail(ruleContext.getRuleViolationList());
				if (StringUtils.isNotBlank(errorDetail)) {
					result.addResultElement(javaClassResource.getResourcePath(), errorDetail);
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuleBaseException(e);
		}
		return result;
	}

	public static class SelectStarCheck extends AbstractJavaRule {

		public static final String SELECT_SENTENCE = "select *";

		@Override
		public Object visit(ASTLiteral node, Object data) {
			String nodeImage = node.getImage();
			if (StringUtils.isNotBlank(nodeImage) && nodeImage.trim().toLowerCase().contains(SELECT_SENTENCE)) {
				RuleContext ruleContext = (RuleContext) data;
				ruleContext.addRuleViolation(ruleContext, this, node, "代码中的select语句最好不要使用*来获取所有字段!", null);
			}
			return super.visit(node, data);
		}

	}

}
