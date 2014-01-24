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
 * 表头、表尾、表体的编辑前及编缉后事件必须使用本领域的事件处理机制
 * 文件解析:1.通过xml取出所有的编辑前与编辑后事件处理类;2,分析每一个类是否继承本领域的nc
 * .ui.mmf.framework.handler.MMBaseHandler以及是否是nc
 * .ui.mmf.framework.handler.MMEventHandler方式
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
		 * 通过class：nc.ui.pubapp.uif2app.model.AppEventHandlerMediator找到所有的事件处理处理类
		 * 。 再通过事件处理类找到具体的事件处理类. 然后判断其是否符合本领域事件的处理规范.
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
	 * 检查事件初始化类，是否满足:public class XXBodyBeforeEditHandler extends MMEventHandler
	 * implements IAppEventHandler<CardBodyBeforeEditEvent> 这种实现形式
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
			 * 如果监听类符合标准，就查看具体的事件处理 类
			 */
			if (MMValueCheck.isEmpty(visitor.concreteHandlerList)) {
				return;
			}
			// 检查具体事件的处理类
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
				noteBuilder.append(String.format("编辑前后事件处理类【%s】没有使用本领域的事件处理框架【MMBaseHandler】.\n",
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
	 * 根据className查询资源文件
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
		noteBuilder.append(String.format("事件监听初始化类【%s】没有使用本领域的事件处理框架.\n", javaClassResource.getJavaCodeClassName()));
	}

	/**
	 * 扫描事件监听处理类是否正确，判断依据是写法如下: public class XXBodyBeforeEditHandler extends
	 * MMEventHandler implements IAppEventHandler<CardBodyBeforeEditEvent>,
	 * 并且：handleAppEvent不能为空
	 * 
	 * @author lijbe
	 * @since V1.0
	 * @version 1.0.0.0
	 */
	private class EventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

		/**
		 * 判断事件的监听类是否是正确
		 */
		boolean isRight = false;

		/**
		 * 存放具体的处理事件
		 */
		List<String> concreteHandlerList = new ArrayList<String>();

		/**
		 * 存放具体事件处理类:key:只是类名，value:包含全路径的类名
		 */
		// Map<String, String> importClassMap = new HashMap<String, String>();
		//
		// /**
		// * 判断handleAppEvent方法是否为空,以及加载initMap中的方法
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
			// 可以将引用个类都加进来因为多页签比较特殊
			if (MMValueCheck.isNotEmpty(importClazz) && MMValueCheck.isNotEmpty(importClazz)) {
				if (importClazz.endsWith("Handler")) {
					this.concreteHandlerList.add(importClazz);
				}
			}
			super.visit(n, arg);
		}

		/**
		 * his否还需要考虑在继承类中使用全路径的情况
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
	 * 扫描事件监听处理类是否正确，判断依据是写法如下:public class BomBodyItemAstUnitHandler extends
	 * MMBaseHandler
	 * 
	 * @author lijbe
	 * @since V1.0
	 * @version 1.0.0.0
	 */
	private class ConcreteEventHandlerVisitorAdapter extends VoidVisitorAdapter<Void> {

		IRuleExecuteContext ruleExecContext;

		/**
		 * 判断事件的监听类是否是正确
		 */
		boolean isRight = false;

		/**
		 * 存放具体事件处理类:key:只是类名，value:包含全路径的类名
		 */
		Map<String, String> parentClassMap = new HashMap<String, String>();

		@Override
		public void visit(ImportDeclaration n, Void arg) {
			this.parentClassMap.put(n.getName().getName(), n.getName().toString());
			super.visit(n, arg);
		}

		/**
		 * his否还需要考虑在继承类中使用全路径的情况
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
				// 再去找一遍其父类
				try {
					this.isRight = this.checkParentClass(this.parentClassMap.get(parentClazz));
				} catch (RuleBaseException e) {
					Logger.error(e.getMessage());
				}
			}
			super.visit(n, arg);
		}

		/**
		 * 递归检查其父类
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
