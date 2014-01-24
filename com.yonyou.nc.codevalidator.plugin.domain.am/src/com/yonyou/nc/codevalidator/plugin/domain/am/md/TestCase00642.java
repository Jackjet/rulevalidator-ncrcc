package com.yonyou.nc.codevalidator.plugin.domain.am.md;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractMetadataResourceRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.md.IEntity;
import com.yonyou.nc.codevalidator.resparser.md.IMetadataFile;
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
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * Ԫ������ʾ���Ʋ��ܴ��������ַ�
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "Ԫ������ʾ���Ʋ��ܴ��������ַ�", relatedIssueId = "642",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00642 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuilder noteBuilder = new StringBuilder();
			IMetadataFile metadataFile = resource.getMetadataFile();
			List<IEntity> entitylist = metadataFile.getAllEntities();
			for (IEntity entity : entitylist) {
				String entityDefaultTableName = entity.getTableName();
				if (StringUtils.containsChineseCharacter(entityDefaultTableName)) {
					noteBuilder.append("ʵ��" + entity.getDisplayName() + "Ĭ�ϵı����а���������.(" + entityDefaultTableName
							+ ")\n");
				}
				if (StringUtils.containSpecialCharacter(entityDefaultTableName)) {
					noteBuilder.append("ʵ��" + entity.getDisplayName() + "Ĭ�ϵı����а����������ַ�.(" + entityDefaultTableName
							+ ")\n");
				}
			}

			if (StringUtils.isNotBlank(noteBuilder.toString())) {
				result.addResultElement(resource.getResourcePath(), noteBuilder.toString());
			}
		}
		return result;

	}

}
