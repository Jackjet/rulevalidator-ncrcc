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

@RuleDefinition(catalog = CatalogEnum.METADATA, description = "Ԫ�����������������ݿ��ֶ�����У�鹤��", relatedIssueId = "677",
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
			for (IEntity entity : allEntities) { // ʵ�����
				List<IAttribute> attributeList = entity.getAttributes(); // ʵ�����Լ�
				for (IAttribute attribute : attributeList) {
					// ���������list���͵���������ӱ���ֶ�
					if (!attribute.getName().equals(attribute.getFieldName())
							&& !attribute.getTypeStyle().equals(MDRuleConstants.TYPE_STYLE_ARRAY)) {
						noteBuilder.append(String.format("\nʵ�壺%s ����������:%s �� �ֶ����� :%s ��һ��   ", entity.getDisplayName(),
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
