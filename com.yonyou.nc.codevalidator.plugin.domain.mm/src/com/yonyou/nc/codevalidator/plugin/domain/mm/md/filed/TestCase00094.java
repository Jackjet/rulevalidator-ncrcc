package com.yonyou.nc.codevalidator.plugin.domain.mm.md.filed;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDConstants;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IAttribute;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * <li>
 * 元数据字段的显示名称中不能包含技术性内容（如：主键、ID、PK、OID、VID、主表、子表等信息）</li>
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, solution="元数据字段的显示名称中不能包含技术性内容(如：主键、ID、PK、OID、VID、主表、子表等信息)",
description = "元数据字段的显示名称中不能包含技术性内容(如：主键、ID、PK、OID、VID、主表、子表等信息)", relatedIssueId = "94", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe")
public class TestCase00094 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(
			List<MetadataResource> resources,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		ResourceRuleExecuteResult resourceRuleResult = new ResourceRuleExecuteResult();
		if (resources != null && resources.size() > 0) {
			for (MetadataResource resource : resources) {
				IMetadataFile metadataFile = resource.getMetadataFile();
				List<IEntity> allEntities = metadataFile.getAllEntities();
				StringBuilder result = new StringBuilder();
				for (IEntity entity : allEntities) {

					List<String> logicAttribs = getEntityShowNames(entity);
					if (logicAttribs.size() > 0) {
						result.append(String
								.format("实体【%s 】对应字段列表:%s ,存在的显示名称中包含技术性内容(如：主键、ID、PK、OID、VID、主表、子表等信息),请修改!\n",
										entity.getFullName(), logicAttribs));
					}
				}
				if (result.toString().trim().length() > 0) {
					resourceRuleResult.addResultElement(
							resource.getResourcePath(), result.toString());
				}
			}
		}
		return resourceRuleResult;
	}

	/**
	 * 得到实体的中不符合规范的显示名
	 * 
	 * @param entity
	 * @return
	 */
	private List<String> getEntityShowNames(IEntity entity) {
		List<String> result = new ArrayList<String>();
		List<IAttribute> attributeList = entity.getAttributes();
		String[] itemNames = MmMDConstants.NONSTANDARD_NAMES;
		for (IAttribute attribute : attributeList) {
			String attribDisplayName = attribute.getDisplayName();
			for (String banName : itemNames) {
				if (attribDisplayName.toLowerCase().contains(
						banName.toLowerCase())) {
					result.add(attribDisplayName);
					continue;
				}
			}
		}
		return result;
	}

}
