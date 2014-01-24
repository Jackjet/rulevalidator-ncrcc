package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MDRuleConstants;
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
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "检测单据模板时检查表体页签编码与元数据主实体属性中子表名称是否一致",
		relatedIssueId = "543", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "ZG")
public class TestCase00543 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			// 主实体
			if(metadataFile.containsMainEntity()) {
				IEntity mainentity = metadataFile.getMainEntity();
				if (containMultiEntity(metadataFile)) {
					String id = mainentity.getId();
					// 得到元数据对应的单据模版
					String preSql = "select b.BILL_TEMPLETCAPTION ,b.NODECODE,b.PK_BILLTYPECODE,b.PK_BILLTEMPLET "
							+ " from  sm_funcregister a,pub_billtemplet b"
							+ " where  a.funcode = b.NODECODE and  a.mdid = '" + id + "'";
					DataSet dstemp = SQLQueryExecuteUtils.executeQuery(preSql, ruleExecContext.getRuntimeContext());
					// 不为空说明一画了单据模版 没画单据模版的情况下不校验
					if (!dstemp.isEmpty()) {
						List<String> arrayStyleAttributeNameList = new ArrayList<String>(); // 存放主实体中子实体的名称
						// 元数据上子实体名称list
						List<IAttribute> attributes = mainentity.getAttributes();
						
						//获取元数据名称
						String mateName = mainentity.getDisplayName();
						for (IAttribute attribute : attributes) {
							if (attribute.getTypeStyle().trim().equals(MDRuleConstants.TYPE_STYLE_ARRAY)) { // 为Array样式
								arrayStyleAttributeNameList.add(attribute.getName());// ////
							}
						}
						// 单据模版的主键放到tabcodelistt中，单据模版主键-名称放到map中
						HashMap<String, String> billTemplateInfoMap = new HashMap<String, String>();
						List<DataRow> datatemplet = dstemp.getRows();
					
						for (DataRow dr : datatemplet) {
							billTemplateInfoMap.put((String) dr.getValue("PK_BILLTEMPLET"),
									(String) dr.getValue("BILL_TEMPLETCAPTION"));
						}
					
						
						for (Map.Entry<String, String> entry : billTemplateInfoMap.entrySet()) {
							String pk_billtemplet = entry.getKey();
							// 单据模版页签数
							StringBuilder billTemplateTobillTemplate_tSQL = new StringBuilder();
							billTemplateTobillTemplate_tSQL.append(" select distinct c.tabname, c.tabcode ");
							billTemplateTobillTemplate_tSQL.append(" from pub_billtemplet b, pub_billtemplet_t c  ");
							billTemplateTobillTemplate_tSQL.append(" where b.PK_BILLTEMPLET = c.PK_BILLTEMPLET and c.pos = 1");
							billTemplateTobillTemplate_tSQL.append(" and b.PK_BILLTEMPLET = '"+pk_billtemplet+"'");
							DataSet dsbill_templet = SQLQueryExecuteUtils.executeQuery(
									billTemplateTobillTemplate_tSQL.toString(), ruleExecContext.getRuntimeContext());
							List<DataRow> datarowbilltemplet = dsbill_templet.getRows();
							List<String> tabcodelist = new ArrayList<String>();
							if (!datarowbilltemplet.isEmpty()) {
								for (DataRow dr : datarowbilltemplet) {
									tabcodelist.add((String) dr.getValue("TABCODE"));// ////
								}
								ListEqualVO<String> compareVO = CollectionUtils.isListEqual(tabcodelist, arrayStyleAttributeNameList);
								if (!compareVO.getIsEqual()) {
									List<String> notExistInTemplet = compareVO.getNotExistDest();
									for(String field : notExistInTemplet){
										noteBuilder.append("单据模板: "+billTemplateInfoMap.get(pk_billtemplet)+" 中存在而对应元数据："+mateName+"中不存在的页签字段："+field +"\n");
									}
									List<String> notExistInMata = compareVO.getNotExistSrc();
									for(String property : notExistInMata){
										noteBuilder.append("元数据："+mateName+" 中的array字段在单据模板："+billTemplateInfoMap.get(pk_billtemplet)+"中不是页签的字段："+property+"\n");
									}
								}
							}
						}
					}
				}
			}
			if (StringUtils.isNotBlank(noteBuilder.toString())) {
				result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
			}
		}
		return result;
	}
}


