package com.yonyou.nc.codevalidator.plugin.domain.platform.code;

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

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
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
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

/**
 * ���public����ʱʹ��BaseDAO����
 * <p>
 * �����ڿ���������BaseDAO����ʹ��Ĭ��design����Դ����˲���������⣬���������л��������public�еĴ����ڿͻ���ʹ�ã�
 * ���ܲ����Ҳ�������Դ�Ĵ�����ա�
 * 
 * @author mazhqa
 * @reporter �ŷ�
 */
@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "���public����ʱʹ��BaseDAO����",
		memo = "�����ڿ���������BaseDAO����ʹ��Ĭ��design����Դ����˲���������⣬���������л��������public�еĴ����ڿͻ���ʹ�ã�\r\n" + "���ܲ����Ҳ�������Դ�Ĵ�����ա�",
		solution = "ɾ��BaseDAO����", specialParamDefine = { "" }, subCatalog = SubCatalogEnum.JC_CODECRITERION,
		relatedIssueId = "62", coder = "zhangfang", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00062 extends AbstractJavaQueryRuleDefinition {

	private static final String BASEDAO_DEF = "nc.bs.dao.BaseDAO";

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
		for (final JavaClassResource resource : resources) {
			try {
				CompilationUnit compilationUnit = JavaParser.parse(new File(resource.getResourcePath()));
				final StringBuilder noteBuilder = new StringBuilder();
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
							if (stmt.toString().contains("BaseDAO")) {
								noteBuilder.append(String.format("���� %s �а��� %s ����!", n.getName(), BASEDAO_DEF));
							}
						}
					}
				};
				visitor.visit(compilationUnit, null);
				if (noteBuilder.toString().length() > 0) {
					result.addResultElement(resource.getJavaCodeClassName(), noteBuilder.toString());
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
