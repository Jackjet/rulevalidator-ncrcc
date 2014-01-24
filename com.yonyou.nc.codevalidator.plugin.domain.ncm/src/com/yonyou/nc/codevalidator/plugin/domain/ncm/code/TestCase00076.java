package com.yonyou.nc.codevalidator.plugin.domain.ncm.code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFinallyStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
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

@RuleDefinition(catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION,
		description = "���ڴ�����δ�����ر��α꣬ż�����������ݿ��α곬���������", repairLevel = RepairLevel.MUSTREPAIR, coder = "wumr3",
		relatedIssueId = "76", executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00076 extends AbstractJavaQueryRuleDefinition {

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
			for (JavaClassResource resource : resources) {
				RuleContext ruleContext = SourceCodeProcessor.parseRule(resource.getResourcePath(),
						new PersistCheckRule());
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

	/**
	 * ���������
	 * 
	 * @author wumr3
	 * 
	 */
	public static class PersistCheckRule extends AbstractJavaRule {
		/**
		 * ���������洢�α������List�ͷ������ʵı�־λ
		 */
		private List<String> persistenceNames = new ArrayList<String>();
		private List<String> connectionNames = new ArrayList<String>();
		private boolean findConnectionType = false;
		private boolean findPersistenceType = false;
		private boolean findBlock = false;
		private boolean findFinallyStatement = true;

		/**
		 * �������ʽ���ʱ��List���
		 * 
		 * @param node
		 * @param data
		 * @return
		 */
		@Override
		public Object visit(ASTMethodDeclaration node, Object data) {
			Object result = super.visit(node, data);
			findFinallyStatement = false;
			persistenceNames.clear();
			connectionNames.clear();
			return result;
		}

		@Override
		public Object visit(ASTFinallyStatement node, Object data) {
			if (!(connectionNames.isEmpty() && persistenceNames.isEmpty())) {
				if (node.hasDescendantOfType(ASTBlock.class)) {
					findBlock = true;
					Object result = super.visit(node, data);
					findBlock = false;
					return result;
				}
			}
			return data;
		}

		/**
		 * ���finally���к���.close()��.release()���
		 */
		@Override
		public Object visit(ASTBlock node, Object data) {
			if (findBlock) {// ����finallyStatement��block
				List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
				List<String> closeNames = new ArrayList<String>();
				List<String> persistNames = new ArrayList<String>();
				for (ASTName astName : astNames) {
					String astParamName = astName.getImage();
					if (astParamName.contains(".close")) {
						closeNames.add(astParamName.substring(0, astParamName.indexOf(".")));
					}
					if (astParamName.contains(".release")) {
						persistNames.add(astParamName.substring(0, astParamName.indexOf(".")));
					}
				}
				for (String persistenceName : persistenceNames) {
					if (!persistNames.contains(persistenceName)) {
						RuleContext ruleContext = (RuleContext) data;
						String detailMessage = String.format("finally����%sû�йر�", persistenceName);
						ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
					}
				}
				for (String connectionName : connectionNames) {
					if (!closeNames.contains(connectionName)) {
						RuleContext ruleContext = (RuleContext) data;
						String detailMessage = String.format("finally����%sû�йر�", connectionName);
						ruleContext.addRuleViolation(ruleContext, this, node, detailMessage, null);
					}
				}
				return super.visit(node, data);
			}
			if (!(connectionNames.isEmpty() && persistenceNames.isEmpty()) && findFinallyStatement) {// ����Ƿ���finallyStatement
				if (!node.hasDecendantOfAnyType(ASTFinallyStatement.class)) {
					RuleContext ruleContext = (RuleContext) data;
					ruleContext.addRuleViolation(ruleContext, this, node, "�α�û����finally���йر�", null);
					findFinallyStatement = true;
					return super.visit(node, data);
				}
			}
			return super.visit(node, data);

		}

		/**
		 * �������������Ƿ���Ŀ���������
		 */
		@Override
		public Object visit(ASTLocalVariableDeclaration node, Object data) {
			List<ASTClassOrInterfaceType> classOrInterfaceTypes = node
					.findDescendantsOfType(ASTClassOrInterfaceType.class);
			for (ASTClassOrInterfaceType astClassOrInterfaceType : classOrInterfaceTypes) {
				String typeName = astClassOrInterfaceType.getImage();
				if ("Connection".equals(typeName) || "Statement".equals(typeName) || "ResultSet".equals(typeName)) {
					findConnectionType = true;
					Object result = super.visit(node, data);
					findConnectionType = false;
					return result;
				}
				if ("PersistenceManager".equals(typeName)) {
					findPersistenceType = true;
					Object result = super.visit(node, data);
					findPersistenceType = false;
					return result;
				}
			}
			return data;
		}

		/**
		 * ��ȡĿ����������ƴ洢��List��
		 */
		@Override
		public Object visit(ASTVariableDeclaratorId node, Object data) {
			if (findConnectionType) {
				connectionNames.add(node.getImage());
			}
			if (findPersistenceType) {
				persistenceNames.add(node.getImage());
			}
			return data;
		}
	}

}
