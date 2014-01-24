package com.yonyou.nc.codevalidator.plugin.domain.dmm.uif;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

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

@RuleDefinition(catalog = CatalogEnum.UIFACTORY, coder = "guojunf", description = "权限控制的按钮中注入编码参数，resourceCode必须注入 ", relatedIssueId = "765", subCatalog = SubCatalogEnum.UF_CONFIGFILE, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00765 extends AbstractXmlRuleDefinition {

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

		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		if (resources == null || resources.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "请检查输入的功能编码是否正确！\n");
			return result;
		}

		List<String> opList = this.getOpNames(ruleExecContext);

		for (XmlResource xmlResource : resources) {

			Map<String, List<String>> actionCodeMap = new HashMap<String, List<String>>();
			List<Element> element = xmlResource
					.getAllBeanClass("nc.ui.uif2.NCAction");
			for (Element ele : element) {
				Class<?> loadClass = ClassLoaderUtilsFactory
						.getClassLoaderUtils().loadClass(
								ruleExecContext.getBusinessComponent()
										.getProjectName(),
								ele.getAttribute("class"));
				Object value = null;
				Object newInstance = null;
				try {
					newInstance = loadClass.newInstance();
				} catch (InstantiationException e) {
					throw new RuleBaseException(e);
				} catch (IllegalAccessException e) {
					throw new RuleBaseException(e);
				}
				Method method = null;
				try {
					method = loadClass.getMethod("getValue", String.class);
				} catch (SecurityException e) {
					throw new RuleBaseException(e);
				} catch (NoSuchMethodException e) {
					throw new RuleBaseException(e);
				}
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
					if (!actionCodeMap.containsKey(value)) {
						actionCodeMap.put(value.toString(),
								new ArrayList<String>());
					}
					actionCodeMap.get(value).add(ele.getAttribute("id"));
				}
			}

			if (actionCodeMap.isEmpty()) {
				continue;
			}

			// 循环每一个业务操作，获取对应的bean,查看是否有resourceCode
			for (String op : opList) {
				List<String> beanIDList = actionCodeMap.get(op.split("=>")[1]);
				if (beanIDList != null && !beanIDList.isEmpty()) {
					for (String beanID : beanIDList) {
						Element beanEle = xmlResource.getElementById(beanID);
						boolean isSuccess = false;
						NodeList nodeList = beanEle
								.getElementsByTagName("property");
						for (int i = 0; i < nodeList.getLength(); i++) {
							NamedNodeMap nameNodeMap = nodeList.item(i)
									.getAttributes();
							String name = nameNodeMap.getNamedItem("name")
									.getNodeValue();

							if (name.equals("resourceCode")) {
								isSuccess = true;
								break;
							}
						}

						if (!isSuccess) {
							result.addResultElement(
									xmlResource.getResourceFileName(),
									"权限控制的按钮中没有注入resourceCode beanID:" + beanID);
						}
					}
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
			busiOperators = resource.getMetaProcessFile().getBusiOperators();
			for (IBusiOperator operator : busiOperators) {
				opList.add(resource.getResourceFileName() + "=>"
						+ operator.getName());
			}
		}
		return opList;
	}

}
