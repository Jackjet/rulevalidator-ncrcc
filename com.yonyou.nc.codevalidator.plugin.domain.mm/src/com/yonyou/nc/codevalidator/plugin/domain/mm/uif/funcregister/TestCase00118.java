package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.funcregister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.MmUIFactoryConstants;
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

/**
 * 功能节点注册中，功能类名必须为nc.ui.pubapp.uif2app.ToftPanelAdaptorEx
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE,
		description = "功能节点注册中，功能类名必须为nc.ui.pubapp.uif2app.ToftPanelAdaptorEx",
		solution = "功能节点注册中，功能类名必须为nc.ui.pubapp.uif2app.ToftPanelAdaptorEx ", coder = "lijbe", relatedIssueId = "118")
public class TestCase00118 extends AbstractXmlRuleDefinition {
	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {

		XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
		return xmlResQry;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();

		List<String> funcodes = new ArrayList<String>();
		for (XmlResource xmlResource : resources) {
			funcodes.add(xmlResource.getFuncNodeCode());
		}
		Map<String, String> funNameMap = new HashMap<String, String>();
		if (funcodes.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
					"请检查输入的功能编码是否正确! \n");
		}
		Map<String, String> funClassMap = this.queryFunClassByFuncode(funcodes.toArray(new String[funcodes.size()]),
				ruleExecContext, funNameMap);
		if (null == funClassMap || funClassMap.size() < 1) {
			result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
					"请检查输入的功能编码是否正确! \n");
		}

		List<String> funNames = new ArrayList<String>();
		for (String key : funClassMap.keySet()) {
			if (!MmUIFactoryConstants.FUN_CLASS_NAME.equals(funClassMap.get(key))) {
				funNames.add(funNameMap.get(key));
			}
		}

		if (funNames.size() > 0) {
			result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
					String.format("功能节点:%s 的功能类名不是" + MmUIFactoryConstants.FUN_CLASS_NAME + "\n", funNames));
		}

		return result;
	}

	/**
	 * 取得功能节点中功能编码对应的功能类名
	 * 
	 * @param funcodes
	 * @param ruleExecContext
	 * @param funNameMap
	 * @return
	 * @throws RuleBaseException
	 */
	private Map<String, String> queryFunClassByFuncode(String[] funcodes, IRuleExecuteContext ruleExecContext,
			Map<String, String> funNameMap) throws RuleBaseException {

		Map<String, String> funClassMap = new HashMap<String, String>();

		String sql = "select funcode,fun_name,class_name from sm_funcregister where funcode in(";

		String whereValue = "";

		for (int i = 0; i < funcodes.length; i++) {

			if (i == funcodes.length - 1) {
				whereValue += "'" + funcodes[i] + "'";
			} else {
				whereValue += "'" + funcodes[i] + "',";
			}
		}
		whereValue += ") and dr = 0";
		sql += whereValue;

		DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());

		if (dataSet.isEmpty()) {
			return funClassMap;
		}

		List<DataRow> dataRows = dataSet.getRows();

		for (DataRow dataRow : dataRows) {
			funClassMap.put((String) dataRow.getValue("funcode"), (String) dataRow.getValue("class_name"));
			funNameMap.put((String) dataRow.getValue("funcode"), (String) dataRow.getValue("fun_name"));
		}

		return funClassMap;

	}

}
