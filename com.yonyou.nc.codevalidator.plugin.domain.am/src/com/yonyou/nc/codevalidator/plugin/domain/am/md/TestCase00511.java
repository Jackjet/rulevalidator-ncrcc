package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
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
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "元数据中的实体和组件中的显示名称必须为中文 ", relatedIssueId = "511",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00511 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		// 检查结果
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			// 获取元数据文件
			IMetadataFile metadataFile = resource.getMetadataFile();
			// 获取元数据所有实体
			List<IEntity> entitys = metadataFile.getAllEntities();
			for (IEntity entity : entitys) { // 遍历实体
				StringBuilder noteBuilder = new StringBuilder();
				if (!StringUtils.isAllChineseCharacter(entity.getDisplayName())) { // 实体显示名称汉字的判断
					noteBuilder.append(entity.getDisplayName() + "实体的显示名称中含有不是中文汉字的其他字符！");
				}
				List<IAttribute> entityattlist = entity.getAttributes(); // 实体属性集
				for (IAttribute attribute : entityattlist) {
					String attdisplayname = attribute.getDisplayName(); // 实体属性显示名字
					if (!StringUtils.isAllChineseCharacter(attdisplayname)) { // 包含中文
						noteBuilder.append(entity.getDisplayName() + "实体中的").append(attdisplayname)
								.append("字段显示名称没有中文汉字！");
					}
				}
				if (noteBuilder.length() != 0) {
					result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
				}
			}
		}
		return result;
	}

}
