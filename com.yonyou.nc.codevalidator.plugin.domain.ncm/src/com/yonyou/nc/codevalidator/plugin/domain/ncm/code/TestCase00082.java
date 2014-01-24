package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "检测将dr字段作为业务字段使用",
		subCatalog = SubCatalogEnum.JC_CODECRITERION, solution = "不使用dr字段，改用其他方式", coder = "wumr3",
		relatedIssueId = "82", executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00082 extends AbstractJavaQueryRuleDefinition {
	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws ResourceParserException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return javaResourceQuery;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws ResourceParserException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (JavaClassResource javaClassResource : resources) {
			try {
				final StringBuilder noteBuilder = new StringBuilder();
				CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
				VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<Void>() {

					@Override
					public void visit(MethodDeclaration n, Void v) {
						BlockStmt body = n.getBody();
						if (body == null) {
							return;
						}
						List<Statement> stmts = body.getStmts();
						if (stmts == null) {
							return;
						}
						for (Statement stmt : stmts) {
							String stmtStr = stmt.toString();
							if (stmtStr.contains(".setDr(")) {
								int beginIndex = stmtStr.indexOf(".setDr(") + 7;
								String subOne = stmtStr.substring(beginIndex);
								int endIndex = subOne.lastIndexOf(")") - 1;
								String setDrParam = subOne.substring(0, endIndex);
								if (!(setDrParam.trim().equals("0") || setDrParam.trim().equals("1"))) {
									noteBuilder.append(stmt.getBeginLine() + "行dr字段的值设置不正确\n");
								}
							}
							String updateDr = "update\\s+.+\\b.+set\\s+dr.*=";
							Pattern p = Pattern.compile(updateDr);
							Matcher m = p.matcher(stmt.toString());
							if (m.find()) {
								int endIndex = m.end();
								String drValue = stmtStr.substring(endIndex).trim().substring(0, 1);
								if (!(drValue.equals("0") || drValue.equals("1"))) {
									noteBuilder.append(stmt.getBeginLine() + "行dr字段值不正确不能作为业务组件\n");
								}
							}
						}
					}
				};
				visitor.visit(compilationUnit, null);
				if (noteBuilder.toString().length() > 0) {
					result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
				}
			} catch (ParseException e) {
				throw new ResourceParserException(e);
			} catch (IOException e) {
				throw new ResourceParserException(e);
			}
		}
		return result;
	}

}
