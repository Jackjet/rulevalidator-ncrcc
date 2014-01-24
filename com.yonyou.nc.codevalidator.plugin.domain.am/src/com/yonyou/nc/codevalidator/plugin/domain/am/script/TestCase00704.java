package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "检查单据是否配置了默认的编码规则", relatedIssueId = "704", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch")
public class TestCase00704 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		// TODO: 从模块入手，去nchome查找billcodepredata.xml;

		// 获取导出xml中所有的元素
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String projectPath = ruleExecContext.getBusinessComponent()
				.getProjectPath();
		String projectName = ruleExecContext.getBusinessComponent()
				.getProjectName();
		File file = new File(projectPath + "/" + projectName
				+ "/pub/config/billcodepredata/billcodepredata.xml");
		// 存放XML中的所有单据类型
		List<String> listXMLBillCode = new ArrayList<String>();
		// xml是否存在
		boolean judge_isXMLExist = true;
		try {
			Document document = factory.newDocumentBuilder().parse(file);
			NodeList billCodeList = document.getElementsByTagName("nbcrcode");
			for (int i = 0; i < billCodeList.getLength(); i++) {
				Element element = (Element) billCodeList.item(i);
				listXMLBillCode.add(element.getTextContent());
			}
		} catch (SAXException e) {
			judge_isXMLExist = false;
			Logger.error(e.getMessage(), e);
		} catch (IOException e) {
			judge_isXMLExist = false;
			Logger.error(e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			judge_isXMLExist = false;
			Logger.error(e.getMessage(), e);
		}

		String moduleName = ruleExecContext.getBusinessComponent().getModule();
		// 根据模块名查询出该模块下所有的单据名及单据号
		String sqlAllBill = "select billtypename,pk_billtypecode from bd_billtype "
				+ "where  istransaction = 'N' and "
				+ "nodecode in(select funcode from sm_funcregister "
				+ " where own_module in(select moduleid from dap_dapsystem where devmodule = '"
				+ moduleName + "')) ";

		DataSet dataAllBill = executeQuery(ruleExecContext, sqlAllBill);

		// key：单据号 value：单据名称
		Map<String, String> mapAllBill = new HashMap<String, String>();
		if (!dataAllBill.isEmpty()) {
			for (DataRow data : dataAllBill.getRows()) {
				mapAllBill.put(data.getValue("pk_billtypecode").toString(),
						data.getValue("billtypename").toString());
			}
			Iterator<String> iterator = mapAllBill.keySet().iterator();
			List<String> listNotInsert = new ArrayList<String>();
			List<String> listXMLNotExist = new ArrayList<String>();
			while (iterator.hasNext()) {
				String pk_billtypecode = iterator.next();
				String sqlBillRule = "select * from pub_bcr_rulebase where nbcrcode = '"
						+ pk_billtypecode + "' and isdefault = 'Y'";
				DataSet dataBillRule = executeQuery(ruleExecContext,
						sqlBillRule);
				if (dataBillRule.isEmpty()) {
					listNotInsert.add(pk_billtypecode);
				} else {
					// xml文件是否导出判断
					boolean judge_isXMLBillExist = false;
					for (String billCode : listXMLBillCode) {
						if (pk_billtypecode.equals(billCode)) {
							judge_isXMLBillExist = true;
						}
					}
					if (!judge_isXMLBillExist) {
						listXMLNotExist.add(pk_billtypecode);
					}
				}
			}

			StringBuffer noteString = new StringBuffer();
			boolean judge_isError = false;
			noteString.append("       模块" + moduleName + "下：\n");
			if (listNotInsert.size() > 0) {
				judge_isError = true;
				for (String pk : listNotInsert) {
					noteString.append(mapAllBill.get(pk) + " " + pk + "\n");
				}
				noteString.append("没有设置默认编码规则\n");
			}
			if (listXMLNotExist.size() > 0) {
				if (judge_isXMLExist) {
					judge_isError = true;
					for (String pk : listXMLNotExist) {
						noteString.append(mapAllBill.get(pk) + " " + pk + "\n");
					}
					noteString.append("导出的xml文件不存在\n");
				} else {
					noteString.append("没有导出XML文件");
				}
			}
			listXMLNotExist.clear();
			listNotInsert.clear();
			listXMLBillCode.clear();
			mapAllBill.clear();
			if (judge_isError) {
				return new ErrorRuleExecuteResult(getIdentifier(),
						noteString.toString());
			}
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
