package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.md.MDRuleConstants;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.METADATA, description = "元数据属性名称与数据库字段名称校验工具", relatedIssueId = "677",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangnane", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00677 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			IMetadataFile metadataFile = resource.getMetadataFile();
			List<IEntity> allEntities = metadataFile.getAllEntities();
			StringBuilder noteBuilder = new StringBuilder();
			for (IEntity entity : allEntities) { // 实体遍历
				List<IAttribute> attributeList = entity.getAttributes(); // 实体属性集
				for (IAttribute attribute : attributeList) {
					// 如果类型是list类型的则可能是子表的字段
					if (!attribute.getName().equals(attribute.getFieldName())
							&& !attribute.getTypeStyle().equals(MDRuleConstants.TYPE_STYLE_ARRAY)) {
						noteBuilder.append(String.format("\n实体：%s 的属性名称:%s 与 字段名称 :%s 不一致   ", entity.getDisplayName(),
								attribute.getName(), attribute.getFieldName()));
					}
				}
			}
			if (noteBuilder.length() > 0) {
				result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
			}
		}

		return result;
	}

}
