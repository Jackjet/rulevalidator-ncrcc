package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.IOException;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTCatchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTThrowStatement;
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
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "try catch������catch��һ��Ҫ���쳣���д��� ",
		memo = "��try catch������һ��Ҫ���쳣���д��������Ӧ�����׳��쳣�����ٰ��쳣��¼һ�¡��Ա��������ܹ����١�" + "����Ļ����Ѹ��ٶ�λ���⡣  ", solution = "�����쳣���¼�쳣",
		specialParamDefine = { "" }, coder = "luoxin3", subCatalog = SubCatalogEnum.JC_CODECRITERION,
		relatedIssueId = "338", executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00338 extends AbstractJavaQueryRuleDefinition {

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
						new CatchCheckRule());
				List<RuleViolation> ruleViolationList = ruleContext.getRuleViolationList();
				String errorDetail = SourceCodeProcessor.generateErrorDetail(ruleViolationList);
				if (errorDetail != null && errorDetail.trim().length() > 0) {
					result.addResultElement(resource.getResourcePath(), errorDetail);
				}
			}
			return result;
		} catch (IOException e) {
			throw new ResourceParserException(e);
		}
	}

	public static class CatchCheckRule extends AbstractJavaRule {

		private int catchStatus = 0;

		private static final String LOGGER_PREFIX = "Logger.";

		@Override
		public Object visit(ASTCatchStatement node, Object data) {
			catchStatus++;
			Object result = super.visit(node, data);
			catchStatus--;
			return result;
		}

		@Override
		public Object visit(ASTBlock node, Object data) {
			if (catchStatus > 0) {
				List<ASTThrowStatement> throwStatements = node.findDescendantsOfType(ASTThrowStatement.class);
				List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
				boolean findLoggerStmt = false;
				for (ASTName astName : astNames) {
					if (astName.getImage().startsWith(LOGGER_PREFIX)) {
						findLoggerStmt = true;
						break;
					}
				}
				if (!findLoggerStmt && throwStatements.isEmpty()) {
					RuleContext ruleContext = (RuleContext) data;
					ruleContext.addRuleViolation(ruleContext, this, node, "δ����catch�����׳����쳣����Ҫ���������׳����¼��־!", null);
				}
			}
			return super.visit(node, data);
		}
	}

}
