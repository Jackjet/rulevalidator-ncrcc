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
import java.util.StringTokenizer;

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
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

@RuleDefinition(catalog = CatalogEnum.JAVACODE, coder = "wumr3", description = "���ݶ���ִ�нű���getCodeRemark��������ֵ��ʵ��ִ������һ��",
		relatedIssueId = "369", subCatalog = SubCatalogEnum.JC_CODECRITERION, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00369 extends AbstractJavaQueryRuleDefinition {

	private static final String PARENT_CLASS_NAME = "nc.bs.pub.compiler.AbstractCompiler2";

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
		String projectName = ruleExecContext.getBusinessComponent().getProjectName();
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (JavaClassResource javaClassResource : resources) {
			final StringBuilder noteBuilder = new StringBuilder();
			String className = javaClassResource.getJavaCodeClassName();
			try {
				boolean isCompiled = ClassLoaderUtilsFactory.getClassLoaderUtils().isExtendsParentClass(projectName,
						className, PARENT_CLASS_NAME);

				if (isCompiled) {
					CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
					VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<Void>() {
						String runComStr = null;
						String compileOldStr = null;

						@Override
						public void visit(MethodDeclaration n, Void v) {
							CompileDMO compileDMO = new CompileDMO();
							if ("runComClass".equals(n.getName())) {
								BlockStmt body = n.getBody();
								if (body != null) {
									runComStr = body.toString();
								}
							}
							if ("getCodeRemark".equals(n.getName())) {
								String oldStr = null;
								BlockStmt body = n.getBody();
								if (body == null) {
									return;
								}
								List<Statement> stmts = body.getStmts();
								if (stmts == null) {
									return;
								}
								for (Statement stmt : stmts) {
									if (stmt.toString().contains("return")) {
										String subReturn = stmt.toString().replaceFirst("return", "").trim();
										// TODO: ��鳤��
										if (subReturn.length() > 4) {

											oldStr = subReturn.substring(1, subReturn.length() - 4);// ȥ���ֺţ�˫���ź�/n
										}
										String compileStr = oldStr.replaceAll("//.*?\\\\n", " ");// Ϊreturn���ȥ��ע�͵�����ƥ��
										compileOldStr = compileDMO.insert(compileStr);
									}
								}
							}
							if ((runComStr != null) && (compileOldStr != null)) {
								boolean isCommon = compileDMO.trim(compileOldStr).equals((compileDMO.trim(runComStr)));
								if (!isCommon) {
									noteBuilder.append("getCodeRemark��������ֵ��ʵ�����ݲ�һ��");
								}
							}
						}
					};
					visitor.visit(compilationUnit, null);
					if (noteBuilder.length() > 0) {
						result.addResultElement(javaClassResource.getResourcePath(), noteBuilder.toString());
					}
				}
			} catch (RuleClassLoadException e1) {
				throw new ResourceParserException(e1);
			} catch (ParseException e) {
				throw new ResourceParserException(e);
			} catch (IOException e) {
				throw new ResourceParserException(e);
			}
		}
		return result;
	}

	public static class CompileDMO {
		public String insert(String strScript) {
			StringBuffer fileContent = new StringBuffer();

			// ��ýӿ�ʵ�ֺ���
			fileContent.append(getRawMethod(strScript));
			// ��÷������ݴ���
			fileContent.append(translate(strScript));
			// �쳣����
			fileContent.append(getRawException());
			// ��÷�������
			fileContent.append(getRawMethodEnd());
			return fileContent.toString();

		}

		protected String getRawMethod(String strScript) {
			return rawMethod(strScript);
		}

		private String rawMethod(String strScript) {

			StringBuffer method = new StringBuffer();

			// //////////////////////////////////////////////////
			// �����Ǳ������ķ������ݣ�
			// //////////////////////////////////////////////////
			/*
			 * ��ע��ƽ̨��дԭʼ�ű� �ӿ�ִ����
			 */

			// public Object runComClass(PfParameterVO vo) throws
			// java.rmi.RemoteException {
			method.append("{" + "\r\n");
			method.append("try{" + "\r\n");
			method.append("	super.m_tmpVo=vo;" + "\r\n");
			return method.toString();
		}

		protected String getRawException() {
			StringBuffer exEnd = new StringBuffer();
			// WARN::����ҵ���쳣��ֱ���׳���
			// exEnd.append("} catch (BusinessException ex) {" + "\r\n");
			// exEnd.append("		throw ex;" + "\r\n");
			exEnd.append("} catch (Exception ex) {" + "\r\n");
			exEnd.append("	if (ex instanceof BusinessException)" + "\r\n");
			exEnd.append("		throw (BusinessException) ex;" + "\r\n");
			exEnd.append("	else " + "\r\n");
			exEnd.append("    throw new PFBusinessException(ex.getMessage(), ex);" + "\r\n");
			exEnd.append("}" + "\r\n");
			return exEnd.toString();
		}

		protected String getRawMethodEnd() {
			StringBuffer methodEnd = new StringBuffer("}" + "\r\n");
			return methodEnd.toString();
		}

		public String translate(String oldStr) {
			String s = oldStr.replaceAll("\\\\n", "\n");
			return s;
		}

		public String[] getInfoFromStr(String strInfo) {
			String strInner = strInfo.trim();
			String[] strRet = new String[3];
			int preIndex = 0, nxtIndex = 0;
			for (int i = 0; i < 2; i++) {
				nxtIndex = strInner.indexOf(",", preIndex);
				strRet[i] = strInner.substring(preIndex, nxtIndex);
				preIndex = nxtIndex + 1;
			}
			strRet[2] = strInner.substring(preIndex);
			return strRet;
		}

		public String trim(String oldStr) {
			StringBuffer buffer = new StringBuffer();
			StringTokenizer stringTokenizer = new StringTokenizer(oldStr);
			while (stringTokenizer.hasMoreTokens()) {
				buffer.append(stringTokenizer.nextToken());
			}
			return buffer.toString();
		}

	}

}
