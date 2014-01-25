package com.yonyou.nc.codevalidator.plugin.domain.mm.code.event;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.MmXmlAnalysisUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMMapUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ��ͷ����β������ı༭ǰ���༩���¼�����ʹ�ñ�������¼��������
 * �ļ�����:1.ͨ��xmlȡ�����еı༭ǰ��༭���¼�������;2,����ÿһ�����Ƿ�̳б������nc
 * .ui.mmf.framework.handler.MMBaseHandler�Լ��Ƿ���nc
 * .ui.mmf.framework.handler.MMEventHandler��ʽ
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY,
		catalog = CatalogEnum.JAVACODE, subCatalog = SubCatalogEnum.JC_CODECRITERION, description = "",
		relatedIssueId = "222", coder = "lijbe", solution = "")
public class TestCase00222 extends AbstractXmlRuleDefinition {

	private String[] eventList = new String[] { "nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent",
			"nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent",
			"nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent",
			"nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent" };

	private IRuleExecuteContext ruleExecuteContext = null;

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
		return xmlResQry;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		this.ruleExecuteContext = ruleExecContext;
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		/*
		 * ͨ��class��nc.ui.pubapp.uif2app.model.AppEventHandlerMediator�ҵ����е��¼���������
		 * �� ��ͨ���¼��������ҵ�������¼�������. Ȼ���ж����Ƿ���ϱ������¼��Ĵ���淶.
		 */

		for (XmlResource xmlResource : resources) {
			MapList<String, String> handlerClazzList = MmXmlAnalysisUtil.getEventHandlerClass(xmlResource,
					this.eventList);
			if (MMMapUtil.isEmpty(handlerClazzList)) {
				continue;
			}
			Collection<List<String>> collection = handlerClazzList.toMap().values();
			List<String> handlerList = new ArrayList<String>();
			Iterator<List<String>> itr = collection.iterator();
			while (itr.hasNext()) {
				handlerList.addAll(itr.next());
			}
			this.checkEventHandler(ruleExecContext, result, handlerList);
		}

		return result;
	}

	private void checkEventHandler(IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result,
			List<String> clazzList) throws RuleBaseException {
		List<JavaClassResource> javResList = this.getJavaResources(ruleExecContext, clazzList);
		for (JavaClassResource javaClassResource : javResList) {
			this.checkHandler(ruleExecContext, result, javaClassResource);
		}
	}

	/**
	 * ����¼���ʼ���࣬�Ƿ�����:public class XXBodyBeforeEditHandler extends MMEventHandler
	 * implements IAppEventHandler<CardBodyBeforeEditEvent> ����ʵ����ʽ
	 * 
	 * @param javaClassResource
	 * @throws RuleBaseException
	 */
	private void checkHandler(IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result,
			JavaClassResource javaClassResource) throws RuleBaseException {
		try {
			EventHandlerVisitorAdapter visitor = new EventHandlerVisitorAdapter();
			CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
			visitor.visit(compilationUnit, null);
			StringBuilder noteBuilder = new StringBuilder();
			if (!visitor.isRight) {
				this.appendMsg(noteBuilder, javaClassResource);
				if (noteBuilder.length() > 0) {
					result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
				}
				return;
			}
			/*
			 * �����������ϱ�׼���Ͳ鿴������¼����� ��
			 */
			if (MMValueCheck.isEmpty(visitor.concreteHandlerList)) {
				return;
			}
			// �������¼��Ĵ�����
			this.checkConcreteHandler(ruleExecContext, result, visitor.concreteHandlerList);

		} catch (ParseException e) {
			Logger.error(e.getMessage(), e);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}

	}

	private void checkConcreteHandler(IRuleExecuteContext ruleExecContext, ResourceRuleExecuteResult result,
			List<String> classList) throws RuleBaseException {
		List<JavaClassResource> javResList = this.getJavaResources(ruleExecContext, classList);
		for (JavaClassResource javaClassResource : javResList) {
			this.checkConcreteHandler(javaClassResource, result);
		}

	}

	private void checkConcreteHandler(JavaClassResource javaClassResource, ResourceRuleExecuteResult result) {
		ConcreteEventHandlerVisitorAdapter visitor = new ConcreteEventHandlerVisitorAdapter();
		visitor.setRuleExecContext(this.ruleExecuteContext);
		try {
			StringBuilder noteBuilder = new StringBuilder();
			CompilationUnit

			compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
			visitor.visit(compilationUnit, null);
			if (!visitor.isRight) {
				noteBuilder.append(String.format("�༭ǰ���¼������ࡾ%s��û��ʹ�ñ�������¼������ܡ�MMBaseHandler��.\n",
						javaClassResource.getJavaCodeClassName()));
				result.addResultElement(javaClassResource.getJavaCodeClassName(), noteBuilder.toString());
			}

		} catch (ParseException e) {
			Logger.error(e.getMessage(), e);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}

	}

	/**
	 * ����className��ѯ��Դ�ļ�
	 * 
	 * @param ruleExecContext
	 * @param filterClazzs
	 * @return
	 * @throws RuleBaseException
	 */
	private List<JavaClassResource> getJavaResources(IRuleExecuteContext ruleExecContext, List<String> filterClazzs)
			throws RuleBaseException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
		javaResourceQuery.setClassNameFilterList(filterClazzs);
		List<JavaClassResource> javaResourceList = new ArrayList<JavaClassResource>();
		javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);

		return javaResourceList;
	}

	private void appendMsg(StringBuilder noteBuilder, JavaClassResource javaClassResource) {
		noteBuilder.append(String.format("�¼�������ʼ���ࡾ%s��û��ʹ�ñ�������¼�������.\n", javaClassResource.getJavaCodeClassName()));
	}

	/**
	 * ɨ���¼������������Ƿ���ȷ���ж�������д������: public class XXBodyBeforeEditHandler extends
	 * MMEventHandler implements IAppEventHandler<CardBodyBeforeEditEvent>,
	 * ���ң�handleAppEvent����Ϊ��
	 * 
	 * @author lijbe
	 * @since V1.0
	 * @version 1.0.0.0
	 */
	private class EventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

		/**
		 * �ж��¼��ļ������Ƿ�����ȷ
		 */
		boolean isRight = false;

		/**
		 * ��ž���Ĵ����¼�
		 */
		List<String> concreteHandlerList = new ArrayList<String>();

		/**
		 * ��ž����¼�������:key:ֻ��������value:����ȫ·��������
		 */
		// Map<String, String> importClassMap = new HashMap<String, String>();
		//
		// /**
		// * �ж�handleAppEvent�����Ƿ�Ϊ��,�Լ�����initMap�еķ���
		// */
		// @Override
		// public void visit(MethodDeclaration n, Void arg) {
		// if (!this.isRight) {
		// return;
		// }
		// if (!"initMap".equals(n.getName())) {
		// return;
		// }
		// BlockStmt bodyStmt = n.getBody();
		// if (bodyStmt == null) {
		// return;
		// }
		// List<Statement> stmts = bodyStmt.getStmts();
		// /*
		// * this.putHandler(BomItemVO.CASSMEASUREID + BomTableCode.BOMITEMS,
		// * BomBodyItemAstUnitHandler.class);
		// */
		// for (Statement stmt : stmts) {
		// String stmtStr = stmt.toString();
		// String tmpClass = stmtStr.substring(stmtStr.indexOf(",") + 1,
		// stmtStr.indexOf(".class")).trim();
		// if (MMValueCheck.isNotEmpty(tmpClass)
		// && MMValueCheck.isNotEmpty(this.importClassMap
		// .get(tmpClass))) {
		// this.concreteHandlerList.add(this.importClassMap
		// .get(tmpClass));
		// }
		// }
		//
		// }

		@Override
		public void visit(ImportDeclaration n, Void arg) {
			String importClazz = n.getName().toString();
			// ���Խ����ø��඼�ӽ�����Ϊ��ҳǩ�Ƚ�����
			if (MMValueCheck.isNotEmpty(importClazz) && MMValueCheck.isNotEmpty(importClazz)) {
				if (importClazz.endsWith("Handler")) {
					this.concreteHandlerList.add(importClazz);
				}
			}
			super.visit(n, arg);
		}

		/**
		 * his����Ҫ�����ڼ̳�����ʹ��ȫ·�������
		 */
		@Override
		public void visit(ClassOrInterfaceDeclaration n, Void arg) {

			String parentClazz = "";
			String implItf = "";
			if (!MMValueCheck.isEmpty(n.getExtends())) {
				parentClazz = n.getExtends().toString();
			}
			if (!MMValueCheck.isEmpty(n.getImplements())) {
				implItf = n.getImplements().toString();
			}
			if (parentClazz.contains("MMEventHandler") && implItf.contains("IAppEventHandler")) {
				this.isRight = true;
			}
			super.visit(n, arg);
		}

	}

	/**
	 * ɨ���¼������������Ƿ���ȷ���ж�������д������:public class BomBodyItemAstUnitHandler extends
	 * MMBaseHandler
	 * 
	 * @author lijbe
	 * @since V1.0
	 * @version 1.0.0.0
	 */
	private class ConcreteEventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

		IRuleExecuteContext ruleExecContext;

		/**
		 * �ж��¼��ļ������Ƿ�����ȷ
		 */
		boolean isRight = false;

		/**
		 * ��ž����¼�������:key:ֻ��������value:����ȫ·��������
		 */
		Map<String, String> parentClassMap = new HashMap<String, String>();

		@Override
		public void visit(ImportDeclaration n, Void arg) {
			this.parentClassMap.put(n.getName().getName(), n.getName().toString());
			super.visit(n, arg);
		}

		/**
		 * his����Ҫ�����ڼ̳�����ʹ��ȫ·�������
		 */
		@Override
		public void visit(ClassOrInterfaceDeclaration n, Void arg) {

			String parentClazz = "";

			if (!MMValueCheck.isEmpty(n.getExtends())) {
				parentClazz = n.getExtends().get(0).toString();
			}
			if (parentClazz.contains("MMBaseHandler")) {
				this.isRight = true;
			} else {
				// ��ȥ��һ���丸��
				try {
					this.isRight = this.checkParentClass(this.parentClassMap.get(parentClazz));
				} catch (RuleBaseException e) {
					Logger.error(e.getMessage());
				}
			}
			super.visit(n, arg);
		}

		/**
		 * �ݹ����丸��
		 * 
		 * @param clzzName
		 * @return
		 * @throws RuleBaseException
		 */
		private boolean checkParentClass(String clzzName) throws RuleBaseException {
			List<String> filterClass = new ArrayList<String>();
			filterClass.add(clzzName);
			List<JavaClassResource> javResList = TestCase00222.this.getJavaResources(this.ruleExecContext, filterClass);
			ConcreteEventHandlerVisitorAdapter visitor = new ConcreteEventHandlerVisitorAdapter();
			visitor.setRuleExecContext(this.ruleExecContext);
			for (JavaClassResource javaClassResource : javResList) {
				CompilationUnit compilationUnit;
				try {
					compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
					visitor.visit(compilationUnit, null);
					if (visitor.isRight) {
						return true;
					}
				} catch (ParseException e) {
					Logger.error(e.getMessage(), e);
				} catch (IOException e) {
					Logger.error(e.getMessage(), e);
				}

			}
			return visitor.isRight;

		}

		public void setRuleExecContext(IRuleExecuteContext ruleExecContext) {
			this.ruleExecContext = ruleExecContext;
		}

	}
}
