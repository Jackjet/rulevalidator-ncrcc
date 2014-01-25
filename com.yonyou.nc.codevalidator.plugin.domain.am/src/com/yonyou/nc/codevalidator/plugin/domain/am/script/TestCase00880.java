package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "������������Ѷ���������ȴδ����Ĭ�ϱ��������--���ż�", relatedIssueId = "880",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00880 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleName = ruleExecContext.getBusinessComponent().getModule();

		// ��ѯ��������Ҫע�Ἧ�ż��������ı������
		String sqlBcrGroup = "Select bn.Code,bb.billtypename From pub_bcr_nbcr bn ,Bd_Billtype bb Where "
				+ " bn.codescope='group' and bn.Code = bb.pk_billtypecode and bb.Istransaction = 'N'  And bb.Systemcode = '"
				+ moduleName.toUpperCase() + "'";
		// ��ѯ����ǰģ�������еı������
		String sqlRuleBase = "select nbcrcode,pk_group from pub_bcr_rulebase where nbcrcode in ("
				+ " Select Pk_Billtypecode From Bd_Billtype Where Istransaction = 'N'  And Systemcode = '"
				+ moduleName.toUpperCase() + "')";
		// ��ǰ�������м���
		String sqlGroup = " select pk_group,name from org_group where dr = 0 ";

		DataSet dataBcrGroup = executeQuery(ruleExecContext, sqlBcrGroup);

		DataSet dataRuleBase = executeQuery(ruleExecContext, sqlRuleBase);

		DataSet dataGroup = executeQuery(ruleExecContext, sqlGroup);
		StringBuffer noteString = new StringBuffer();
		if (!dataRuleBase.isEmpty() && !dataGroup.isEmpty()) {
			// key:���ݺ� value:������ Ĭ�ϱ��������Ϣ
			Map<String, List<String>> mapRuleBase = new HashMap<String, List<String>>();
			// ���еļ�����Ϣ key:���� value:����
			Map<String, String> orgGroups = new HashMap<String, String>();
			// ��ע������������е��ݺ� key:���ݺ� value����������
			// ���ż�����ע������������е��ݺ� key:���ݺ� value����������
			Map<String, String> bcrCodesGroup = new HashMap<String, String>();
			
			for (DataRow row : dataRuleBase.getRows()) {
				String code = row.getValue("nbcrcode").toString();
				String group = row.getValue("pk_group").toString();
				if (mapRuleBase.containsKey(code)) {
					mapRuleBase.get(code).add(group);
				} else {
					List<String> groups = new ArrayList<String>();
					groups.add(group);
					mapRuleBase.put(code, groups);
				}
			}
			// ������ע��������ĵ��ݺ� ���ż�
			for (DataRow row : dataBcrGroup.getRows()) {
				bcrCodesGroup.put(row.getValue("code").toString(), row.getValue("billtypename").toString());
			}
	
			// ��ǰ��������
			for (DataRow row : dataGroup.getRows()) {
				orgGroups.put(row.getValue("pk_group").toString(), row.getValue("name").toString());
			}
			orgGroups.put("presetdatagrouppk000", "Ԥ������");
			Iterator<String> iteratorCodesGroup = bcrCodesGroup.keySet().iterator();
			// ���ż����ж���ע��ı�������Ƿ�ע��������
			while (iteratorCodesGroup.hasNext()) {
				String code = iteratorCodesGroup.next();
				if (!mapRuleBase.containsKey(code)) {
					// û��ע���������code
					noteString.append("���ݣ�" + code + bcrCodesGroup.get(code) + "�����м��Ŷ�û��ע��Ĭ�ϱ������         \n");
				} else {

					if (mapRuleBase.get(code).size() < orgGroups.size()) {
						noteString.append("���ݣ�" + code + "-" + bcrCodesGroup.get(code) + ":�����¼��� ");
						Iterator<String> orgGroupsPk = orgGroups.keySet().iterator();
						while (orgGroupsPk.hasNext()) {
							String groupPk = orgGroupsPk.next();
							if (!mapRuleBase.get(code).contains(groupPk)) {
								// code��ע�������󣬵�����group����û��ע��������
								noteString.append(groupPk + "-" + orgGroups.get(groupPk) + ";");
							}
						}

						noteString.append("û��ע��Ĭ�ϱ������           \n");
					}
				}
			}
		}

		if (noteString.length() > 0) {
			return new ErrorRuleExecuteResult(getIdentifier(), noteString.toString());
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
