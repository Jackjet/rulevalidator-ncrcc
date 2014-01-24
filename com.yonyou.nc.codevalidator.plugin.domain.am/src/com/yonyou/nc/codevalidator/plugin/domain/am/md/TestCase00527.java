package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.CollectionUtils;
import com.yonyou.nc.codevalidator.sdk.utils.ListEqualVO;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "单据模版上的自定义项与元数据自定义项的匹配问题", relatedIssueId = "527",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang")
public class TestCase00527 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			// 获取元数据所有实体
			// 主实体
			if(metadataFile.containsMainEntity()) {
				List<IEntity> entitys = metadataFile.getAllEntities();
				// 元数据上的所有自定义项
				List<String> allDefNameList = new ArrayList<String>(); // 存放元数据上的所有实体的自定义项
				for (IEntity entity : entitys) { // 遍历实体
					List<IAttribute> listentiy = entity.getAttributes();
					for (IAttribute ia : listentiy) {
						if (ia.getName().startsWith("def")) {
							allDefNameList.add(ia.getName());
						}
					}
				}
				IEntity mainEntity = metadataFile.getMainEntity();
				// 目的 得到主实体——》单据模版上的所有自定义项
				String mainEntityId = mainEntity.getId();
				String funcodeQuerySql = "select funcode from sm_funcregister where mdid = '" + mainEntityId + "'";
				DataSet funcodeDataSet = SQLQueryExecuteUtils.executeQuery(funcodeQuerySql, ruleExecContext.getRuntimeContext());
				Map<String, List<String>> map = new HashMap<String, List<String>>();
				if (!funcodeDataSet.isEmpty()) { // 功能注册了
					List<DataRow> maindatarow = funcodeDataSet.getRows();
					for (DataRow dataRow : maindatarow) { // 对应多个模版时
						String mainfuncode = (String) dataRow.getValue("funcode");
						String sqlsql = "select b.METADATAPROPERTY " + "from pub_billtemplet a,pub_billtemplet_b b "
								+ " where a.PK_BILLTEMPLET = b.PK_BILLTEMPLET " + "and a.nodecode = '" + mainfuncode + "'";
						// DataSet dsbill_bb = getDataSet(sqlsql); //单据模版字段
						DataSet dsbill_bb = SQLQueryExecuteUtils.executeQuery(sqlsql, ruleExecContext.getRuntimeContext()); // 单据模版字段
						
						if (!dsbill_bb.isEmpty()) { // 说明画了单据模版
							List<String> mainlist = new ArrayList<String>();
							List<DataRow> datarowbilltemplet_bb = dsbill_bb.getRows();
							for (DataRow drr : datarowbilltemplet_bb) {
								String s = (String) drr.getValue("METADATAPROPERTY");
								if(StringUtils.isNotBlank(s)) {
									String stringarray[] = s.split("\\.");
									int length = stringarray.length;
									if (stringarray[length - 1].startsWith("def")) {
										mainlist.add(stringarray[length - 1]); // 获得单据模版所有自定义项
									}
								}
							}
							
							
							//modified by zhangnane 原有的比较方法有问题
							ListEqualVO<String> judgeVO = CollectionUtils.isListEqual(mainlist, allDefNameList);
							List<String> notIncludeDef = judgeVO.getNotExistDest();
							if(notIncludeDef!=null&&notIncludeDef.size()>0){
								map.put(mainfuncode, notIncludeDef);
							}
//							List<String> shengyulist = new ArrayList<String>();
//							if (mainlist.size() != allDefNameList.size()) { // 不相等说明少拖了自定义项
//								for (String main : allDefNameList) {
//									if (mainlist.contains(main)) {
//										mainlist.remove(main);
//									} else {
//										shengyulist.add(main);
//									}
//								}
//								if (shengyulist.size() > 0) {
//									map.put(mainfuncode, shengyulist);
//								}
//								
//							}
						}
					}
				}
				if (!map.isEmpty()) {
					StringBuilder noteBuilder = new StringBuilder();
					for (Entry<String, List<String>> key : map.entrySet()) {
						noteBuilder.append(key.getKey() + ":" + key.getValue());
					}
					result.addResultElement("\n" + resource.getResourcePath(),
							"单据模版缺少信息如下 |(功能号：缺少自定义项)|：" + noteBuilder.toString() + "\n"); // 为所有缺少(拖)自定义项的字段
				}
			}
		}
		return result;
	}

}
