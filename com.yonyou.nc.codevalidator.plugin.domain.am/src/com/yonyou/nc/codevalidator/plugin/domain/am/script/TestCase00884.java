package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "检查当前库中的集团单据转换规则与预制转换规则是否相同", relatedIssueId = "884",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangnane")
public class TestCase00884 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		//TODO: 重构，模块级，状态
		// 模块信息大写
		String modulecode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		// 预制交换规则查询
		DataSet globalVOChange = SQLQueryExecuteUtils.executeQuery(getCheckSql(modulecode, true),
				ruleExecContext.getRuntimeContext());
		// 集团交换规则查询
		DataSet groupVOChange = SQLQueryExecuteUtils.executeQuery(getCheckSql(modulecode, false),
				ruleExecContext.getRuntimeContext());
		// 得到差异结果
		StringBuilder noteBuilder = getDiffResult(globalVOChange, groupVOChange);

		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
	}

	/**
	 * 返回作为键的字符串=来源单据类型+来源交易类型+目的单据类型+目的交易类型+目的属性
	 * 
	 * @param dataRow
	 * @return
	 */
	private String getRowKey(DataRow dataRow) {

		String rowKey = (String) dataRow.getValue("DEST_BILLTYPE") + (String) dataRow.getValue("DEST_TRANSTYPE")
				+ (String) dataRow.getValue("SRC_BILLTYPE") + (String) dataRow.getValue("SRC_TRANSTYPE")
				+ (String) dataRow.getValue("DEST_ATTR");

		return rowKey;
	}

	/**
	 * 比较预制数据和集团数据，将不同行返回
	 * 
	 * @param globalVOChange
	 * @param groupVOChange
	 * @return
	 */
	private StringBuilder getDiffResult(DataSet globalVOChange, DataSet groupVOChange) {

		StringBuilder noteBuilder = new StringBuilder();
		// 将集团结果集按照集团分组 键--集团id，值--vo交换规则list
		Map<String, List<DataRow>> groupToDataRows = new HashMap<String, List<DataRow>>();

		for (DataRow dataRow : groupVOChange.getRows()) {
			String grouprowKey = (String) dataRow.getValue("PK_GROUP");
			List<DataRow> groupRows = groupToDataRows.get(grouprowKey);
			if (groupRows != null && !groupRows.isEmpty()) {
				groupRows.add(dataRow);
			} else {
				List<DataRow> newRows = new ArrayList<DataRow>();
				newRows.add(dataRow);
				groupToDataRows.put(grouprowKey, newRows);
			}
		}

		Iterator<Entry<String, List<DataRow>>> groupMapItr = groupToDataRows.entrySet().iterator();
		// 按集团循环
		while (groupMapItr.hasNext()) {

			Entry<String, List<DataRow>> grouptemp = groupMapItr.next();
			String pk_group = grouptemp.getKey();
			List<DataRow> globalDataRows = globalVOChange.getRows();
			List<DataRow> groupDataRows = grouptemp.getValue();
			// 用于比较的map 键--（来源单据类型+来源交易类型+目的单据类型+目的交易类型），值--结果行list
			// 每个集团分开比较
			Map<String, List<DataRow>> compareMap = new HashMap<String, List<DataRow>>();

			addToCompareMap(globalDataRows, compareMap);
			addToCompareMap(groupDataRows, compareMap);

			Iterator<Entry<String, List<DataRow>>> compareMapItr = compareMap.entrySet().iterator();
			while (compareMapItr.hasNext()) {

				Entry<String, List<DataRow>> comparetemp = compareMapItr.next();
				if (comparetemp.getValue().size() > 1) {
					List<DataRow> compareList = comparetemp.getValue();
					if (!compareRow(compareList.get(0), compareList.get(1))) {
						DataRow row = compareList.get(1);
						noteBuilder.append(String.format("集团pk:%s 交换规则:%s 来源单据类型:%s 目的单据类型:%s 目的属性:%s 与预制数据不同\n",
								pk_group, row.getValue("PK_VOCHANGE"), row.getValue("SRC_BILLTYPE"),
								row.getValue("DEST_BILLTYPE"), row.getValue("DEST_ATTR")));
					}
				} else if (comparetemp.getValue().size() == 1) {
					DataRow row = comparetemp.getValue().get(0);
					String pk_groupInMap = (String) row.getValue("PK_GROUP");
					if (pk_groupInMap.equals("global00000000000000")) {
						noteBuilder.append(String.format(
								"集团pk:%s 交换规则:%s 来源单据类型:%s 目的单据类型:%s 目的属性:%s 预制数据中存在集团数据中不存在\n", pk_group,
								row.getValue("PK_VOCHANGE"), row.getValue("SRC_BILLTYPE"),
								row.getValue("DEST_BILLTYPE"), row.getValue("DEST_ATTR")));
					} else {
						noteBuilder.append(String.format(
								"集团pk:%s 交换规则:%s 来源单据类型:%s 目的单据类型:%s 目的属性:%s 集团数据中存在预制数据中不存在\n", pk_group,
								row.getValue("PK_VOCHANGE"), row.getValue("SRC_BILLTYPE"),
								row.getValue("DEST_BILLTYPE"), row.getValue("DEST_ATTR")));
					}
				}
			}

		}

		return noteBuilder;
	}

	/**
	 * 比较行数据，比较字段：规则类型，规则
	 * 
	 * @param globalRow
	 * @param groupRow
	 * @return
	 */
	private boolean compareRow(DataRow globalRow, DataRow groupRow) {

		boolean isEqual = false;
		String ruledata = (String) globalRow.getValue("RULEDATA");
		String ruletype = globalRow.getValue("RULETYPE") + "";

		String ruledata_g = (String) groupRow.getValue("RULEDATA");
		String ruletype_g = groupRow.getValue("RULETYPE") + "";

		if (ruledata.equals(ruledata_g) && ruletype.equals(ruletype_g)) {
			isEqual = true;
		}
		return isEqual;
	}

	private void addToCompareMap(List<DataRow> dataRows, Map<String, List<DataRow>> compareMap) {

		for (DataRow datarow : dataRows) {

			String rowKey = getRowKey(datarow);
			List<DataRow> compareList = compareMap.get(rowKey);
			if (compareList != null && !compareList.isEmpty()) {
				compareList.add(datarow);
			} else {
				List<DataRow> newRows = new ArrayList<DataRow>();
				newRows.add(datarow);
				compareMap.put(rowKey, newRows);
			}
		}
	}

	/**
	 * 查询数据库SQL
	 * 
	 * @param modulecode
	 *            模块号
	 * @param globalflag
	 *            是否预制数据
	 * @return
	 */
	private String getCheckSql(String modulecode, boolean globalflag) {

		StringBuilder checkSql = new StringBuilder();

		checkSql.append("select pub_vochange.dest_billtype dest_billtype,pub_vochange.dest_transtype dest_transtype,pub_vochange.src_billtype src_billtype,pub_vochange.src_transtype src_transtype,pub_vochange_b.*,pub_vochange.pk_group ");
		checkSql.append(" from pub_vochange ");
		checkSql.append("left join pub_vochange_b ");
		checkSql.append("on pub_vochange_b.pk_vochange = pub_vochange.pk_vochange ");
		// 通过来源单据类型和目的单据类型确定检查范围
		checkSql.append("where src_billtype in ");

		checkSql.append("(select pk_billtypecode from  bd_billtype where SYStemcode like '" + modulecode
				+ "') and dest_billtype in (select pk_billtypecode from  bd_billtype where SYStemcode like '"
				+ modulecode + "') ");

		if (globalflag) {
			checkSql.append("and pk_group like 'global00000000000000'");
		} else {
			checkSql.append("and pk_group not like 'global00000000000000'");
		}
		return checkSql.toString();
	}

}
