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
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "元数据信息字段有权组是否在业务实体管理中存在检查", relatedIssueId = "686",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "xiepch")
public class TestCase00686 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			// 获取元数据所有实体
			List<IEntity> entitys = metadataFile.getAllEntities();
			for (IEntity e : entitys) {
				// 所有有有权组字段的信息
				List<List<AccessPowerGroupInfo>> info = new ArrayList<List<AccessPowerGroupInfo>>();

				// 当前实体下使用权组不为空的实体字段
				String sql = "select name,datatype,accesspowergroup From md_Property  where classid = '" + e.getId()
						+ "' and accesspowergroup is not null;";
				DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
				if (!ds.isEmpty()) {
					// 先过滤所有_v字段名称
					for (DataRow da : ds.getRows()) {
						if (!da.getValue("name").toString().endsWith("_v")) {
							// 存放有权组信息
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
					// 将_v字段和相应字段放一起
					for (DataRow da : ds.getRows()) {
						if (da.getValue("name").toString().endsWith("_v")) {
							// 同一类的属性放在一个List中,如pk_unit_used和pk_unit_used_v
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
					// 存放当前实体下所有有使用权组的字段-值
					Map<String, String> map = new HashMap<String, String>();
					// 判断所有有权组不为空的字段在业务实体管理中是否存在此有权组
					for (List<AccessPowerGroupInfo> singleInfo : info) {
						if (singleInfo.size() == 1) {
							AccessPowerGroupInfo a = singleInfo.get(0);
							if (!isExitAccessPowerGroup(a.datatype, a.accesspowergroup, ruleExecContext)) {
								map.put(a.name, a.accesspowergroup);
							}
						} else {
							AccessPowerGroupInfo a = singleInfo.get(0);
							AccessPowerGroupInfo b = singleInfo.get(1);
							// 需要判断两次，有的有权组是_v注册的为无_v的权限资源，有的相反
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
					// 添加备注信息
					if (map.size() > 0) {
						// 注释信息
						StringBuilder noteBuilder = new StringBuilder();
						noteBuilder.append(e.getDisplayName() + "\n");
						Iterator<String> iterator = map.keySet().iterator();
						while (iterator.hasNext()) {
							String key = iterator.next();
							noteBuilder.append(String.format("字段名：%s  有权组：%s  \n", key, map.get(key)));
						}
						result.addResultElement(resource.getResourcePath(), noteBuilder.toString()
								+ "在业务实体管理中未注册！           \n");
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
