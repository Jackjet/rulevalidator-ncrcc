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

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "������������Ѷ���������ȴδ����Ĭ�ϱ��������--ȫ�ּ�", relatedIssueId = "888",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00888 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleName = ruleExecContext.getBusinessComponent().getModule();

		// ��ѯ��������Ҫע��ȫ�ּ��������ı������
		String sqlBcrGlobal = "Select bn.Code,bb.billtypename From pub_bcr_nbcr bn ,Bd_Billtype bb Where "
				+ " bn.codescope='global' and bn.Code = bb.pk_billtypecode and bb.Istransaction = 'N'  And bb.Systemcode = '"
				+ moduleName.toUpperCase() + "'";
		// ��ѯ����ǰģ�������еı������
		String sqlRuleBase = "select nbcrcode,pk_group from pub_bcr_rulebase where nbcrcode in ("
				+ " Select Pk_Billtypecode From Bd_Billtype Where Istransaction = 'N'  And Systemcode = '"
				+ moduleName.toUpperCase() + "')";

		DataSet dataBcrGlobal = executeQuery(ruleExecContext, sqlBcrGlobal);

		DataSet dataRuleBase = executeQuery(ruleExecContext, sqlRuleBase);

		StringBuffer noteString = new StringBuffer();
		if (!dataRuleBase.isEmpty()) {
			
			// key�����ݺ�  value�����ż��ϣ��˴����ã�
			Map<String, String> bcrCodesGroup = new HashMap<String, String>();
			
			// key:���ݺ� value:������ Ĭ�ϱ��������Ϣ
			Map<String, List<String>> mapRuleBase = new HashMap<String, List<String>>();
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
			
			// ȫ�ּ�������ѯ��Ĭ�ϱ��������Ϣ�����Ե��ݺ���Ϊ�ؼ��֣��˵��ݺŵ����м�����Ϊvalue
			Map<String, String> bcrCodesGlobal = new HashMap<String, String>();
			
			// ȫ�ּ�
			for (DataRow row : dataBcrGlobal.getRows()) {
				bcrCodesGlobal.put(row.getValue("code").toString(), row.getValue("billtypename").toString());
			}
			Iterator<String> iteratorCodesGlobal = bcrCodesGlobal.keySet().iterator();
			// ȫ�ּ�
			while (iteratorCodesGlobal.hasNext()) {
				String code = iteratorCodesGlobal.next();
				if (!mapRuleBase.containsKey(code)) {
					noteString.append("���ݣ�" + code + bcrCodesGroup.get(code) + "��ע��ȫ�ּ���Ĭ�ϱ������         \n");
				}
			}
		}

		if (noteString.length() > 0) {
			return new ErrorRuleExecuteResult(getIdentifier(), noteString.toString());
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
