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
@RuleDefinition(catalog = CatalogEnum.METADATA, description = "���Ԫ�����ֶ����Ͳ���ʹ��text���� Uapƽ̨��֧��ntext",
		relatedIssueId = "571", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangguang",
		executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.CHECKOUT)
public class TestCase00571 extends AbstractMetadataResourceRuleDefinition {

	@Override
	protected IRuleExecuteResult processResources(List<MetadataResource> resources, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		for (MetadataResource resource : resources) {
			IMetadataFile metadataFile = resource.getMetadataFile();
			List<IEntity> allEntities = metadataFile.getAllEntities();
			StringBuilder noteBuilder = new StringBuilder();
			for (IEntity entity : allEntities) { // ʵ�����
				List<IAttribute> entityattlist = entity.getAttributes(); // ʵ�����Լ�
				for (IAttribute ia : entityattlist) {
					if (ia.getFieldType().toLowerCase().equals("text")) {
						noteBuilder.append(entity.getDisplayName() + "ʵ���" + ia.getDisplayName() + "�ֶε��ֶ����Ͳ���Ϊ"
								+ ia.getFieldType() + "\n");
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
