package com.yonyou.nc.codevalidator.plugin.domain.dmm.md;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.MetadataResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.bpf.IBusiOperator;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.MetaResType;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;

@RuleDefinition(catalog = CatalogEnum.METADATA, relatedIssueId = "764", coder = "guojunf", description = "建立业务操作的名称要与当前业务操作按钮的actionCode一致", subCatalog = SubCatalogEnum.MD_BASESETTING, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00764 extends AbstractXmlRuleDefinition {

	private List<IBusiOperator> busiOperators;

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes,
				ruleExecContext.getBusinessComponent());
		return xmlResQry;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(
			IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		Set<String> actionCodes = new HashSet<String>();

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		if (resources == null || resources.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "请检查输入的功能编码是否正确！\n");
			return result;
		}

		for (XmlResource xmlResource : resources) {
			List<Element> element = xmlResource
					.getAllBeanClass("nc.ui.uif2.NCAction");
			for (Element ele : element) {
				Class<?> loadClass = ClassLoaderUtilsFactory
						.getClassLoaderUtils().loadClass(
								ruleExecContext.getBusinessComponent()
										.getProjectName(),
								ele.getAttribute("class"));
				Object newInstance;
				try {
					newInstance = loadClass.newInstance();
				} catch (InstantiationException e) {
					throw new RuleBaseException(e);
				} catch (IllegalAccessException e) {
					throw new RuleBaseException(e);
				}
				Method method;
				try {
					method = loadClass.getMethod("getValue", String.class);
				} catch (SecurityException e) {
					throw new RuleBaseException(e);
				} catch (NoSuchMethodException e) {
					throw new RuleBaseException(e);
				}
				Object value;
				try {
					value = method.invoke(newInstance, "Code");
				} catch (IllegalArgumentException e) {
					throw new RuleBaseException(e);
				} catch (IllegalAccessException e) {
					throw new RuleBaseException(e);
				} catch (InvocationTargetException e) {
					throw new RuleBaseException(e);
				}
				if (value != null) {
					actionCodes.add(value.toString());
				}

			}
		}

		List<String> bpfOpNames = this.getOpNames(ruleExecContext);
		if (bpfOpNames != null) {
			for (String twoPart : bpfOpNames) {
				String file = twoPart.split("=>")[0];
				String op = twoPart.split("=>")[1];
				if (!actionCodes.contains(op)) {
					result.addResultElement(ruleExecContext
							.getBusinessComponent().getDisplayBusiCompName(),
							file + "文件中定义的操作" + op + "与按钮的actionCode不一致！\n");
				}
			}

		}

		return result;
	}

	private List<String> getOpNames(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		MetadataResourceQuery metadataResourceQuery = new MetadataResourceQuery();
		metadataResourceQuery.setBusinessComponent(ruleExecContext
				.getBusinessComponent());
		metadataResourceQuery.setMetaResType(MetaResType.BPF);
		List<String> opList = new ArrayList<String>();
		List<MetadataResource> resources = ResourceManagerFacade
				.getResource(metadataResourceQuery);
		for (MetadataResource resource : resources) {
			this.busiOperators = resource.getMetaProcessFile()
					.getBusiOperators();
			for (IBusiOperator operator : busiOperators) {
				opList.add(resource.getResourceFileName() + "=>"
						+ operator.getName());
			}
		}

		return opList;
	}
}
