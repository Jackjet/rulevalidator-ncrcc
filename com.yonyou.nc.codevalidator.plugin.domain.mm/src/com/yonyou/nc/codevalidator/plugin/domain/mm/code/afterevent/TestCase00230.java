package com.yonyou.nc.codevalidator.plugin.domain.mm.code.afterevent;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.MmXmlAnalysisUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMMapUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * �����ı༩���¼��б��봦��������������
 * 
 * @author qiaoyanga
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(
		executePeriod = ExecutePeriod.DEPLOY,
		catalog = CatalogEnum.JAVACODE,
		subCatalog = SubCatalogEnum.JC_CODECRITERION,
		description = "�����ı༩���¼��б��봦�������������� ",
		relatedIssueId = "230",
		coder = "qiaoyanga",
		solution = "���ù��ܽڵ�Ų�������ͨ���ڵ���ҵ���Ӧ�������ļ���ͨ�������ļ��ҵ���ͷ����༭���¼��࣬"
				+ "�ж�����������'NastNumHandler'��β�Ļ������¼������ࡣ�ٷ������廻�������к��¼��Ƿ������á�nc.ui.mmf.busi.measure.CalculatorUtil�����������������")
public class TestCase00230 extends AbstractXmlRuleDefinition {
	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		return new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
	}

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (XmlResource xmlResource : resources) {

			MapList<String, String> handlerClazzList = MmXmlAnalysisUtil.getEventHandlerClass(xmlResource,
					new String[] { "nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent" });
			if (MMMapUtil.isEmpty(handlerClazzList)) {
				result.addResultElement(xmlResource.getResourcePath(), "δ�ҵ���ͷ����༭���¼�\n");
				continue;
			}
			List<String> classNames = handlerClazzList
					.get("nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent");
			for (String className : classNames) {
				try {
					JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
					javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
					javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
					List<String> cNames = new ArrayList<String>();
					cNames.add(className);
					javaResourceQuery.setClassNameFilterList(cNames);
					List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);
					if (javaResourceList == null || javaResourceList.size() <= 0) {
						result.addResultElement(xmlResource.getResourcePath(), "δ�ҵ���ͷ����༭���¼���\n");
						continue;
					}
					JavaClassResource javaClassResource = javaResourceList.get(0);

					CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
					Visitor visitor = new Visitor();
					visitor.visit(compilationUnit, null);
					if (visitor.getEventname() == null) {
						result.addResultElement(xmlResource.getResourcePath(), className
								+ "��δ�ҵ��ԡ�NastNumHandler����β�������༭�¼�\n");
						continue;
					}
					JavaResourceQuery javaQuery = new JavaResourceQuery();
					javaQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
					javaQuery.setResPrivilege(JavaResPrivilege.CLIENT);
					List<String> Names = new ArrayList<String>();
					Names.add(visitor.getEventname());
					javaQuery.setClassNameFilterList(Names);
					List<JavaClassResource> javaList = ResourceManagerFacade.getResource(javaQuery);
					if (javaList == null || javaList.size() <= 0) {
						result.addResultElement(javaClassResource.getResourcePath(), "��δ�ҵ��ԡ�NastNumHandler����β�������༭�¼�\n");
						continue;
					}
					JavaClassResource javaClass = javaList.get(0);

					CompilationUnit compUnit = JavaParser.parse(new File(javaClass.getResourcePath()));
					EventVisitor eventVisitor = new EventVisitor();
					eventVisitor.visit(compUnit, null);
					if (!eventVisitor.isImport()) {
						result.addResultElement(javaClass.getResourcePath(), "�����ı༩���¼���δ����������������\n");
						continue;
					}
				} catch (ParseException e) {
					Logger.error(e.getMessage(), e);
				} catch (IOException e) {
					Logger.error(e.getMessage(), e);
				}
			}
		}
		return result;
	}

	/**
	 * ɨ��������
	 * 
	 * @author qiaoyanga
	 * @since V1.0
	 * @version 1.0.0.0
	 */
	private static class Visitor extends VoidVisitorAdapter<Void> {
		String eventname = null;

		@Override
		public void visit(ImportDeclaration n, Void arg) {
			String importClazz = n.getName().toString();
			if (importClazz.contains("NastNumHandler")) {
				this.eventname = importClazz;
			}
		}

		public String getEventname() {
			return this.eventname;
		}
	}

	/**
	 * ɨ��������
	 * 
	 * @author qiaoyanga
	 * @since V1.0
	 * @version 1.0.0.0
	 */
	private static class EventVisitor extends VoidVisitorAdapter<Void> {
		boolean isImport = false;

		@Override
		public void visit(ImportDeclaration n, Void arg) {
			// �Ѿ��ҵ��Ͳ���Ҫ������ִ��
			if (this.isImport) {
				return;
			}
			String importClazz = n.getName().toString();
			if ("nc.ui.mmf.busi.measure.CalculatorUtil".equals(importClazz)) {
				this.isImport = true;
				return;
			}
		}

		public boolean isImport() {
			return this.isImport;
		}
	}
}
