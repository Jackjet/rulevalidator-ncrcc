package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.CollectionUtils;
import com.yonyou.nc.codevalidator.sdk.utils.ListEqualVO;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "测试查询模版上的自定义项是否和对应元数据上的自定义项相等", relatedIssueId = "856",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00856 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			if (metadataFile.containsMainEntity()) {
				// 主实体
				IEntity mainentity = metadataFile.getMainEntity();
				String mainEntityId = mainentity.getId();
				String mainEntityDisplayName = mainentity.getDisplayName();
				// 元数据上的所有自定义项

				// 查询模版item查询语句 只要pk和field_code 两字段
				String sql = " select b.pk_templet,b.field_code " + " from pub_query_templet a,pub_query_condition b "
						+ "where a.id = b.pk_templet and a.metaclass = " + "'" + mainEntityId + "'"
						+ " and a.MODEL_NAME like '%" + mainEntityDisplayName + "%'" + " ";
				DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
				if (!ds.isEmpty()) { // 定义了查询模版
					List<DataRow> maindatarow = ds.getRows();
					// 得查询模版上的所有自定义项list
					List<String> defFieldCodeInDbList = new ArrayList<String>();
					for (DataRow dr : maindatarow) { // 遍历属性，寻找自定义项 包括主实体和子实体
						String fieldCode = (String) dr.getValue("field_code");
						if (fieldCode.startsWith("def")) { // 主实体
							defFieldCodeInDbList.add(fieldCode);
						} else if (fieldCode.startsWith("bodyvos.def")) { // 子实体
							String[] ss = fieldCode.split("\\.");
							defFieldCodeInDbList.add(ss[ss.length - 1]);
						}
					}

					// 得元数据自定义项list
					List<String> attribNameInMetaList = new ArrayList<String>(); // 存放元数据上的所有实体的自定义项
					List<IEntity> allEntities = metadataFile.getAllEntities();
					for (IEntity ientity : allEntities) {
						List<IAttribute> entityatt = ientity.getAttributes();
						for (IAttribute ia : entityatt) {
							if (ia.getName().startsWith("def") || ia.getName().startsWith("bodyvos.def")) {
								attribNameInMetaList.add(ia.getName());
							}
						}
					}

					// 输出信息
					// TODO : NB的代码
					StringBuilder noteBuildershengyu = new StringBuilder();

					ListEqualVO<String> compareVO = CollectionUtils.isListEqual(defFieldCodeInDbList,
							attribNameInMetaList);

					if (!compareVO.getIsEqual()) { // 如果长度相等则说明不缺少
						// 否则
						for (String s : compareVO.getNotExistDest()) { // 利用元数据的自定义项依次判断此项是否在查询模版上
							noteBuildershengyu.append(s + " ");

						}
						// 如果noteBuildershengyu不为空，说明有缺失信息
						if (noteBuildershengyu.toString().trim().length() > 0) {
							result.addResultElement("\n" + resource.getResourcePath(), mainEntityDisplayName
									+ "所对应的查询模版缺少信息如下(缺少项)：" + noteBuildershengyu.toString() + "\n\n");
						}
					}
				}
			}

		}
		return result;
	}

}
