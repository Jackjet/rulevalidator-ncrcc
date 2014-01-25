package com.yonyou.nc.codevalidator.plugin.domain.mm.code.query;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

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
 * 如果供应商作为查询条件，则供应商ID、供应商编码、供应商名称需要进行参照过滤
 * 
 * @author qiaoyanga
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(
		executePeriod = ExecutePeriod.DEPLOY,
		catalog = CatalogEnum.JAVACODE,
		subCatalog = SubCatalogEnum.JC_CODEDESIGN,
		description = "如果供应商作为查询条件，则供应商ID、供应商编码、供应商名称需要进行参照过滤 ",
		relatedIssueId = "238",
		coder = "qiaoyanga",
		solution = "配置节点号参数，将通过节点号找到对应的配置文件，通过配置文件找到查询初试化类，判断类中是否有供应商参照过滤类‘nc.ui.mmf.busi.query.reffilter.QSupplierFilter’")
public class TestCase00238 extends AbstractXmlRuleDefinition {
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
			Element queryAction = xmlResource.getElementById("queryAction");
			Element qryCondDLGInitializer = xmlResource.getChildPropertyElement(queryAction, "qryCondDLGInitializer");
			if (qryCondDLGInitializer == null) {
				result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
				continue;
			}
			String ref = qryCondDLGInitializer.getAttribute("ref");
			if (ref == null) {
				result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
				continue;
			}
			Element qryCondDLGInitializerBean = xmlResource.getElementById(ref);
			if (qryCondDLGInitializerBean == null) {
				result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
				continue;
			}
			String qryCondDLGInitializerBeanName = qryCondDLGInitializerBean.getAttribute("class");
			JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
			javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
			javaResourceQuery.setResPrivilege(JavaResPrivilege.CLIENT);
			List<String> className = new ArrayList<String>();
			className.add(qryCondDLGInitializerBeanName);
			javaResourceQuery.setClassNameFilterList(className);
			List<JavaClassResource> javaResourceList = ResourceManagerFacade.getResource(javaResourceQuery);
			if (javaResourceList == null || javaResourceList.size() <= 0) {
				result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
				continue;
			}
			JavaClassResource javaClassResource = javaResourceList.get(0);
			if (javaClassResource == null) {
				result.addResultElement(xmlResource.getResourcePath(), "未找到正确的查询初始化类\n");
				continue;
			}
			try {
				CompilationUnit compilationUnit = JavaParser.parse(new File(javaClassResource.getResourcePath()));
				QryCondDLGInitializer visitor = new QryCondDLGInitializer();
				visitor.visit(compilationUnit, null);
				// 是否引用了懒加载类
				boolean isImport = visitor.isImport();
				if (!isImport) {
					result.addResultElement(javaClassResource.getJavaCodeClassName(), "查询初试化类中供应商需要进行参照过滤 \n");
					continue;
				}
			} catch (ParseException e) {
				Logger.error(e.getMessage(), e);
			} catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

	/**
	 * 扫描引用类
	 * 
	 * @author qiaoyanga
	 * @since V1.0
	 * @version 1.0.0.0
	 */
	private static class QryCondDLGInitializer extends VoidVisitorAdapter<Void> {
		boolean isImport = false;

		@Override
		public void visit(ImportDeclaration n, Void arg) {
			// 已经找到就不需要再往下执行
			if (this.isImport) {
				return;
			}
			String importClazz = n.getName().toString();
			if ("nc.ui.mmf.busi.query.reffilter.QSupplierFilter".equals(importClazz)) {
				this.isImport = true;
				return;
			}
		}

		public boolean isImport() {
			return this.isImport;
		}
	}

}
