package com.yonyou.nc.codevalidator.plugin.domain.dmm.uif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.UIFACTORY, coder = "muxh", description = "配置文件templateContainer中nodekey与功能节点默认模板设置一致检测规则 ", relatedIssueId = "808", subCatalog = SubCatalogEnum.UF_CONFIGFILE)
public class TestCase00808 extends AbstractXmlRuleDefinition {

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
		// key:功能节点号；value:nodekeys
		Map<String, List<String>> queryNodeKeys = new HashMap<String, List<String>>();
		Map<String, List<String>> billNodeKeys = new HashMap<String, List<String>>();
		// 根据xml文件，得到nokeKey配置信息。
		this.getXmlInfo(queryNodeKeys, billNodeKeys, resources);

//		ScriptResourceQuery query = new ScriptResourceQuery(querySql);
//		query.setRuntimeContext(ruleExecContext.getRuntimeContext());
//
//		ScriptDataSetResource dataSet = ResourceManagerFacade
//				.getResourceAsDataSet(query);
		String querySql = this.getQuerySys_base(resources);
		DataSet set = SQLQueryExecuteUtils.executeQuery(querySql, ruleExecContext.getRuntimeContext());
//		DataSet set = dataSet.getDataSet();
		result = this.BuilderError(queryNodeKeys, billNodeKeys, set);
		return result;
	}

	// 这个有待抽象化，暂时先这么写
	private ResourceRuleExecuteResult BuilderError(
			Map<String, List<String>> queryNodeKeys,
			Map<String, List<String>> billNodeKeys, DataSet set) {
		Map<String, DataRow> hashset = new HashMap<String, DataRow>();
		if (!set.isEmpty()) {
			Iterator<DataRow> rows = set.getRows().iterator();
			while (rows.hasNext()) {
				DataRow rowdata = rows.next();
				StringBuilder rowkey = new StringBuilder();

				String funnode = (String) rowdata.getValue("funnode");
				String nodekey = (String) rowdata.getValue("nodekey");
				String tempstyle = rowdata.getValue("tempstyle").toString();
				rowkey.append(funnode).append(nodekey).append(tempstyle);
				hashset.put(rowkey.toString(), rowdata);
			}
		}

		ResourceRuleExecuteResult resultMessage = new ResourceRuleExecuteResult();
		StringBuilder totalErrorMsg = new StringBuilder();
		for (Entry<String, List<String>> entry : billNodeKeys.entrySet()) {
			String funnode = entry.getKey();
			List<String> nodekeys = entry.getValue();
			for (String nodekey : nodekeys) {
				String key = funnode + nodekey + 0;
				if (!hashset.containsKey(key)) {
					String errorMsg = "功能节点号[" + funnode + "] 单据模版nodeKey["
							+ nodekey + "]与预制信息不符\r\n";
					totalErrorMsg.append(errorMsg);
				}
			}
		}
		for (Entry<String, List<String>> entry : queryNodeKeys.entrySet()) {
			String funnode = entry.getKey();
			List<String> nodekeys = entry.getValue();
			for (String nodekey : nodekeys) {
				String key = funnode + nodekey + 1;
				if (!hashset.containsKey(key)) {
					String errorMsg = "功能节点号[" + funnode + "] 查询模版nodeKey["
							+ nodekey + "]与预制信息不符\r\n";
					totalErrorMsg.append(errorMsg);
				}
			}
		}
		if (totalErrorMsg.length() > 0) {
			resultMessage.addResultElement("NCRCC-00808",
					totalErrorMsg.toString());
		}
		return resultMessage;
	}

	private String getQuerySys_base(List<XmlResource> resources) {
		// Set<String>funnodes=new HashSet<String>();
		StringBuilder nodes = new StringBuilder();
		Iterator<XmlResource> iterator = resources.iterator();
		if (iterator.hasNext()) {
			String funccode = iterator.next().getFuncNodeCode();
			// funnodes.add(funcnode);
			nodes.append("'" + funccode + "',");
		}
		String inNodes = (String) nodes.subSequence(0, nodes.length() - 1);
		String sql = "select funnode,nodekey ,tempstyle from pub_systemplate_base where dr=0 and layer=0 and funnode in("
				+ inNodes + ")";
		return sql;
	}

	private void getXmlInfo(Map<String, List<String>> queryNodeKeys,
			Map<String, List<String>> billNodeKeys, List<XmlResource> resources) {
		for (XmlResource resource : resources) {
			// 查询模版container
			List<Element> queryTemplate = resource
					.getBeanElementByClass("nc.ui.uif2.editor.QueryTemplateContainer");
			// 单据模版container
			List<Element> billTemplate = resource
					.getBeanElementByClass("nc.ui.uif2.editor.TemplateContainer");

			if (queryTemplate != null) {
				for (Element qt : queryTemplate) {
					Element property_nodekeys = resource
							.getChildPropertyElement(qt, "nodeKey");
					if (property_nodekeys == null) {
						continue;
					}
					String value = property_nodekeys.getAttribute("value");
					if (value.trim().length() > 0) {
						this.putValue(resource.getFuncNodeCode(), value,
								queryNodeKeys);
					}

				}

			}
			if (billTemplate != null) {
				for (Element bt : billTemplate) {
					Element property_nodekeys = resource
							.getChildPropertyElement(bt, "nodeKeies");
					if (property_nodekeys == null) {
						continue;
					}
					List<Element> list = resource.getChildElementsByTagName(
							property_nodekeys, "list");
					if (list == null) {
						continue;
					}
					List<Element> values = resource.getChildElementsByTagName(
							list.get(0), "value");
					if (values == null) {
						continue;
					}
					for (Element value : values) {
						String valueStr = value.getFirstChild().getNodeValue();
						if (valueStr.trim().length() > 0) {
							this.putValue(resource.getFuncNodeCode(),
									valueStr.trim(), billNodeKeys);
						}
					}
				}
			}
		}

	}

	private void putValue(String key, String value,
			Map<String, List<String>> map) {
		if (map.get(key) == null) {
			List<String> list = new ArrayList<String>();
			map.put(key, list);
		}
		map.get(key).add(value);
	}

//	class SystemplateBaseVO {
//		private String funnode;
//		private String nodekey;
//		private String tempstyle;
//	}
}
