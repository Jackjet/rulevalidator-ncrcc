package com.yonyou.nc.codevalidator.plugin.domain.platform.md;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.METADATA, executePeriod = ExecutePeriod.CHECKOUT,
		executeLayer = ExecuteLayer.BUSICOMP,
		description = "若一个组件含有若干个相互关系的实体（只有一个主实体），在主实体中表示“主子实体关系”的属性的类型为该子实体的名称，字段名称为主实体的主键名", relatedIssueId = "456",
		subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "suncheng3",
		memo = "主实体中表示“主子实体关系”的属性的类型为该子实体的名称，字段名称为子实体的主键名", solution = "主实体中表示“主子实体关系”的属性的类型应为该子实体的名称，字段名称应为主实体的主键名")
public class TestCase00456 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		// 获取所有的元数据
		if (resources != null && resources.size() > 0) {
			for (MetadataResource metadataResource : resources) {
				StringBuilder noteBuilder = new StringBuilder();
				IMetadataFile metafile = metadataResource.getMetadataFile();

				// 获取元数据对应的所有实体
				IEntity mainEntity = metafile.getMainEntity();
				// 获取主实体对应的所有属性
				if (mainEntity != null) {
					List<IAttribute> attributes = mainEntity.getAttributes();
					// 属性名和属性对应的map
					Map<String, IAttribute> attnameAttributeMap = new HashMap<String, IAttribute>();
					for (IAttribute attribute : attributes) {
						// 如果为引用类型入map
						if (MDRuleConstants.TYPE_STYLE_REF.equalsIgnoreCase(attribute.getTypeStyle())) {
							attnameAttributeMap.put(attribute.getName(), attribute);
						}
					}
					// 获取元数据对应的所有实体
					List<IEntity> entities = metafile.getAllEntities();
					for (IEntity entity : entities) {
						// 规则仅仅对子实体有效，去掉主实体
						if (entity.getId() != mainEntity.getId()) {
							String entityName = entity.getDisplayName();
							if (attnameAttributeMap.containsKey(entityName)) {
								IAttribute attribute = attnameAttributeMap.get(entityName);
								if (attribute.getFieldName().equals(mainEntity.getKeyAttribute().getFieldName()) == false) {
									noteBuilder.append(mainEntity.getFullClassName()).append("的")
											.append(attribute.getDisplayName()).append("属性的字段名与所对应的主实体")
											.append(entity.getFullName()).append("的主键名不符/n");
								}
							} else {
								noteBuilder.append("子实体").append(entity.getFullName()).append("的名称在主实体")
										.append(mainEntity.getFullClassName()).append("的属性中找不到/n");
							}
						}
					}
					if (noteBuilder.toString().trim().length() > 0) {
						result.addResultElement(metadataResource.getResourcePath(), noteBuilder.toString());
					}
				}
			}
		}
		return result;
	}

}
