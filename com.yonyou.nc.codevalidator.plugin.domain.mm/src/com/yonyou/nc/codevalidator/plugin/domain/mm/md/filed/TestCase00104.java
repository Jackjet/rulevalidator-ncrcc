package com.yonyou.nc.codevalidator.plugin.domain.mm.md.filed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * Ԫ�����е����Ա�������Ϊ��̬����
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.CHECKOUT, catalog = CatalogEnum.METADATA, description = "Ԫ�����ϵ��������Ա������óɶ�̬����", solution = "Ԫ�����ϵ��������Ա������óɶ�̬����", relatedIssueId = "104", subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "lijbe")
public class TestCase00104 extends AbstractMetadataResourceRuleDefinition {

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

					Set<String> undynamicAttribs = getEntityUndynamic(entity);
					if (undynamicAttribs.size() > 0) {
						result.append(String.format(
								"Ԫ���ݡ�%s ����Ӧ�ֶ��б�:%s ���Ƕ�̬����,���޸� . \n",
								entity.getFullName(), undynamicAttribs));
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
	 * �õ�ʵ����в��Ƕ�̬���Ե��ֶ�
	 * 
	 * @param entity
	 * @return
	 */
	private Set<String> getEntityUndynamic(IEntity entity) {
		Set<String> result = new HashSet<String>();
		List<IAttribute> attributeList = entity.getAttributes();
		for (IAttribute attribute : attributeList) {

			if (!attribute.isDynamic()) {
				result.add(attribute.getFieldName());
				continue;
			}

		}
		return result;
	}

}
