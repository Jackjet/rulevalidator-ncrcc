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

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "��鵱ǰ���еļ��ŵ���ת��������Ԥ��ת�������Ƿ���ͬ", relatedIssueId = "884",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangnane")
public class TestCase00884 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		//TODO: �ع���ģ�鼶��״̬
		// ģ����Ϣ��д
		String modulecode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		// Ԥ�ƽ��������ѯ
		DataSet globalVOChange = SQLQueryExecuteUtils.executeQuery(getCheckSql(modulecode, true),
				ruleExecContext.getRuntimeContext());
		// ���Ž��������ѯ
		DataSet groupVOChange = SQLQueryExecuteUtils.executeQuery(getCheckSql(modulecode, false),
				ruleExecContext.getRuntimeContext());
		// �õ�������
		StringBuilder noteBuilder = getDiffResult(globalVOChange, groupVOChange);

		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
	}

	/**
	 * ������Ϊ�����ַ���=��Դ��������+��Դ��������+Ŀ�ĵ�������+Ŀ�Ľ�������+Ŀ������
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
	 * �Ƚ�Ԥ�����ݺͼ������ݣ�����ͬ�з���
	 * 
	 * @param globalVOChange
	 * @param groupVOChange
	 * @return
	 */
	private StringBuilder getDiffResult(DataSet globalVOChange, DataSet groupVOChange) {

		StringBuilder noteBuilder = new StringBuilder();
		// �����Ž�������ռ��ŷ��� ��--����id��ֵ--vo��������list
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
		// ������ѭ��
		while (groupMapItr.hasNext()) {

			Entry<String, List<DataRow>> grouptemp = groupMapItr.next();
			String pk_group = grouptemp.getKey();
			List<DataRow> globalDataRows = globalVOChange.getRows();
			List<DataRow> groupDataRows = grouptemp.getValue();
			// ���ڱȽϵ�map ��--����Դ��������+��Դ��������+Ŀ�ĵ�������+Ŀ�Ľ������ͣ���ֵ--�����list
			// ÿ�����ŷֿ��Ƚ�
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
						noteBuilder.append(String.format("����pk:%s ��������:%s ��Դ��������:%s Ŀ�ĵ�������:%s Ŀ������:%s ��Ԥ�����ݲ�ͬ\n",
								pk_group, row.getValue("PK_VOCHANGE"), row.getValue("SRC_BILLTYPE"),
								row.getValue("DEST_BILLTYPE"), row.getValue("DEST_ATTR")));
					}
				} else if (comparetemp.getValue().size() == 1) {
					DataRow row = comparetemp.getValue().get(0);
					String pk_groupInMap = (String) row.getValue("PK_GROUP");
					if (pk_groupInMap.equals("global00000000000000")) {
						noteBuilder.append(String.format(
								"����pk:%s ��������:%s ��Դ��������:%s Ŀ�ĵ�������:%s Ŀ������:%s Ԥ�������д��ڼ��������в�����\n", pk_group,
								row.getValue("PK_VOCHANGE"), row.getValue("SRC_BILLTYPE"),
								row.getValue("DEST_BILLTYPE"), row.getValue("DEST_ATTR")));
					} else {
						noteBuilder.append(String.format(
								"����pk:%s ��������:%s ��Դ��������:%s Ŀ�ĵ�������:%s Ŀ������:%s ���������д���Ԥ�������в�����\n", pk_group,
								row.getValue("PK_VOCHANGE"), row.getValue("SRC_BILLTYPE"),
								row.getValue("DEST_BILLTYPE"), row.getValue("DEST_ATTR")));
					}
				}
			}

		}

		return noteBuilder;
	}

	/**
	 * �Ƚ������ݣ��Ƚ��ֶΣ��������ͣ�����
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
	 * ��ѯ���ݿ�SQL
	 * 
	 * @param modulecode
	 *            ģ���
	 * @param globalflag
	 *            �Ƿ�Ԥ������
	 * @return
	 */
	private String getCheckSql(String modulecode, boolean globalflag) {

		StringBuilder checkSql = new StringBuilder();

		checkSql.append("select pub_vochange.dest_billtype dest_billtype,pub_vochange.dest_transtype dest_transtype,pub_vochange.src_billtype src_billtype,pub_vochange.src_transtype src_transtype,pub_vochange_b.*,pub_vochange.pk_group ");
		checkSql.append(" from pub_vochange ");
		checkSql.append("left join pub_vochange_b ");
		checkSql.append("on pub_vochange_b.pk_vochange = pub_vochange.pk_vochange ");
		// ͨ����Դ�������ͺ�Ŀ�ĵ�������ȷ����鷶Χ
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
