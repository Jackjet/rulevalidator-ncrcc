package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "Ԫ������Ϣ�ֶ���Ȩ���Ƿ���ҵ��ʵ������д��ڼ��", relatedIssueId = "686",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "xiepch")
public class TestCase00686 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// ��ȡԪ�����ļ�
			IMetadataFile metadataFile = resource.getMetadataFile();
			// ��ȡԪ��������ʵ��
			List<IEntity> entitys = metadataFile.getAllEntities();
			for (IEntity e : entitys) {
				// ��������Ȩ���ֶε���Ϣ
				List<List<AccessPowerGroupInfo>> info = new ArrayList<List<AccessPowerGroupInfo>>();

				// ��ǰʵ����ʹ��Ȩ�鲻Ϊ�յ�ʵ���ֶ�
				String sql = "select name,datatype,accesspowergroup From md_Property  where classid = '" + e.getId()
						+ "' and accesspowergroup is not null;";
				DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
				if (!ds.isEmpty()) {
					// �ȹ�������_v�ֶ�����
					for (DataRow da : ds.getRows()) {
						if (!da.getValue("name").toString().endsWith("_v")) {
							// �����Ȩ����Ϣ
							List<AccessPowerGroupInfo> singleInfo = new ArrayList<AccessPowerGroupInfo>();
							AccessPowerGroupInfo a = new AccessPowerGroupInfo();
							a.name = (String) da.getValue("name");
							a.datatype = (String) da.getValue("datatype");
							a.accesspowergroup = (String) da.getValue("accesspowergroup");
							;
							singleInfo.add(a);
							info.add(singleInfo);
						}
					}
					// ��_v�ֶκ���Ӧ�ֶη�һ��
					for (DataRow da : ds.getRows()) {
						if (da.getValue("name").toString().endsWith("_v")) {
							// ͬһ������Է���һ��List��,��pk_unit_used��pk_unit_used_v
							List<Object> singleInfo = new ArrayList<Object>();
							AccessPowerGroupInfo a = new AccessPowerGroupInfo();
							a.name = (String) da.getValue("name");
							a.datatype = (String) da.getValue("datatype");
							a.accesspowergroup = (String) da.getValue("accesspowergroup");
							;
							singleInfo.add(a);
							for (List<AccessPowerGroupInfo> singleInfo1 : info) {
								if (singleInfo1.get(0).name.equals(a.name.substring(0, a.name.length() - 2))) {
									singleInfo1.add(a);
								}
							}
						}
					}
				}

				if (info.size() > 0) {
					// ��ŵ�ǰʵ����������ʹ��Ȩ����ֶ�-ֵ
					Map<String, String> map = new HashMap<String, String>();
					// �ж�������Ȩ�鲻Ϊ�յ��ֶ���ҵ��ʵ��������Ƿ���ڴ���Ȩ��
					for (List<AccessPowerGroupInfo> singleInfo : info) {
						if (singleInfo.size() == 1) {
							AccessPowerGroupInfo a = singleInfo.get(0);
							if (!isExitAccessPowerGroup(a.datatype, a.accesspowergroup, ruleExecContext)) {
								map.put(a.name, a.accesspowergroup);
							}
						} else {
							AccessPowerGroupInfo a = singleInfo.get(0);
							AccessPowerGroupInfo b = singleInfo.get(1);
							// ��Ҫ�ж����Σ��е���Ȩ����_vע���Ϊ��_v��Ȩ����Դ���е��෴
							if (!isExitAccessPowerGroup(a.datatype, a.accesspowergroup, ruleExecContext)) {
								if (!isExitAccessPowerGroup(b.datatype, a.accesspowergroup, ruleExecContext)) {
									map.put(a.name, a.accesspowergroup);
								}
							}
							if (!isExitAccessPowerGroup(b.datatype, b.accesspowergroup, ruleExecContext)) {
								if (!isExitAccessPowerGroup(a.datatype, b.accesspowergroup, ruleExecContext)) {
									map.put(b.name, b.accesspowergroup);
								}
							}

						}

					}
					// ��ӱ�ע��Ϣ
					if (map.size() > 0) {
						// ע����Ϣ
						StringBuilder noteBuilder = new StringBuilder();
						noteBuilder.append(e.getDisplayName() + "\n");
						Iterator<String> iterator = map.keySet().iterator();
						while (iterator.hasNext()) {
							String key = iterator.next();
							noteBuilder.append(String.format("�ֶ�����%s  ��Ȩ�飺%s  \n", key, map.get(key)));
						}
						result.addResultElement(resource.getResourcePath(), noteBuilder.toString()
								+ "��ҵ��ʵ�������δע�ᣡ           \n");
					}
				}
			}
		}

		return result;
	}

	public boolean isExitAccessPowerGroup(String datatype, String accessPowerGroup, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		String sql = "select sr.operationcode from Sm_Permission_Res sp,sm_res_operation sr "
				+ " where sr.resourceid = sp.pk_permission_res and sp.mdid = '" + datatype + "' and Sr.Operationcode='"
				+ accessPowerGroup + "'";
		DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());

		if (dataSet.isEmpty()) {
			return false;
		}
		return true;
	}
}

class AccessPowerGroupInfo {
	String name;
	String datatype;
	String accesspowergroup;
}
