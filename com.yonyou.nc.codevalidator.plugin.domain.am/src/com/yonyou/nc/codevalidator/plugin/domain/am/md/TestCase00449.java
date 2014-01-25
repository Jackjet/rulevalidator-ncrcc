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
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "Ԫ���ݱ����ֶ������ȼ��", relatedIssueId = "449",
		subCatalog = SubCatalogEnum.MD_BASESETTING, coder = "xiepch", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00449 extends AbstractMetadataResourceRuleDefinition {

	// Ĭ���ֶ�������
	public static final int DEFAULT_FIELD_LENGTH = 18;

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			StringBuilder noteString = new StringBuilder();
			IMetadataFile metadataFile = resource.getMetadataFile();
			List<IEntity> allEntities = metadataFile.getAllEntities();
			for (IEntity entity : allEntities) {
				// TODO ������ԣ��������������
				// ����У��
				if (entity.getTableName().length() > DEFAULT_FIELD_LENGTH) {
					noteString.append(String.format("ʵ�壺%s �б�%s ���Ƴ���%s λ\n", entity.getDisplayName(),
							entity.getTableName(), DEFAULT_FIELD_LENGTH));
				}
				// Ԫ�����ֶ�У��
				for (IAttribute attribute : entity.getAttributes()) {
					if (attribute.getFieldName().length() > DEFAULT_FIELD_LENGTH) {
						noteString.append(String.format("ʵ�壺%s �б�:%s, �ֶΣ�%s ���Ƴ���%s λ\n", entity.getDisplayName(),
								entity.getTableName(), attribute.getFieldName(), DEFAULT_FIELD_LENGTH));
					}
				}
			}
			if (StringUtils.isNotBlank(noteString.toString())) {
				result.addResultElement(resource.getResourcePath(), noteString.toString());
			}
		}
		return result;
	}

}
