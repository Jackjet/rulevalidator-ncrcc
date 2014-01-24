package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

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
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "测试若元数据(实体)上有自定义项则在用户定义属性组注册中必须注册",
		relatedIssueId = "536", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang",
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00536 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			// 获取元数据所有实体
			List<IEntity> entitys = metadataFile.getAllEntities();
			StringBuilder noteBuilder = new StringBuilder();
			for (IEntity entity : entitys) { // 遍历实体
				boolean containDef = false;
				List<IAttribute> attributes = entity.getAttributes(); // 如果IAttribute接口中有判断自定义项的方法后可以直接判断是否有自定义项
				// “目前暂时这样”
				for (IAttribute iaa : attributes) {
					if (iaa.getType().getDisplayName().contains("自定义项")) {
						containDef = true;
						break;
					}
				}
				if (containDef) { // 包含自定义项
					String id = entity.getId();
					// 查询语句 ref表
					String sql = String.format("select b.pk_userdefrule,a.name,a.code  "
							+ "from bd_userdefrule a,bd_userdefruleref b "
							+ "where a.pk_userdefrule = b.pk_userdefrule and " + " b.refclass = '%s'", id);
					DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
					if (ds.isEmpty()) { // 用户自定义属性组没有注册
						noteBuilder.append("实体：").append(entity.getDisplayName()).append(" 缺少用户自定义组注册!\n");
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
